package com.example.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ThongBao implements Serializable {
    String idThongBao, tieuDe, noiDung;
    String ngayThongBao, gioThongBao;
    int loaiThongBao;

    public final static int ThongBaoLichKham = 0;
    public final static int ThongBaoDanhGia = 1;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    public ThongBao() {
    }

    public ThongBao(String idThongBao, String tieuDe, String noiDung, String ngayThongBao, String gioThongBao, int loaiThongBao) {
        this.idThongBao = idThongBao;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayThongBao = ngayThongBao;
        this.gioThongBao = gioThongBao;
        this.loaiThongBao = loaiThongBao;
    }

    public static Comparator<ThongBao> ThongBaoDateDescendingComparator = new Comparator<ThongBao>() {
        @Override
        public int compare(ThongBao t1, ThongBao t2) {
            try {
                Date date1 = sdf1.parse(t1.getNgayThongBao());
                Date date2 = sdf1.parse(t2.getNgayThongBao());
                if(date1.compareTo(date2) == 0)
                {
                    try {
                        Date time1 = sdf2.parse(t1.getGioThongBao());
                        Date time2 = sdf2.parse(t2.getGioThongBao());
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

    public String getIdThongBao() {
        return idThongBao;
    }

    public void setIdThongBao(String idThongBao) {
        this.idThongBao = idThongBao;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNgayThongBao() {
        return ngayThongBao;
    }

    public void setNgayThongBao(String ngayThongBao) {
        this.ngayThongBao = ngayThongBao;
    }

    public String getGioThongBao() {
        return gioThongBao;
    }

    public void setGioThongBao(String gioThongBao) {
        this.gioThongBao = gioThongBao;
    }

    public int getLoaiThongBao() {
        return loaiThongBao;
    }

    public void setLoaiThongBao(int loaiThongBao) {
        this.loaiThongBao = loaiThongBao;
    }
}
