package com.example.syd.hand_shanwei_2.Model;

/**
 * Created by presisco on 2015/11/16.
 */
public class FloorInfo {
    /**
     * 楼层名
     */
    public String layer;
    /**
     * 总座位
     */
    public int total;
    /**
     * 剩余座位
     */
    public int rest;

    public FloorInfo(String layer, int total, int rest) {
        this.layer = layer;
        this.total = total;
        this.rest = rest;
    }

    public FloorInfo() {
    }
}


