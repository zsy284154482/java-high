package com.zsy.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *MappedByteBuffer:
 *      可以让文件直接在内存(堆外内存)中修改,即操作系统不需要拷贝一次
 * @author zsy
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("/home/zsy/Desktop/zsy01.txt", "rw");
        //获取对应通道
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1:FileChannel.MapMode.READ_WRITE  使用读写模式
         * 参数2:0  可以直接修改的起始位置
         * 参数3:5  是映射到内存的大小(不是索引位置)  即文件的多少字节映射到内存中
         * 可以直接修改的范围是0-5
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        map.put(3, (byte) '9');

        randomAccessFile.close();

    }
}
