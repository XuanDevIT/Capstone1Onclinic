package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.helper.NgayGio;
import com.example.helper.NoteFireBase;
import com.example.local_data.DataLocalManager;
import com.example.helper.ActivityState;
import com.example.local_data.MyApplication;
import com.example.model.LichKham;
import com.example.model.LichSu;
import com.example.model.Room;
import com.example.model.ThongBao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TrangChuBenhNhan extends MyBaseActivity{

    LinearLayout btnDatPhong, btnDoNhipTim, btnLichSuKham, btnKhamOnline, btnLienHe, btnDSPhongKham;

    private static final int NOTIFICATION_ID2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_trang_chu_benh_nhan,null,false);
        mDrawerLayout.addView(view,0);
        //setContentView(R.layout.activity_trang_chu_benh_nhan);
        AnhXa();
        layThongBao();
        XyLy();
    }

    private void layThongBao() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        myRef.child("Room").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Room room = snapshot.getValue(Room.class);
                if(idNguoiDung.equals(room.getLichKham().getIdBenhNhan()))
                {
                    guiThongBao(room.getThongBao().getTieuDe(),room.getThongBao().getNoiDung());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Room room = snapshot.getValue(Room.class);
                if(idNguoiDung.equals(room.getLichKham().getIdBenhNhan()))
                {
                    guiThongBao(room.getThongBao().getTieuDe(),room.getThongBao().getNoiDung());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void XyLy() {
        btnDatPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChuBenhNhan.this, DatPhong.class);
                startActivity(intent);
            }
        });

        btnLichSuKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChuBenhNhan.this, LichSuKhamBenhNhan.class);
                startActivity(intent);
            }
        });

        btnLienHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrangChuBenhNhan.this, LienHe.class);
                DataLocalManager.setActivityNumber(ActivityState.ACTIVITY_LIENHE);
                startActivity(intent);
            }
        });

        btnKhamOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBenhNhan.this,LichKhamBenhNhan.class);
                startActivity(intent);
            }
        });

        btnDSPhongKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBenhNhan.this,DanhSachPhongKham.class);
                startActivity(intent);
            }
        });
    }

    private void guiThongBao(String tieuDe, String noiDung)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_app);
        Uri sound = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sound_notification_custom);

        Intent resultIntent = new Intent(this, LichKhamBenhNhan.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(NOTIFICATION_ID2, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID_2)
                .setContentTitle(tieuDe)
                .setContentText(noiDung)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(sound)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(noiDung))
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.small_icon_notify)
                .build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID2, notification);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(TrangChuBenhNhan.this)
                .setTitle("Thông báo").setMessage("Bạn muốn đăng xuất?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TrangChuBenhNhan.this,LoiChao.class);
                        startActivity(intent);
                        finishAffinity();
                        killActivity();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void killActivity() {
        finish();
        return;
    }

    private void AnhXa() {
        btnDatPhong = findViewById(R.id.btnDatPhongBN);
        btnDoNhipTim = findViewById(R.id.btnDoNhipTimBN);
        btnLichSuKham = findViewById(R.id.btnLichSuKhamBN);
        btnKhamOnline = findViewById(R.id.btnKhamOnlineBN);
        btnLienHe = findViewById(R.id.btnLienHeBN);
        btnDSPhongKham = findViewById(R.id.btnDanhSachPhongKhamBN);
    }
}