package com.example.model;

import java.io.Serializable;

public class PhongKham implements Serializable {
    private String idPhongKham;
    private String tenPhongKham;
    private String chuyenKhoa;
    private String diaChi;
    private String moTa;
    private String hinhAnh;
    private String hinhThucKham;
    private String idBacSi;

    public PhongKham() {
    }

    public PhongKham(String tenPhongKham, String chuyenKhoa, String diaChi, String moTa, String hinhAnh, String hinhThucKham) {
        this.tenPhongKham = tenPhongKham;
        this.chuyenKhoa = chuyenKhoa;
        this.diaChi = diaChi;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.hinhThucKham = hinhThucKham;
    }

    public String getIdPhongKham() {
        return idPhongKham;
    }

    public void setIdPhongKham(String idPhongKham) {
        this.idPhongKham = idPhongKham;
    }

    public String getIdBacSi() {
        return idBacSi;
    }

    public void setIdBacSi(String idBacSi) {
        this.idBacSi = idBacSi;
    }

    public String getTenPhongKham() {
        return tenPhongKham;
    }

    public void setTenPhongKham(String tenPhongKham) {
        this.tenPhongKham = tenPhongKham;
    }

    public String getChuyenKhoa() {
        return chuyenKhoa;
    }

    public void setChuyenKhoa(String chuyenKhoa) {
        this.chuyenKhoa = chuyenKhoa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getHinhThucKham() {
        return hinhThucKham;
    }

    public void setHinhThucKham(String hinhThucKham) {
        this.hinhThucKham = hinhThucKham;
    }
}
