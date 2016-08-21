package com.glsx.main;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Created by xiaolong on 2016/8/1.
 */
public class DirectByteBufferCleaner {

    public static void main(String[] args) {
        while (true) {
            sleep(1000);
            System.out.println("start allocate....");
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 300);
            System.out.println("end allocate....");
            sleep(5000);
            clean(byteBuffer);
            System.out.println("end ...");
            sleep(1000);
        }
    }

    public static void clean(final ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            System.out.println("-------");
            ((DirectBuffer)byteBuffer).cleaner().clean();
        }
    }

    public static void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
