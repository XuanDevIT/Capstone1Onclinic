package com.example.model;

import java.io.Serializable;
import java.util.List;

public class Benh implements Serializable {
    private String trieuChung;
    private String benh;
    private String ghiChu;

    public Benh() {
    }

    public Benh(String trieuChung, String benh, String ghiChu) {
        this.trieuChung = trieuChung;
        this.benh = benh;
        this.ghiChu = ghiChu;
    }

    public String getTrieuChung() {
        return trieuChung;
    }

    public void setTrieuChung(String trieuChung) {
        this.trieuChung = trieuChung;
    }

    public String getBenh() {
        return benh;
    }

    public void setBenh(String benh) {
        this.benh = benh;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
