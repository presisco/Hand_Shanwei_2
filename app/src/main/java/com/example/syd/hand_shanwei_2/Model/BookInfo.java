package com.example.syd.hand_shanwei_2.Model;

/**
 * Created by syd on 2015/11/17.
 */
public class BookInfo {
    public String marcno;
    public String name;
    public String code;
    public String detail;
    public String layer;
    public Integer total;
    public Integer rest;

    public BookInfo(String marcno,String name, String code, String detail,String layer,Integer total,Integer rest) {
        this.marcno=marcno;
        this.name = name;
        this.code = code;
        this.detail = detail;
        this.layer = layer;
        this.total=total;
        this.rest=rest;
    }

    public BookInfo()
    {

    }
}
