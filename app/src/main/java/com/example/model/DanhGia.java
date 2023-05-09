package com.example.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DanhGia implements Serializable {
    private String idNguoiDungDG;
    private String nhanXet;
    private Float rating;

    public DanhGia() {
    }

    public DanhGia(String idNguoiDungDG, Float rating, String nhanXet){
        this.idNguoiDungDG = idNguoiDungDG;
        this.nhanXet = nhanXet;
        this.rating = rating;
    }

    public String getIdNguoiDungDG() {
        return idNguoiDungDG;
    }

    public void setIdNguoiDungDG(String idNguoiDungDG) {
        this.idNguoiDungDG = idNguoiDungDG;
    }

    public String getNhanXet() {
        return nhanXet;
    }

    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }

    public Float getRating() { return rating; }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
