package com.zsy.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件复制
 * @author zsy
 */
public class NioFileChannel04 {
    public static void main(String[] args) throws Exception {
        File file=new File("/home/zsy/Desktop/1.jpg");
        FileInputStream fileInputStream=new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream=new FileOutputStream("/home/zsy/Desktop/2.jpg");
        FileChannel fileChannel1 = fileOutputStream.getChannel();

        fileChannel1.transferFrom(fileChannel,0,fileChannel.size());


        fileInputStream.close();
        fileOutputStream.close();
    }
}
