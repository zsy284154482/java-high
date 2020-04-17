package com.zsy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws Exception {
        //创建一个ServerSocketChannel - >ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666,在服务器上监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把ServerSocketChannel注册到selector   事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的SelectionKey的数量  "+selector.keys().size());

        //循环等待客户端连接
        while (true){

            //这里我们等待一秒,如果没有事件发生,返回
            if (selector.select(1000)==0){
                System.out.println("服务器等待1秒,无连接");
                continue;
            }
            //如果返回的>0,就获取相关的SelectionKey集合
            //1.如果返回的>0,表示已经获取到关注的事件
            //2.selector.selectedKeys()返回关注事件的集合,通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("selectionKeys数量   "+selectionKeys.size());
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件进行相应的处理
                if (key.isAcceptable()){//如果是OP_ACCEPT,有新的客户端连接
                    //该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //设置为非阻塞
                    socketChannel.configureBlocking(false);

                    System.out.println("客户端连接成功 生成一个SocketChannel  "+socketChannel.hashCode());
                    //将socketChannel注册到Selector,关注事件为OP_ACCEPT,同    时给SocketChannel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接注册后的SelectionKey的数量  "+selector.keys().size());
                }
                if (key.isReadable()){//发生OP_READ
                    //通过key反向获取对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取该Channel关联的Buffer
                    ByteBuffer buffer= (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端"+"\t"+new String(buffer.array()));
                }

                //手动从集合中移动当前的SelectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
