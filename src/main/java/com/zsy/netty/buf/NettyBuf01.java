package com.zsy.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyBuf01 {
    public static void main(String[] args) {

        /*
        //创建一个ByteBuf
        //说明
        //1.创建了一个对象,该对象包含一个数组,是一个byte[10]
        //2.在 netty的byteBuf中,不需要使用flip进行反转
        //  因为底层维护了readerIndex和writerIndex
        3.通过readerIndex和writerIndex和capacity,将ByteBuf分成了三个区域
            0--readerIndex :已经读取的区域
            readerIndex--writerIndex :可读区域
            writerIndex--capacity :可写的区域
         */

        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println(buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
//            System.out.println(buffer.getByte(i));
            System.out.println(buffer.readByte());
        }

    }
}
