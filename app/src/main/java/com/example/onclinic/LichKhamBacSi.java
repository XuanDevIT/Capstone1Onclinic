package com.example.onclinic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.LichKhamBacSiAdapter;
import com.example.local_data.DataLocalManager;
import com.example.local_data.MyApplication;
import com.example.model.LichKham;
import com.example.model.LichSu;
import com.example.model.NguoiDung;
import com.example.helper.NgayGio;
import com.example.helper.NoteFireBase;
import com.example.model.PhongKham;
import com.example.model.Room;
import com.example.model.ThongBao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LichKhamBacSi extends AppCompatActivity {

    private RecyclerView rcvLichKham;
    private LichKhamBacSiAdapter lichKhamBacSiAdapter;
    private List<LichKham> listLichKham;

    String idPhongKham;
    PhongKham phongKham;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_kham_bac_si);
        idPhongKham = DataLocalManager.getIDPhongKham();
        phongKham = DataLocalManager.getPhongKham();
        addControls();
        layDanhSachLichKhamTuFireBase();
    }

    private void layDanhSachLichKhamTuFireBase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
        DatabaseReference refSuatKham = myRef.child(idPhongKham).child(NoteFireBase.LICHKHAM);
        refSuatKham.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLichKham.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    LichKham lichKham = data.getValue(LichKham.class);
                    try {
                        Date ngayFirebase = NgayGio.ConvertStringToDate(lichKham.getNgayKham());
                        Date ngayHienTai = NgayGio.GetDateCurrent();
                        //so sánh ngày trong lịch và hiện tại để thêm vào lịch khám và phải ở trạng thái chưa khám
                        if(ngayFirebase.getTime() >= ngayHienTai.getTime() && lichKham.getTrangThai() <= LichKham.DatLich)
                        {
                            listLichKham.add(lichKham);
                            Collections.sort(listLichKham, LichKham.LichKhamDateAsendingComparator);//sắp xếp ngày tăng dần
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                lichKhamBacSiAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LichKhamBacSi.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(LichKham lichKham : listLichKham)
        {
            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child("Room");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data : snapshot.getChildren())
                    {
                        Room room = data.getValue(Room.class);
                        if(room.getLichKham().getIdLichKham().equals(lichKham.getIdLichKham()))
                            myRef.child(lichKham.getIdLichKham()).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(lichKhamBacSiAdapter != null)
            lichKhamBacSiAdapter.release();
    }

    private void xuLyBatDau(LichKham lichKham, NguoiDung nguoiDung) {

        try {
            Date ngayHienTai = NgayGio.GetDateCurrent();
            Date ngayLichKham = NgayGio.ConvertStringToDate(lichKham.getNgayKham());
            if(ngayLichKham.getTime() == ngayHienTai.getTime())
            {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(lichKham.getIdLichKham().toString())
                        .setWelcomePageEnabled(false).build();
                JitsiMeetActivity.launch(LichKhamBacSi.this,options);

                //lưu thông báo
                DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
                DatabaseReference refThongBaoBS = myRef.child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).child(lichKham.getIdBenhNhan()).child(NoteFireBase.THONGBAO);
                String idThongBao = refThongBaoBS.push().getKey();
                String ngayThongBao = NgayGio.GetDateCurrentString();
                String gioThongBao = NgayGio.GetTimeCurrentString();
                ThongBao thongBao = new ThongBao(idThongBao,"Phòng khám "+phongKham.getTenPhongKham(),
                        "Buổi khám vào "+lichKham.getGioKham()+" ngày "+lichKham.getNgayKham()+" đã bắt đầu",
                        ngayThongBao,gioThongBao, ThongBao.ThongBaoLichKham);
                refThongBaoBS.child(idThongBao).setValue(thongBao);

                Room room = new Room(lichKham,thongBao);
                myRef.child("Room").child(lichKham.getIdLichKham()).setValue(room);
            }
            else{
                long soNgay = (ngayLichKham.getTime() - ngayHienTai.getTime())/86400000;
                Toast.makeText(LichKhamBacSi.this,"Còn "+soNgay+" ngày nữa mới đến ngày khám",Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void xuLyHoanThanh(LichKham lichKham) {
        new AlertDialog.Builder(LichKhamBacSi.this)
                .setTitle("Thông báo").setMessage("Bạn đã hoàn thành buổi khám?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        luuLichSu(lichKham);
                    }
                })
                .setNegativeButton("Chưa", null)
                .show();
    }

    private void luuLichSu(LichKham lichKham) {
        //lưu lịch sử
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        String key = myRef.child(NoteFireBase.LICHSU).push().getKey();
        LichSu lichSu = new LichSu(phongKham, lichKham);
        //cập nhật lại trạng thái của lịch khám là khám xong
        lichKham.setTrangThai(LichKham.KhamXong);
        DatabaseReference refLichkham = myRef.child(NoteFireBase.PHONGKHAM).child(phongKham.getIdPhongKham()).child(NoteFireBase.LICHKHAM).child(lichKham.getIdLichKham());
        refLichkham.setValue(lichKham);
        //đưa dữ liệu lịch sử lên firebase
        lichSu.setIdLichSu(key);
        myRef.child(NoteFireBase.LICHSU).child(key).setValue(lichSu);
    }

    private void addControls() {
        URL server;
        try{
            server =new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(server)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        rcvLichKham = findViewById(R.id.rcvLichKhamBS);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LichKhamBacSi.this);
        rcvLichKham.setLayoutManager(layoutManager);

        listLichKham = new ArrayList<>();
        lichKhamBacSiAdapter = new LichKhamBacSiAdapter(listLichKham, LichKhamBacSi.this, new LichKhamBacSiAdapter.ILichKhamBacSiAdapter() {
            @Override
            public void clickBatDau(LichKham lichKham, NguoiDung nguoiDung) {
                xuLyBatDau(lichKham, nguoiDung);
            }

            @Override
            public void clickHoanThanh(LichKham lichKham) {
                xuLyHoanThanh(lichKham);
            }
        });

        rcvLichKham.setAdapter(lichKhamBacSiAdapter);
    }
}
