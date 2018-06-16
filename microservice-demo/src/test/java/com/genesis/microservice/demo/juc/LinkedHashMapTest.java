package com.genesis.microservice.demo.juc;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Aizhanglin on 2018/3/12.
 */
public class LinkedHashMapTest {
    @Test
    public void foo(){
        Map<String,Integer> linkedHashMap=new LinkedHashMap();
        linkedHashMap.put("z",3);
        linkedHashMap.put("y",1);
        linkedHashMap.put("x",2);
        System.out.println(linkedHashMap);
        linkedHashMap.put("z",4);
        Iterator<Map.Entry<String, Integer>> iterator = linkedHashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> next = iterator.next();
            System.out.println(next.getKey()+":"+next.getValue());
        }
        System.out.println("size:"+linkedHashMap.size());
    }
}
