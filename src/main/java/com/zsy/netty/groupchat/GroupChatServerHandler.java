package com.zsy.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个Channel组,管理所有的Channel
    //GlobalEventExecutor.INSTANCE 是全局的事件执行器,是一个单例
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
    handlerAdded 表示连接建立, 一旦连接,第一个被执行
    将当前的Channel加入到channelGroup
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        /**
         * 该方法会将ChannelGroup中所有的Channel遍历,并发送 消息
         * 我们不需要自己遍历
         */
        channelGroup.writeAndFlush(sdf.format(new Date())+": [客户端]"+ channel.remoteAddress()+" 加入聊天\n");
        channelGroup.add(channel);
    }

    //表示channel处于一个活动的状态,提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 上线了~");
    }

    //表示channel处于一个不活动的状态,提示xx离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+" 离线了~");
    }

    //断开连接 将xx客户离开信息推送个当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(sdf.format(new Date())+" :[客户端]"+ channel.remoteAddress()+" 离开了\n");
        System.out.println("channelGroup Size: "+channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前Channel
        Channel channel = ctx.channel();
        //这时我们遍历ChannelGroup,根据不同的情况,回送不同的消息
        channelGroup.forEach(ch ->{
            if (channel !=ch){
                ch.writeAndFlush(sdf.format(new Date())+" :[客户]"+channel.remoteAddress() +" 发送了消息 "+msg+"\n");
            }else {
                ch.writeAndFlush(sdf.format(new Date())+" :[自己]发送了消息 "+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭
        ctx.close();
    }
}
