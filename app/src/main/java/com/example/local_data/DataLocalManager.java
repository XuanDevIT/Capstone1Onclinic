package com.example.local_data;

import android.content.Context;

import com.example.model.DonThuoc;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.google.gson.Gson;

//lớp này quản lí dữ liệu local
public class DataLocalManager {
    private static final String ID_NGUOI_DUNG = "ID_NGUOI_DUNG";
    private static final String OBJECT_NGUOI_DUNG = "OBJECT_NGUOI_DUNG";
    private static final String ID_PHONG_KHAM = "ID_PHONG_KHAM";
    private static final String OBJECT_PHONG_KHAM = "OBJECT_PHONG_KHAM";
    private static final String ID_LICH_KHAM = "ID_LICH_KHAM";
    private static final String ACTIVITY_NUMBER = "ACTIVITY_NUMBER";
    private static final String ROLE_NUMBER = "ROLE_NUMBER";
    private static final String OBJECT_DON_THUOC = "OBJECT_DON_THUOC";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context)
    {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance()
    {
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static void setIDNguoiDung(String idNguoiDung)
    {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(ID_NGUOI_DUNG,idNguoiDung);
    }
    public static String getIDNguoiDung()
    {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(ID_NGUOI_DUNG);
    }
    public static void setNguoiDung(NguoiDung nguoiDung)
    {
        Gson gson = new Gson();//gson để chuyển object sang json
        String strNguoiDung = gson.toJson(nguoiDung);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(OBJECT_NGUOI_DUNG,strNguoiDung);
    }
    public static NguoiDung getNguoiDung()
    {
        String jsonNguoiDung = DataLocalManager.getInstance().mySharedPreferences.getStringValue(OBJECT_NGUOI_DUNG);
        Gson gson = new Gson();
        NguoiDung nguoiDung = gson.fromJson(jsonNguoiDung,NguoiDung.class);
        return nguoiDung;
    }
    public static void setIDPhongKham(String idPhongKham)
    {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(ID_PHONG_KHAM,idPhongKham);
    }
    public static String getIDPhongKham()
    {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(ID_PHONG_KHAM);
    }
    public static void setPhongKham(PhongKham phongKham)
    {
        Gson gson = new Gson();//gson để chuyển object sang json
        String strPhongKham = gson.toJson(phongKham);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(OBJECT_PHONG_KHAM,strPhongKham);
    }
    public static PhongKham getPhongKham()
    {
        String jsonPhongKham = DataLocalManager.getInstance().mySharedPreferences.getStringValue(OBJECT_PHONG_KHAM);
        Gson gson = new Gson();
        PhongKham phongKham = gson.fromJson(jsonPhongKham,PhongKham.class);
        return phongKham;
    }
    public static void setIDLichKham(String idLichKham)
    {
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(ID_LICH_KHAM,idLichKham);
    }
    public static String getIDLichKham()
    {
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(ID_LICH_KHAM);
    }
    public static void setActivityNumber(int activityNumber)
    {
        DataLocalManager.getInstance().mySharedPreferences.putIntegerValue(ACTIVITY_NUMBER,activityNumber);
    }
    public static Integer getActivityNumber()
    {
        return DataLocalManager.getInstance().mySharedPreferences.getIntegerValue(ACTIVITY_NUMBER);
    }
    public static void setRole(int role)
    {
        DataLocalManager.getInstance().mySharedPreferences.putIntegerValue(ROLE_NUMBER,role);
    }
    public static Integer getRole()
    {
        return DataLocalManager.getInstance().mySharedPreferences.getIntegerValue(ROLE_NUMBER);
    }
    public static void setDonThuoc(DonThuoc donThuoc)
    {
        Gson gson = new Gson();//gson để chuyển object sang json
        String strDonThuoc = gson.toJson(donThuoc);
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(OBJECT_DON_THUOC,strDonThuoc);
    }
    public static DonThuoc getDonThuoc()
    {
        String jsonDonThuoc = DataLocalManager.getInstance().mySharedPreferences.getStringValue(OBJECT_DON_THUOC);
        Gson gson = new Gson();
        DonThuoc donThuoc = gson.fromJson(jsonDonThuoc,DonThuoc.class);
        return donThuoc;
    }
}

