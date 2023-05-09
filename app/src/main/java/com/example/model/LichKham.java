package com.example.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class LichKham implements Serializable {
    private String ngayKham;//note chính
    private String gioKham;
    private String hinhThucKham;
    private String idBenhNhan;//người đã đặt lịch
    private String idLichKham;
    private int trangThai;//1-đã đặt lịch, 2-đã khám xong, chưa nhập đơn thuốc, 3-đã khám xong và đã nhập đơn thuốc
    private Benh benh;

    public final static int ChuaDatLich = 0;
    public final static int DatLich = 1;
    public final static int KhamXong = 2;
    public final static int NhapDonThuoc = 3;
    public final static String Online = "Online";
    public final static String TrucTiep = "Trực tiếp";

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    public LichKham() {
    }

    public LichKham(String ngayKham, String gioKham) {
        this.ngayKham = ngayKham;
        this.gioKham = gioKham;
    }

    public static Comparator<LichKham> LichKhamDateAsendingComparator = new Comparator<LichKham>() {
        @Override
        public int compare(LichKham l1, LichKham l2) {
            try {
                Date date1 = sdf1.parse(l1.getNgayKham());
                Date date2 = sdf1.parse(l2.getNgayKham());
                //kiểm tra ngày trong lịch có lớn hơn ngày hiện tại ko
                if(date1.compareTo(date2) == 0)//nếu 2 lịch cùng ngày thì so sánh giờ
                {
                    try {
                        Date time1 = sdf2.parse(l1.getGioKham());
                        Date time2 = sdf2.parse(l2.getGioKham());
                        return time1.compareTo(time2);
                    }
                    catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
                return date1.compareTo(date2);
            }
            catch (ParseException e) {
                e.printStackTrace();
                return 1;
            }
        }
    };

    public static Comparator<String> StringDateAsendingComparator = new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            try {
                Date date1 = sdf1.parse(s1);
                Date date2 = sdf1.parse(s2);
                return date1.compareTo(date2);
            }
             catch (ParseException e) {
                e.printStackTrace();
                return 1;
            }
        }
    };

    public static Comparator<Date> TimeAsendingComparator = new Comparator<Date>() {
        @Override
        public int compare(Date d1, Date d2) {
            return d1.compareTo(d2);
        }
    };

    public String getNgayKham() {
        return ngayKham;
    }

    public void setNgayKham(String ngayKham) {
        this.ngayKham = ngayKham;
    }

    public String getGioKham() {
        return gioKham;
    }

    public void setGioKham(String gioKham) {
        this.gioKham = gioKham;
    }

    public String getHinhThucKham() {
        return hinhThucKham;
    }

    public void setHinhThucKham(String hinhThucKham) {
        this.hinhThucKham = hinhThucKham;
    }

    public String getIdBenhNhan() {
        return idBenhNhan;
    }

    public void setIdBenhNhan(String idBenhNhan) {
        this.idBenhNhan = idBenhNhan;
    }

    public String getIdLichKham() {
        return idLichKham;
    }

    public void setIdLichKham(String idLichKham) {
        this.idLichKham = idLichKham;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public Benh getBenh() {
        return benh;
    }

    public void setBenh(Benh benh) {
        this.benh = benh;
    }
}
