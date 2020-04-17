package com.zsy.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();

            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将listenChannel注册到selector上
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            //循环处理
            while (true) {
                int count = selector.select();
                if (count > 0) {//说明有事件处理
                    System.out.println(count);
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        //监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            //设置为非阻塞
                            socketChannel.configureBlocking(false);
                            //注册
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示上线
                            System.out.println(socketChannel.getRemoteAddress() + " 上线 ");

                        }
                        if (key.isReadable()) {//通道发生read,即通道是可读的状态
                            //处理读,专门写方法
                            read(key);

                        }
                        //当前的key删除
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待....");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取客户端消息
    private void read(SelectionKey key) {
        //定义一个SocketChannel
        SocketChannel channel = null;
        try {
            //取到关联的channel
            channel = (SocketChannel) key.channel();
            //创建一个Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int count = channel.read(buffer);
            //根据count值做处理
            if (count > 0) {
//                System.out.println(count);
                //把缓存区的数据转换成字符串
                String s = new String(buffer.array());
                //输出该消息
                System.out.println("from 客户端: " + s.trim());

                //向其他的客户端转发消息(去掉自己),专门写一个方法来处理
                sendInfoToOtherClient(s, channel);
            }else {
                try {
                    System.out.println(channel.getRemoteAddress() + " 离线了...");
                    //取消注册
                    key.cancel();
                    //关闭通道
                    channel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //转发消息给其他客户
    private void sendInfoToOtherClient(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中.....");
        //遍历所有注册到selector上的SocketChannel,并排除自己
        for (SelectionKey selectionKey : selector.keys()) {

            //通过key 取出对应的 SocketChannel
            Channel targetChannel = selectionKey.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到Buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将Buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
