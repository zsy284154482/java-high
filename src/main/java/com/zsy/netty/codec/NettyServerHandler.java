package com.zsy.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明:
 * 1.我们自定义一个handler 需要继承netty 规定好的某个HandlerAdapter
 * 2.这时我们自定义一个handler   ,才能成为一个handler
 *
 * @author zsy
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据实际(这里我们可以读取客户端发送的消息)
        /*
        1.ChannelHandlerContext ctx: 上下文对象,含有  管道pipeLine ,通道Channel, 地址
        2.Object msg:就是客户端发送的数据  ,默认Object
         */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //比如这里我们有一个非常耗时的业务 -> 异步执行 -> 提交给Channel对应的NioEventLoop的 TaskQueue中

        //解决方案1:用户程序自定义普通任务

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端2", CharsetUtil.UTF_8));
                    System.out.println("go on ...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //用户自定义定时任务 -> 该任务提交到scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端3", CharsetUtil.UTF_8));
                    System.out.println("go on ...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },5, TimeUnit.SECONDS);




        /*System.out.println("ctx = "+ctx);
        //将msg转成一个ByteBuf
        //ByteBuf 是Netty提供的,不是Nio的ByteBuffer
        ByteBuf buf= (ByteBuf) msg;
        System.out.println("客户端发送的消息是: "+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址是: "+ctx.channel().remoteAddress());*/
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存,并读取到Channel
        //一般的讲,我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端", CharsetUtil.UTF_8));
    }

    //处理异常,一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
