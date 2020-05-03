package com.zsy.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        //创建BossGroup 和WorkerGroup
        //说明:
        //1.创建两个线程组:BossGroup 和WorkerGroup
        //2.bossGroup只是处理连接请求,真正的客户端业务处理,会交给workerGroup
        //3.两个都是无线循环
        //4.BossGroup 和WorkerGroup 含有的子线程数(NioEventLoop)的个数,实际=cpu核数*2
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {


            //创建服务端的启动对象,配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试

                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户SocketChannel hashcode= "+ch.hashCode());
                            //可以用一个集合管理SocketChannel,在推送消息时,可以将业务加入到各个Channel对应的NioEventLoop的taskQueue
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("服务器 is ready");

            //绑定一个端口并且同步,生成了一个ChannelFuture对象
            //启动服务器并绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册一个监听器,监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("监听端口 6668成功");
                    }else {
                        System.out.println("监听端口 6668失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
