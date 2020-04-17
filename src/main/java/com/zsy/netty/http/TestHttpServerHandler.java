package com.zsy.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明:
 * 1.SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter的子类
 * 2.HttpObject 是客户端和服务器相互通信的数据被封装成HttpObject
 * @author zsy
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg 是不是 httpRequest请求
        if (msg instanceof HttpRequest){

            //获取到
            HttpRequest httpRequest= (HttpRequest) msg;
            //获取uri,过滤特定资源
            URI uri=new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico,不作响应 ");
                return;
            }
            System.out.println("msg类型: "+msg.getClass());
            System.out.println("客户端地址: "+ ctx.channel().remoteAddress());

            //回复消息给浏览器[http协议]
            ByteBuf buf= Unpooled.copiedBuffer("hello,我是服务器",CharsetUtil.UTF_8);

            //构建一个http的响应,即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,buf.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
