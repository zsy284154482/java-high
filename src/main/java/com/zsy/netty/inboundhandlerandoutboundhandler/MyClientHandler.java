package com.zsy.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip "+ctx.channel().remoteAddress()+" 收到服务器的消息 "+msg);
    }
    //发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");

        ctx.writeAndFlush(123456L);
        /**
         * abcdabcdabcdabcd 是16个字节
         * 该处理器的前一个handler 是MyLongToByteEncoder
         * MyLongToByteEncoder 父类 MessageToByteEncoder
         * 因此我们编写encoder 时要注意传入的数据类型和处理的数据类型一致
         */
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
