package com.zsy.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //创建一个Buffer,大小为5
        IntBuffer intBuffer=IntBuffer.allocate(5);
        //向Buffer存放数据
        for(int i = 1; i <=intBuffer.capacity(); i++){
            intBuffer.put(i*2);
        }
        //怎么从Buffer读取数据
        //将Buffer转换,读写切换
        intBuffer.flip();
        intBuffer.position(1);
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
