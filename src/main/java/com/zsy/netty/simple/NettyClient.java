package com.zsy.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {


            //创建一个客户端的启动对象
            //注意:客户端使用的不是 ServerBootStrap,而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数
            bootstrap.group(group)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端 is OK..");
            //启动客户端连接服务器端
            //关于ChannelFuture 要分析,涉及到netty的异步模型
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668).sync();
            //给关闭通道进行监听   加上sync(),可以是非阻塞的
            cf.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
