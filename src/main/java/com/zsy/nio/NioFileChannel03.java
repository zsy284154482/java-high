package com.zsy.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件复制
 * @author zsy
 */
public class NioFileChannel03 {
    public static void main(String[] args) throws Exception {
        File file=new File("/home/zsy/Desktop/zsy01.txt");
        FileInputStream fileInputStream=new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream=new FileOutputStream("/home/zsy/Desktop/zsy02.txt");
        FileChannel fileChannel1 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer= ByteBuffer.allocate(512);

        while (true){//循环读取
            byteBuffer.clear();
            int read = fileChannel.read(byteBuffer);

            if (read==-1){
                break;
            }
            byteBuffer.flip();
            fileChannel1.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
