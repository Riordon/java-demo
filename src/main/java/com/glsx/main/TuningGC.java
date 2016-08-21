package com.glsx.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaolong on 2016/8/18.
 * 本文件用于常见5中调优GC测试用例
 */
public class TuningGC {
    private static final Logger LOG = LoggerFactory.getLogger(TuningGC.class);

    public static void main(String[] args) throws IOException {
        LOG.info("The test is begining...");
//        test01();
//        test02();
//        test03();
//        test04();
        test05();
        LOG.info("The test is end.");
    }

    /**
     * 使用原生
     */
    private static void test05() {
    }

    /**
     * 小心字符串拼接
     */
    private static void test04() {
        int size = 10000;
        long beginTime01 = System.currentTimeMillis();
        String str01 = new String();

        for (int i = 0; i < size; i++) {
            str01 += "test" + i;
        }
        long costTime01 = System.currentTimeMillis() - beginTime01;

        long beginTime02 = System.currentTimeMillis();
        StringBuffer str02 = new StringBuffer();

        for (int i = 0; i < size; i++) {
            str02.append("test" + i);
            if (str02.length() > 1000) {
                str02 = new StringBuffer();
            }
        }
        long costTime02 = System.currentTimeMillis() - beginTime02;

        LOG.info("使用String拼接：costTime=" + costTime01);
        LOG.info("使用StringBuffer拼接：costTime=" + costTime02);
    }

    /**
     * 优先使用不可变量
     */
    private static void test03() {
    }

    /**
     * 直接处理数据流，有时数据流过大时，会导致OutOfMemory异常
     */
    private static void test02() throws IOException {
        File file = new File("test.log");
        Writer writer = new FileWriter(file);
        StringBuffer buff = new StringBuffer();

        int i = 0;
        while (true) {
            i++;
            buff.append("test" + i);
            buff.append("\n");
            if (buff.length() > 10) {
                writer.write(buff.toString());
                writer.flush();
                buff = new StringBuffer();
            }
            if (i == 10) break;
        }
        writer.flush();
        writer.close();
    }

    /**
     * 通过预测集合容量，有时可以减轻集群频繁重分配，降低GC开销
     */
    private static void test01() {
        int size = 10000000;
        long beginTime01 = System.currentTimeMillis();
        List list01 = new ArrayList();

        for (int i = 0; i < size; i++) {
            list01.add(i);
        }
        long costTime01 = System.currentTimeMillis() - beginTime01;

        long beginTime02 = System.currentTimeMillis();
        List list02 = new ArrayList(size);

        for (int i = 0; i < size; i++) {
            list02.add(i);
        }
        long costTime02 = System.currentTimeMillis() - beginTime02;

        LOG.info("未预分配时：costTime=" + costTime01);
        LOG.info("有预分配时：costTime=" + costTime02);
    }
}
