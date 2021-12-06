package top.lpepsi.lchatserver.controller.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author 林北
 * @description
 * @date 2021-12-02 22:29
 */
public class CloseUtil {
    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
