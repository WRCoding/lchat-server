package top.lpepsi.lchatserver.service;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.lpepsi.lchatserver.dao.mapper.GroupMemberMapper;
import top.lpepsi.lchatserver.dao.mapper.MessageMapper;
import top.lpepsi.lchatserver.dao.mapper.UserGroupMessageMapper;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.MsgType;
import top.lpepsi.lchatserver.entity.Response;
import top.lpepsi.lchatserver.entity.Type;
import top.lpepsi.lchatserver.entity.group.GroupMessage;
import top.lpepsi.lchatserver.entity.group.UserGroupMessage;
import top.lpepsi.lchatserver.service.chatInterface.ClientHandlerCallback;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.Resources;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author 林北
 * @description 服务器socket
 * @date 2021-08-20 09:26
 */
@Slf4j
@Component
public class ChatServer implements ClientHandlerCallback {


    protected ConcurrentHashMap<String, Socket> userSocketMap = new ConcurrentHashMap<>();


    private CopyOnWriteArrayList<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>();

    private ConcurrentHashMap<String, ClientHandler> userClientMap = new ConcurrentHashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();

    Thread thread = new Thread(() -> connectClient(), "L-chat-server");

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private GroupMemberMapper groupMemberMapper;

//    @Resource
//    private GroupMessageMapper groupMessageMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserGroupMessageMapper userGroupMessageMapper;

    @PostConstruct
    public void init() {
        thread.start();
    }

    private void connectClient() {
        try {
            ServerSocket serverSocket = new ServerSocket(7778);
            log.info("---服务器启动---");
            while (true) {
                System.out.println("---正在监听---");
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client, this);
                clientHandlers.add(clientHandler);
                clientHandler.read();
//                registerClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeClient(ClientHandler clientHandler) {
        log.info("客户端: {} , 下线了", clientHandler.getClientInfo());
        String key = null;
        for (Map.Entry<String, ClientHandler> next : userClientMap.entrySet()) {
            if (next.getValue().getClientHandlerId().equals(clientHandler.getClientHandlerId())) {
                key = next.getKey();
            }
        }
        userClientMap.remove(key);
        stringRedisTemplate.delete(key);
        clientHandlers.remove(clientHandler);
    }


    @Override
    public void receiveMessage(ClientHandler clientHandler, String message) {
        //todo 解析读取的信息
        log.info(clientHandler.getClientInfo() + " : " + message);
        try {
            Message value = mapper.readValue(message, Message.class);
            parseType(value, clientHandler);
        } catch (JsonProcessingException e) {
            log.error("receiveMessage JSON 解析失败 : {}", e);
        }
    }


    public void parseType(Message message, ClientHandler clientHandler) {
        //兼容之前的代码,如果消息类型为空,默认为单聊
        if (message.getType() == null) {
            message.setType(Type.SINGLE.getType());
        }
        switch (Type.getByType(message.getType())) {
            case INIT:
                initConnect(message.getFrom(), clientHandler);
                break;
            case SINGLE:
                handlerSingle(message);
                break;
            case GROUP:
                handlerGroup(message);
                break;
            case QUIT:
                handleQuit(message.getFrom(), clientHandler);
                break;
            default:
                throw new UnsupportedOperationException("unsupported type : " + message.getType());
        }
    }

    private void initConnect(String from, ClientHandler clientHandler) {
        if (userClientMap.get(from) == null && isOnLine(from)) {
            userClientMap.put(from, clientHandler);
        } else {
            throw new RuntimeException("initConnect 发送异常, " + from + " 重复初始化");
        }
    }

    private void handleQuit(String from, ClientHandler clientHandler) {
        if (userClientMap.get(from) != null) {
            ClientHandler mapClientHandler = userClientMap.get(from);
            if (mapClientHandler.getClientHandlerId().equals(clientHandler.getClientHandlerId())) {
                clientHandler.exitBySelf();
                userClientMap.remove(from);
            } else {
                String exception = "handleQuit 发送异常," + from +
                        " 对应的clientHandlerId: " +
                        clientHandler.getClientHandlerId() +
                        " 有误";
                throw new RuntimeException(exception);
            }
        }
    }

    //第二版加入可靠性
    //1.前端发送消息时设置一个定时器，等待ack
    //2.server端接收到消息后，回调函数发送ack给客户端
    private <T extends Message> void handlerSingle(T message) {
        switch (MsgType.getByType(message.getMsgType())) {
            case TEXT:
                parseText(message);
                break;
            case IMAGE:
                parseImage(message);
                break;
            default:
                throw new UnsupportedOperationException("unsupported msgType : " + message.getMsgType());
        }
    }

    private <T extends Message> void handlerGroup(T message) {
        //todo 处理群聊消息
        String groupId = message.getTo();
        long msgSeq = message.getMsgSeq();
        //父类无法强制转成子类：GroupMessage groupMessage = (GroupMessage) message;

        //将消息转换成群聊消息
        //1.保存群聊消息
        messageMapper.insertGroupMessage(message);
        //2.保存群成员对群消息的映射关系,在线的直接转发,只存储离线的
        List<String> list = groupMemberMapper.findLcidByGroupId(groupId);
        List<String> onlineList = new ArrayList<>();
        List<UserGroupMessage> userGroupMessageList = new ArrayList<>();
        log.info("list: {}", list.toString());
        for (String lcid : list) {
            if (isOnLine(lcid)) {
                if (!lcid.equals(message.getFrom())) {
                    onlineList.add(lcid);
                }
            } else {
                userGroupMessageList.add(UserGroupMessage.instance(lcid, groupId, msgSeq));
            }
        }
        log.info("online lcid : {}", onlineList);
        //3.在线的进行发送,
        for (String online : onlineList) {
            ClientHandler clientHandler = userClientMap.get(online);
            switch (MsgType.getByType(message.getMsgType())) {
                case TEXT:
                    parseText(message, clientHandler);
                    break;
                case IMAGE:
                    parseImage(message, clientHandler);
                    break;
                default:
                    throw new UnsupportedOperationException("unsupported msgType : " + message.getMsgType());
            }
        }
        //离线的写入kafka
        if (!userGroupMessageList.isEmpty()) {
            sendGroupMsgToKafka(userGroupMessageList);
        }

//        for (String lcid : list) {
//            handlerSingle(message);
//        }
    }

    /**
     * Description: 将离线群聊信息写入kafka
     *
     * @param userGroupMessageList return void
     *                             Author: ink
     *                             Date: 2022/1/17
     */
    private void sendGroupMsgToKafka(List<UserGroupMessage> userGroupMessageList) {
        //batch-size
//        kafkaTemplate.send()
        for (UserGroupMessage userGroupMessage : userGroupMessageList) {
            String message = JSONUtil.toJsonStr(userGroupMessage);
            kafkaTemplate.send("lchat-group", message).addCallback(success -> {
                log.info("成功发送消息到,topic:{} ,partition:{} ,offset:{}", success.getRecordMetadata().topic(),
                        success.getRecordMetadata().partition(), success.getRecordMetadata().offset());
            }, error -> {
                log.error("消息发送失败: {}", error.getMessage());
            });
        }


    }

    private void parseText(Message message) {
        String to = message.getTo();
        //判断是否在线
        if (isOnLine(to)) {
            ClientHandler clientHandler = getClientHandler(to);
            parseText(message, clientHandler);
        } else {
            sendMsgToKafKa(message);
        }
    }

    private void parseText(Message message, ClientHandler clientHandler) {
        try {
            clientHandler.send(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("parseText JSON 解析失败 : {}", e);
        }
    }

    private void parseImage(Message message) {
        String to = message.getTo();
        if (isOnLine(to)) {
            ClientHandler clientHandler = getClientHandler(to);
            parseImage(message, clientHandler);
        } else {
            sendMsgToKafKa(message);
        }
    }

    private void parseImage(Message message, ClientHandler clientHandler) {
        Message tempMessage = new Message();
        BeanUtils.copyProperties(message,tempMessage);
        handlerUrl(tempMessage);
        try {
            clientHandler.send(mapper.writeValueAsString(tempMessage));
        } catch (JsonProcessingException e) {
            log.error("parseImage JSON 解析失败 : {}", e);
        }
    }

    private void handlerUrl(Message message) {
        String prefix = "lchatimage/";
        String suffix = ".png";
        String imageFlag = message.getMessage();
        log.info("image path : {}", prefix + imageFlag + suffix);
        message.setMessage(prefix + imageFlag + suffix);
    }

    private ClientHandler getClientHandler(String to) {
        return userClientMap.get(to);
    }

    private void sendMsgToKafKa(Message data) {
        String message = JSONUtil.toJsonStr(data);
        kafkaTemplate.send("lchat", message).addCallback(success -> {
            log.info("成功发送消息到,topic:{} ,partition:{} ,offset:{}", success.getRecordMetadata().topic(),
                    success.getRecordMetadata().partition(), success.getRecordMetadata().offset());
        }, error -> {
            log.error("消息发送失败: {}", error.getMessage());
        });
    }

    private boolean isOnLine(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public Response getClient() {
        return Response.success(userSocketMap.keys());
    }


//    class ClientThread extends Thread{
//        private Socket client;
//        public ClientThread(Socket client,String name) {
//            super(name);
//            this.client = client;
//        }
//
//        @Override
//        public void run() {
//            try(BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
//                String clientInfo = client.getInetAddress().getHostAddress()+" : "+client.getPort();
//                log.info("new connect [client info : {}]",clientInfo);
//                char[] buffer = new char[1024];
//                while(!Thread.currentThread().isInterrupted()){
//                    int len;
//                    len = reader.read(buffer, 0, buffer.length);
//                    String message = new String(buffer,0,len);
//                    log.info("Receive message '{}' from {} ",message,clientInfo);
//                    handleMessage(message);
////                    sendToOtherClient(message,client);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void handleMessage(String message) {
//            try {
//                Message data = mapper.readValue(message, Message.class);
//                if (!isOnLine(data.getTo()) && !isSystemType(data)){
//                    parseOffLineMessage(data);
//                }else{
//                    parseOnLineMessage(data);
//                }
//            } catch (JsonProcessingException e) {
//                log.error("JSON 解析失败 : {}",e);
//            }
//        }
//
//        private boolean isSystemType(Message data) {
//            return data.getMsgType().equals(MsgType.QUIT.getType()) || data.getMsgType().equals(MsgType.INIT.getType());
//        }
//
//        private void parseOffLineMessage(Message data) {
//            if (data.getMsgType().equals(MsgType.IMAGE.getType())){
//                String prefix = "lchatimage/";
//                String suffix = ".png";
//                String imageFlag = data.getMessage();
//                log.info("image path : {}",prefix + imageFlag + suffix);
//                data.setMessage(prefix + imageFlag + suffix);
//            }
//            sendMesToMQ(data);
//        }
//
//        private boolean isOnLine(String to) {
//            return stringRedisTemplate.hasKey(to);
//        }
//
//        private void parseOnLineMessage(Message data) {
////            switch (MsgType.getByType(data.getMsgType())){
////                case INIT:
////                    initConnect(data);
////                    break;
////                case TEXT:
////                    parseText(data);
////                    break;
////                case IMAGE:
////                    parseImage(data);
////                    break;
////                case QUIT:
////                    handleQuit(data);
////                    break;
////                default:
////                    throw new UnsupportedOperationException("unsupported msgType : "+data.getMsgType());
////            }
//        }
//
//        private void parseImage(Message data) {
//            String prefix = "lchatimage/";
//            String suffix = ".png";
//            String imageFlag = data.getMessage();
//            log.info("image path : {}",prefix + imageFlag + suffix);
//            data.setMessage(prefix + imageFlag + suffix);
//            try {
//                sendToOtherClient(mapper.writeValueAsString(data),userSocketMap.get(data.getTo()));
//            } catch (JsonProcessingException e) {
//                log.error("parseImage JSON 解析失败 : {}",e);
//            }
//        }
//
//        private void parseText(Message data) {
//            try {
//                sendToOtherClient(mapper.writeValueAsString(data),userSocketMap.get(data.getTo()));
//            } catch (JsonProcessingException e) {
//                log.error("parseText JSON 解析失败 : {}",e);
//            }
//        }
//
//        private void sendMesToMQ(Message data) {
//            String message = JSONUtil.toJsonStr(data);
//            kafkaTemplate.send("lchat",message).addCallback( success -> {
//                log.info("成功发送消息到,topic:{} ,partition:{} ,offset:{}",success.getRecordMetadata().topic(),
//                        success.getRecordMetadata().partition(),success.getRecordMetadata().offset());
//            },error -> {
//                log.error("消息发送失败: {}",error.getMessage());
//            });
//        }
//
//        private void initConnect(Message data) {
//            try {
//                userSocketMap.put(data.getFrom(),client);
//                printClientInfo();
//                clientList.add(client);
//                socketList.put(client,client.getOutputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void handleQuit(Message data) {
//            //获取断开连接的用户ID
//            final String from = data.getFrom();
//            final Socket disconnectSocket = userSocketMap.get(from);
//            userSocketMap.remove(from);
//            clientList.remove(disconnectSocket);
//            socketList.remove(disconnectSocket);
//            stringRedisTemplate.delete(from);
//            this.interrupt();
//            try {
//                if (client != null){
//                    client.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void printClientInfo() {
//            userSocketMap.forEach((key,value) -> {
//                log.info(key + " : " + value);
//            });
//        }
//    }
//    private synchronized void sendToOtherClient(String message,Socket client) {
//        try{
//            log.info("send to {} , message : {}",client,message);
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketList.get(client)));
//            writer.write(message + "\n");
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
