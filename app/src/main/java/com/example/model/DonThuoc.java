package com.example.model;

import java.io.Serializable;

public class DonThuoc implements Serializable {
    int idDonThuoc;
    String tenThuoc, donViTinh, lieuDung;
    int soLuong;
    int donGia;

    public DonThuoc() {
    }

    public DonThuoc(String tenThuoc, String donViTinh, String lieuDung, int soLuong, int donGia) {
        this.tenThuoc = tenThuoc;
        this.donViTinh = donViTinh;
        this.lieuDung = lieuDung;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public int getIdDonThuoc() {
        return idDonThuoc;
    }

    public void setIdDonThuoc(int idDonThuoc) {
        this.idDonThuoc = idDonThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getLieuDung() {
        return lieuDung;
    }

    public void setLieuDung(String lieuDung) {
        this.lieuDung = lieuDung;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }
}
