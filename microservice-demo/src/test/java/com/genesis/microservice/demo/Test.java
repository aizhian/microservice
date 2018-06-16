package com.genesis.microservice.demo;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aizhanglin on 2018/5/28.
 */
public class Test {
    @Data
    static class Foo {
        private Integer id;
    }

    @org.junit.Test
    public void foo() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Foo foo = new Foo();
        foo.setId(1);
        Map<String, String> map = foo2Map(foo);
        System.out.println(map);
        Foo foo1 = mapToFoo(map);
        System.out.println(foo1.getId());

    }

    private Map<String, String> foo2Map(Foo foo) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        for (Field field : foo.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String value = field.get(foo) + "";
            map.put(field.getName(), value);
        }
        return map;
    }

    private Foo mapToFoo(Map<String, String> map) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Foo foo = new Foo();
        for (Field field : Foo.class.getDeclaredFields()) {
            Class<?> type = field.getType();
            Method valueOf = type.getDeclaredMethod("valueOf", String.class);
            Object value = valueOf.invoke(field, map.get(field.getName()));
            field.setAccessible(true);
            field.set(foo, value);
        }
        return foo;
    }
}
