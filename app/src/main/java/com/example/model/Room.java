package com.example.model;

public class Room {
    private LichKham lichKham;
    private ThongBao thongBao;

    public Room() {
    }

    public Room(LichKham lichKham, ThongBao thongBao) {
        this.lichKham = lichKham;
        this.thongBao = thongBao;
    }

    public LichKham getLichKham() {
        return lichKham;
    }

    public void setLichKham(LichKham lichKham) {
        this.lichKham = lichKham;
    }

    public ThongBao getThongBao() {
        return thongBao;
    }

    public void setThongBao(ThongBao thongBao) {
        this.thongBao = thongBao;
    }
}
