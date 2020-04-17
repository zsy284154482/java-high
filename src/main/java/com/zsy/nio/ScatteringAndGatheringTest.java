package com.zsy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数据写入到Buffer时,可以采用Buffer数组,依次写入(分散)
 * Gathering:从Buffer读取数据时,也可以采用Buffer数组,依次读
 *
 * @author zsy
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        //使用ServerSocketChannel 和SocketChannel 网络

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到Socket并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8; //假定从客户端接受8个字节
        //循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;//累计读取字节数
                System.out.println("byteRead"+byteRead);
                //使用流打印,看看当前的这个Buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer->"position"+buffer.position()+", limit"+buffer.limit()).forEach(System.out::println);

            }
            //将所有的Buffer进行flip
            Arrays.stream(byteBuffers).forEach(buffer->buffer.flip());
            //将数据显示到客户端
            long byteWrite=0;
            while (byteWrite<messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite+=l;
            }
            //将所有的Buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer->buffer.clear());

            System.out.println("byteRead"+byteRead+"\tbyteWrite"+byteWrite+"\tmessageLength"+messageLength);
        }
    }
}
