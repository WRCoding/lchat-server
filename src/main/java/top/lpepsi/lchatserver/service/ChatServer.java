package top.lpepsi.lchatserver.service;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.MsgType;
import top.lpepsi.lchatserver.entity.Response;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 林北
 * @description 服务器socket
 * @date 2021-08-20 09:26
 */
@Slf4j
@Component
public class ChatServer {

    protected CopyOnWriteArrayList<Socket> clientList = new CopyOnWriteArrayList<>();

    protected ConcurrentHashMap<String,Socket> userSocketMap = new ConcurrentHashMap<>();

    protected ConcurrentHashMap<Socket,OutputStream> socketList = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    Thread thread = new Thread(() -> connectClient(),"L-chat-server");

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate<String ,String> kafkaTemplate;


    @PostConstruct
    public void init() {
        thread.start();
    }

    private void connectClient(){
        try {
            ServerSocket serverSocket = new ServerSocket(7778);
            log.info("---服务器启动---");
            while (true){
                System.out.println("---正在监听---");
                Socket client = serverSocket.accept();
                registerClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerClient(Socket client) throws IOException {
        ClientThread clientThread = new ClientThread(client,client.getPort() + "-Thread");
        clientThread.start();
    }

    public Response getClient() {
        return Response.success(userSocketMap.keys());
    }


    class ClientThread extends Thread{
        private Socket client;
        public ClientThread(Socket client,String name) {
            super(name);
            this.client = client;
        }

        @Override
        public void run() {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                String clientInfo = client.getInetAddress().getHostAddress()+" : "+client.getPort();
                log.info("new connect [client info : {}]",clientInfo);
                char[] buffer = new char[1024];
                while(!Thread.currentThread().isInterrupted()){
                    int len;
                    len = reader.read(buffer, 0, buffer.length);
                    String message = new String(buffer,0,len);
                    log.info("Receive message '{}' from {} ",message,clientInfo);
                    handleMessage(message);
//                    sendToOtherClient(message,client);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleMessage(String message) {
            try {
                Message data = mapper.readValue(message, Message.class);
                if (!isOnLine(data.getTo()) && !isSystemType(data)){
                    parseOffLineMessage(data);
                }else{
                    parseOnLineMessage(data);
                }
            } catch (JsonProcessingException e) {
                log.error("JSON 解析失败 : {}",e);
            }
        }

        private boolean isSystemType(Message data) {
            return data.getMsgType().equals(MsgType.QUIT.getType()) || data.getMsgType().equals(MsgType.INIT.getType());
        }

        private void parseOffLineMessage(Message data) {
            if (data.getMsgType().equals(MsgType.IMAGE.getType())){
                String prefix = "lchatimage/";
                String suffix = ".png";
                String imageFlag = data.getMessage();
                log.info("image path : {}",prefix + imageFlag + suffix);
                data.setMessage(prefix + imageFlag + suffix);
            }
            sendMesToMQ(data);
        }

        private boolean isOnLine(String to) {
            return stringRedisTemplate.hasKey(to);
        }

        private void parseOnLineMessage(Message data) {
            switch (MsgType.getByType(data.getMsgType())){
                case INIT:
                    initConnect(data);
                    break;
                case TEXT:
                    parseText(data);
                    break;
                case IMAGE:
                    parseImage(data);
                    break;
                case QUIT:
                    handleQuit(data);
                    break;
                default:
                    throw new UnsupportedOperationException("unsupported msgType : "+data.getMsgType());
            }
        }

        private void parseImage(Message data) {
            String prefix = "lchatimage/";
            String suffix = ".png";
            String imageFlag = data.getMessage();
            log.info("image path : {}",prefix + imageFlag + suffix);
            data.setMessage(prefix + imageFlag + suffix);
            try {
                sendToOtherClient(mapper.writeValueAsString(data),userSocketMap.get(data.getTo()));
            } catch (JsonProcessingException e) {
                log.error("parseImage JSON 解析失败 : {}",e);
            }
        }

        private void parseText(Message data) {
            try {
                sendToOtherClient(mapper.writeValueAsString(data),userSocketMap.get(data.getTo()));
            } catch (JsonProcessingException e) {
                log.error("parseText JSON 解析失败 : {}",e);
            }
        }

        private void sendMesToMQ(Message data) {
            String message = JSONUtil.toJsonStr(data);
            kafkaTemplate.send("lchat",message).addCallback( success -> {
                log.info("成功发送消息到,topic:{} ,partition:{} ,offset:{}",success.getRecordMetadata().topic(),
                        success.getRecordMetadata().partition(),success.getRecordMetadata().offset());
            },error -> {
                log.error("消息发送失败: {}",error.getMessage());
            });
        }

        private void initConnect(Message data) {
            try {
                userSocketMap.put(data.getFrom(),client);
                printClientInfo();
                clientList.add(client);
                socketList.put(client,client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleQuit(Message data) {
            //获取断开连接的用户ID
            final String from = data.getFrom();
            final Socket disconnectSocket = userSocketMap.get(from);
            userSocketMap.remove(from);
            clientList.remove(disconnectSocket);
            socketList.remove(disconnectSocket);
            stringRedisTemplate.delete(from);
            this.interrupt();
            try {
                if (client != null){
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void printClientInfo() {
            userSocketMap.forEach((key,value) -> {
                log.info(key + " : " + value);
            });
        }
    }
    private synchronized void sendToOtherClient(String message,Socket client) {
        try{
            log.info("send to {} , message : {}",client,message);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketList.get(client)));
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
