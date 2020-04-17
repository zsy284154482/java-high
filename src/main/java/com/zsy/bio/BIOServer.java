package com.zsy.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    //线程池机制

    /**
     * 思路:
     * 创建一个线程池
     * 如果有客户端连接,就创建一个线程,与之通讯
     */
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true) {
            System.out.println("等待连接"+"\t"+Thread.currentThread().getName());
            final Socket socket = serverSocket.accept();

            System.out.println("连接到一个用户");

            threadPool.execute(new Runnable() {
                public void run() {
                    handle(socket);
                }
            });
        }
    }

    public static void handle(Socket socket) {
        try {
            System.out.println(Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream in = socket.getInputStream();
            while (true){
                System.out.println(Thread.currentThread().getName());
                System.out.println("read.....");
                int read=in.read(bytes);
                if (read!=-1){
                    System.out.println(new String(bytes,0,read)); //输出客户端发送的数据
                }{
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
