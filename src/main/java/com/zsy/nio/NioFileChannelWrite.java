package com.zsy.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannelWrite {
    public static void main(String[] args) throws Exception {
        String s="你好,张斯宇";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("/home/zsy/Desktop/zsy01.txt");
        //通过fileOutputStream获取对应的Channel
        FileChannel filechannel = fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将s放入byteBuffer
        byteBuffer.put(s.getBytes());
        //进行翻转
        byteBuffer.flip();

        filechannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
