package com.zsy.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    //定义相关的属性
    private final String HOST="127.0.0.1";//服务器的ip
    private final int PORT=6667;//服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;
    //构造器
    public GroupChatClient() throws IOException {
        selector=Selector.open();
        //连接服务器
        socketChannel=SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //注册
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username=socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+" is ok....");
    }
    //向服务器发送消息
    public void sendInfo(String info){
        info=username+" 说: "+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //读取从服务器端回复的消息
    public void read(){
        try {
            int readChannels = selector.select();

            if (readChannels>0){//又可以用的通道

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        //得到相关的通道
                        SocketChannel socketChannel= (SocketChannel) key.channel();
                        //得到一个Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        //把读到的缓冲区的数据转成字符串
                        String msg=new String(buffer.array());
                        System.out.println(msg.trim());//把头尾的空格去掉

                    }

                }
                iterator.remove();
            }else {
                System.out.println("没有可用的通道");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();

        //启动一个线程,每隔三秒,读取从服务器发送数据
        new Thread(() -> {
            while (true){
                groupChatClient.read();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }
}
