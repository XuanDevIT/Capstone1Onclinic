package com.example.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import com.example.local_data.MyApplication;
import com.example.onclinic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NgayGio {
    public static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    public static Date ConvertStringToDate(String ngay) throws ParseException {
        return  sdf1.parse(ngay);
    }
    public static Date ConvertStringToTime(String gio) throws ParseException{
        return sdf2.parse(gio);
    }
    public static String ConvertDateToString(Date ngay)
    {
        return sdf1.format(ngay);
    }
    public static String ConvertTimeToString(Date gio)
    {
        return sdf2.format(gio);
    }
    public static Date GetDateCurrent() throws ParseException
    {
        String today = sdf1.format(Calendar.getInstance().getTime());
        return sdf1.parse(today);
    }
    public static String GetDateCurrentString()
    {
        return sdf1.format(Calendar.getInstance().getTime());
    }
    public static Date GetTimeCurrent() throws ParseException
    {
        String time = sdf2.format(Calendar.getInstance().getTime());
        return sdf2.parse(time);
    }
    public static String GetTimeCurrentString()
    {
        return sdf2.format(Calendar.getInstance().getTime());
    }
}
