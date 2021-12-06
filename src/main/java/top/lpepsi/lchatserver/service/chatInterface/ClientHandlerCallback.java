package top.lpepsi.lchatserver.service.chatInterface;/**
 * @author 林北
 * @description
 * @date 2021-12-02 22:16
 */

import top.lpepsi.lchatserver.service.ClientHandler;

/**
 *@author 林北
 *@description 客户端处理回调接口
 *@date 2021-12-02 22:16
 */
public interface ClientHandlerCallback {
    /**
     * Description: 通知服务器断开当前客户端的链接
     *@param clientHandler
     * return: void
     * Author: 7link
     * Date: 2021-12-04
    */
    void closeClient(ClientHandler clientHandler);

    /**
     * Description: 通知服务端接收消息
     * @param clientHandler
     * @param message
     * return: void
     * Author: 7link
     * Date: 2021-12-04
    */
    void receiveMessage(ClientHandler clientHandler, String message);


}
