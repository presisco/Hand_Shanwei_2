package com.example.syd.hand_shanwei_2.Model;

/**
 * Created by syd on 2015/11/17.
 */
public class Book {
    String marcno;  //ͼ����
    String name;	//ͼ�����
    String code;	//�汾��
    String detail;	//��ϸ��Ϣ
    String layer;		//¥����Ϣ
    int total;		//�ܿ��
    int rest;		//ʣ����
    public Book(String name, String code, String detail,String layer) {
        super();
        this.name = name;
        this.code = code;
        this.detail = detail;
        this.layer = layer;
    }

    public Book() {
        super();
    }
    public String getMarcno() {
        return marcno;
    }

    public void setMarcno(String marcno) {
        this.marcno = marcno;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
