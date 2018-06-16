package com.genesis.microservice.demo.map;

import org.junit.Test;

import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Aizhanglin on 2018/4/10.
 *
 */
public class TreemapTest {
    @Test
    public void foo(){
        TreeMap<Integer, String> tm = new TreeMap<Integer, String>();
        tm.put(1, "one");
        tm.put(0, "zero");
        tm.put(3, "three");
        tm.put(2, "two");
        // System.out.println(tm);// {0=zero, 1=one, 2=two, 3=three}
        // System.out.println(tm.keySet());// [0, 1, 2, 3]
        // System.out.println(tm.values());// [zero, one, two, three]

        Set<Integer> keys = tm.keySet();// set本身就是一个集合
        for (Integer key : keys) {
            System.out.print("学号：" + key + ",姓名：" + tm.get(key) + "\t");
        }
    }
}
