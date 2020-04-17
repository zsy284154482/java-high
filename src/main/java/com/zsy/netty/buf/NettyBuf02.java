package com.zsy.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyBuf02 {
    public static void main(String[] args) {
            ByteBuf buf=Unpooled.copiedBuffer("Hello,World!", Charset.forName("UTF-8"));

            //使用相关的方法
        if (buf.hasArray()){
            byte[] content = buf.array();

            //将content转换为字符串
            System.out.println(new String(content,Charset.forName("UTF-8")).trim());

            System.out.println("byteBuf= "+buf);

//            System.out.println(buf.readByte());

            System.out.println(buf.arrayOffset());//0
            System.out.println(buf.readerIndex());//0
            System.out.println(buf.writerIndex());//12
            System.out.println(buf.capacity());


            System.out.println(buf.getByte(0));
        }

    }
}
