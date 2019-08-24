package com.andy.springboot.entity;

import java.io.Serializable;

/**
 * @author lianhong
 * @description order
 * @date 2019/8/22 0022下午 10:34
 */
public class Order implements Serializable {
    private String id;
    private String name;

    public Order() {
    }

    public Order(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // TODO: 2019/8/24 0024 绝对不能重写toString方法，否则监听消息的时候无法序列化对象，报错
    /*@Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }*/
}
