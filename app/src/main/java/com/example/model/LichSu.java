package com.example.model;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class LichSu implements Serializable {
    private String idLichSu;
    private Benh benh;
    private PhongKham phongKham;
    private LichKham lichKham;
    private List<DonThuoc> donThuocList;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    public LichSu() {
    }

    public LichSu(PhongKham phongKham, LichKham lichKham) {
        this.phongKham = phongKham;
        this.lichKham = lichKham;
    }

    public static Comparator<LichSu> LichSuDateDescendingComparator = new Comparator<LichSu>() {
        @Override
        public int compare(LichSu l1, LichSu l2) {
            try {
                Date date1 = sdf1.parse(l1.getLichKham().getNgayKham());
                Date date2 = sdf1.parse(l2.getLichKham().getNgayKham());
                //kiểm tra ngày trong lịch có lớn hơn ngày hiện tại ko
                if(date1.compareTo(date2) == 0)//nếu 2 lịch cùng ngày thì so sánh giờ
                {
                    try {
                        Date time1 = sdf2.parse(l1.getLichKham().getGioKham());
                        Date time2 = sdf2.parse(l2.getLichKham().getGioKham());
                        return time2.compareTo(time1);
                    }
                    catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
                return date2.compareTo(date1);
            }
            catch (ParseException e) {
                e.printStackTrace();
                return 1;
            }
        }
    };

    public String getIdLichSu() {
        return idLichSu;
    }

    public void setIdLichSu(String idLichSu) {
        this.idLichSu = idLichSu;
    }

    public Benh getBenh() {
        return benh;
    }

    public void setBenh(Benh benh) {
        this.benh = benh;
    }

    public PhongKham getPhongKham() {
        return phongKham;
    }

    public void setPhongKham(PhongKham phongKham) {
        this.phongKham = phongKham;
    }

    public LichKham getLichKham() {
        return lichKham;
    }

    public void setLichKham(LichKham lichKham) {
        this.lichKham = lichKham;
    }

    public List<DonThuoc> getDonThuocList() {
        return donThuocList;
    }

    public void setDonThuocList(List<DonThuoc> donThuocList) {
        this.donThuocList = donThuocList;
    }
}
