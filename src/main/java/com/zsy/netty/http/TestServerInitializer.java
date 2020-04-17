package com.zsy.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty提供的httpServerCodec  =->[coder  decoder]
        //HttpServerCodec 说明
        //1. HttpServerCodec是netty提供的处理http的 编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
//        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
//        pipeline.addLast(new StringDecoder(Charset.forName("GBK")));
        //2.增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }
}
