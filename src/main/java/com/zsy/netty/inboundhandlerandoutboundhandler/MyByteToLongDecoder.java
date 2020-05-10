package com.zsy.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * decoder 会根据接受的数据,被调用多次,直到确定新的元素被添加到list,或者ByteBuf没有更多的可读字节为止
     * 如果list out 不为空会将list内容传到下一个Channelinboundhandler处理,该处理器的方法会被调用多次
     *
     *
     * @param ctx  上下文对象
     * @param in  入栈的ByteBuf
     * @param out  集合,将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder 被调用");
        //因为Long是8个字节
        if (in.readableBytes()>= 8){
            out.add(in.readLong());
        }
    }
}
