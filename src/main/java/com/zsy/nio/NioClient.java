package com.zsy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)){

            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间,客户端不会阻塞,可以其他工作");
            }
        }
        //如果成功,就发送数据
        String str="hello,张斯宇";

        ByteBuffer buffer=ByteBuffer.wrap(str.getBytes());
        //发送数据,将Buffer写入Channel
        socketChannel.write(buffer);

        System.in.read();
    }
}
