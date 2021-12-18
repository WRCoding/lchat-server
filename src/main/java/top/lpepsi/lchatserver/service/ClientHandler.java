package top.lpepsi.lchatserver.service;

import lombok.extern.slf4j.Slf4j;
import top.lpepsi.lchatserver.core.util.CloseUtil;
import top.lpepsi.lchatserver.service.chatInterface.ClientHandlerCallback;

import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 林北
 * @description 客户端连接处理类
 * @date 2021-12-02 22:10
 */
@Slf4j
public class ClientHandler {

    /** 每个ClientHandler都有唯一的ID */
    private String clientHandlerId;

    private Socket socket;
    /**  处理读*/
    private ReadHandler readHandler;
    /** 处理写 */
    private WriteHandler writeHandler;

    private ClientHandlerCallback clientHandlerCallback;

    private String clientInfo;

    public ClientHandler(Socket socket, ClientHandlerCallback clientHandlerCallback) throws IOException {
        this.clientHandlerId = UUID.randomUUID().toString();
        this.socket = socket;
        this.readHandler = new ReadHandler(socket.getInputStream());
        this.writeHandler = new WriteHandler(socket.getOutputStream());
        this.clientHandlerCallback = clientHandlerCallback;
        this.clientInfo = createClientInfo(socket);
        log.info("新客户端连接: {}",this.clientInfo);
    }

    private String createClientInfo(Socket socket) {
        return "[ " + socket.getInetAddress().getHostAddress()+" : "+socket.getPort() + " ]";
    }

    public String getClientHandlerId() {
        return clientHandlerId;
    }

    private void exit(){
        readHandler.exit();
        writeHandler.exit();
        CloseUtil.close(socket);
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void read(){
        readHandler.start();
    }

    public void send(String message){
        writeHandler.send(message);
    }

    public void exitBySelf() {
        exit();
        /** 通知服务端有客户端下线 */
        clientHandlerCallback.closeClient(this);
    }

    class ReadHandler extends Thread{

        /** socket输入流 */
        private InputStream inputStream;
        /** 标识线程是否终止,即客户端是否断开 */
        private boolean stop = false;
        /** 用线程池将读到的信息回调ChatServer,提高并发 */
        private ThreadPoolExecutor readHandlerThreadPool = new ThreadPoolExecutor(1,3,60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10));

        public ReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        @Override
        public void run() {
            read();
        }

        public void read(){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                do{
                    String message = reader.readLine();
                    log.info("message : {}", message);
                    readHandlerThreadPool.execute(() -> clientHandlerCallback.receiveMessage(ClientHandler.this,message));
                }while (!stop);
            } catch (Exception e) {
                if (!stop){
                    log.error("客户端: {} ,异常断开连接, e : {}",clientInfo,e.getMessage());
                    ClientHandler.this.exitBySelf();
                }
            }
        }

        public void exit() {
            stop = true;
            CloseUtil.close(inputStream);
            readHandlerThreadPool.shutdownNow();
        }
    }

    class WriteHandler{
        private BufferedWriter writer;
        private boolean stop = false;
        /** 用线程池将发送信息,提高并发 */
        private ThreadPoolExecutor writeHandlerThreadPool = new ThreadPoolExecutor(1,3,60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10));

        public WriteHandler(OutputStream outputStream) {
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        }

        public void send(String message){
            try{
                writeHandlerThreadPool.execute(() -> {
                    if (stop){
                        return;
                    }
                    try {
                        writer.write(message);
                        writer.flush();
                    } catch (IOException e) {
                        log.error("发送数据失败, e : {}",e.getMessage());
                    }
                });
            }catch (Exception e){
                if (!stop){
                    log.error("客户端: {} ,异常断开连接, e : {}",clientInfo,e.getMessage());
                    ClientHandler.this.exitBySelf();
                }
            }
        }
        public void exit() {
            stop = true;
            CloseUtil.close(writer);
            writeHandlerThreadPool.shutdownNow();
        }
    }


}
