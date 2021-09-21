package top.lpepsi.lchatserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.lpepsi.lchatserver.entity.Message;
import top.lpepsi.lchatserver.entity.MsgType;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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

    Thread thread = new Thread(() -> connectClient(),"L-chat-server");

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
            ObjectMapper mapper = new ObjectMapper();
            try {
                Message data = mapper.readValue(message, Message.class);
                parseMessage(data);
            } catch (JsonProcessingException e) {
                log.error("JSON 解析失败 : {}",e);
            }
        }

        private void parseMessage(Message data) {
            switch (MsgType.getByType(data.getMsgType())){
                case INIT:
                    initConnect(data);
                    break;
                case TEXT:
                    parseText(data);
                    break;
                default:
                    throw new UnsupportedOperationException("unsupported msgType : "+data.getMsgType());
            }
        }

        private void parseText(Message data) {
            String to = data.getTo();
            sendToOtherClient(data.getMessage(),userSocketMap.get(to));
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
