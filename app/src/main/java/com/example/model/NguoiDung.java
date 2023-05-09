package com.example.model;

import java.io.Serializable;
import java.util.Date;

public class NguoiDung implements Serializable{

    private String userID;
    private String email_sdt;
    private String matKhau;
    private String tenNguoiDung;
    private String ngaySinh;
    private String thanhpho,quan;
    private String hinhAnh;

    public NguoiDung() {
    }

    public NguoiDung(String email_sdt, String matKhau, String tenNguoiDung, String ngaySinh, String thanhpho, String quan) {
        this.email_sdt = email_sdt;
        this.matKhau = matKhau;
        this.tenNguoiDung = tenNguoiDung;
        this.ngaySinh = ngaySinh;
        this.thanhpho = thanhpho;
        this.quan = quan;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail_sdt() {
        return email_sdt;
    }

    public void setEmail_sdt(String email_sdt) {
        this.email_sdt = email_sdt;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getThanhpho() {
        return thanhpho;
    }

    public void setThanhpho(String thanhpho) {
        this.thanhpho = thanhpho;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
