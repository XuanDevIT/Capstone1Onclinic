package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.example.adapter.LichKhamBenhNhanAdapter;
import com.example.helper.NgayGio;
import com.example.local_data.DataLocalManager;
import com.example.local_data.MyApplication;
import com.example.model.LichKham;
import com.example.model.PhongKham;
import com.example.helper.NoteFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LichKhamBenhNhan extends AppCompatActivity {

    private RecyclerView rcvLichKham;
    private LichKhamBenhNhanAdapter lichKhamBenhNhanAdapter;
    private List<LichKham> listLichKham;
    private List<PhongKham> listPhongKham;//toàn bộ phòng khám trên firebase
    private List<PhongKham> listPhongKhamCoLichKham;//tìm được phòng khám có lịch khám bệnh nhân này thì thêm vào
    String idNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_kham_benh_nhan);
        idNguoiDung = DataLocalManager.getIDNguoiDung();
        addControls();
        layDanhSachLichKhamTuFireBase();
    }

    private void layDanhSachLichKhamTuFireBase() {
        docDanhSachPhongKhamTuFirebase(new ILichKhamBenhNhan() {
            @Override
            public void layDanhSachPhongKham(List<PhongKham> list) {
                for(PhongKham phong : list)
                {
                    String idPhongKham = phong.getIdPhongKham();
                    DatabaseReference refLichKham = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).
                            getReference(NoteFireBase.PHONGKHAM).child(idPhongKham).child(NoteFireBase.LICHKHAM);
                    refLichKham.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichKham lich = snapshot.getValue(LichKham.class);
                            try {
                                Date ngayFirebase = NgayGio.ConvertStringToDate(lich.getNgayKham());
                                Date ngayHienTai = NgayGio.GetDateCurrent();
                                if(idNguoiDung.equals(lich.getIdBenhNhan()) && lich.getTrangThai() == LichKham.DatLich && ngayFirebase.getTime()>=ngayHienTai.getTime())
                                {
                                    listLichKham.add(lich);
                                    listPhongKhamCoLichKham.add(phong);
                                    Collections.sort(listLichKham, LichKham.LichKhamDateAsendingComparator);//sắp xếp ngày tăng dần
                                }
                                } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            lichKhamBenhNhanAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            LichKham lich = snapshot.getValue(LichKham.class);
                            if(lich == null || listLichKham == null || listLichKham.isEmpty()) return;
                            for(int i = 0;i<listLichKham.size();i++)
                            {
                                try {
                                    Date ngayFirebase = NgayGio.ConvertStringToDate(lich.getNgayKham());
                                    Date ngayHienTai = NgayGio.GetDateCurrent();
                                    if((lich.getIdLichKham()).equals(listLichKham.get(i).getIdLichKham()) && ngayFirebase.getTime()>=ngayHienTai.getTime()) {
                                        listLichKham.set(i, lich);
                                        Collections.sort(listLichKham, LichKham.LichKhamDateAsendingComparator);//sắp xếp ngày tăng dần
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            lichKhamBenhNhanAdapter.notifyDataSetChanged();
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
            }
        });
    }

    private void docDanhSachPhongKhamTuFirebase(ILichKhamBenhNhan iLichKhamBenhNhan)
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference(NoteFireBase.PHONGKHAM);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                listPhongKham.clear();
                PhongKham phongKham = snapshot.getValue(PhongKham.class);
                if(phongKham!=null)
                {
                    listPhongKham.add(phongKham);
                }
                iLichKhamBenhNhan.layDanhSachPhongKham(listPhongKham);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    public interface ILichKhamBenhNhan{
        void layDanhSachPhongKham(List<PhongKham> list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(lichKhamBenhNhanAdapter != null)
            lichKhamBenhNhanAdapter.release();
    }

    private void xuLyThamGia(LichKham lichKham, PhongKham phongKham) {
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom(lichKham.getIdLichKham().toString())
                .setUserInfo(new JitsiMeetUserInfo())
                .setWelcomePageEnabled(false).build();
        JitsiMeetActivity.launch(LichKhamBenhNhan.this,options);

        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        myRef.child("Room").child(lichKham.getIdLichKham()).removeValue();
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

        rcvLichKham = findViewById(R.id.rcvLichKhamBN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LichKhamBenhNhan.this);
        rcvLichKham.setLayoutManager(layoutManager);
        listLichKham = new ArrayList<>();
        listPhongKham = new ArrayList<>();
        listPhongKhamCoLichKham = new ArrayList<>();

        lichKhamBenhNhanAdapter = new LichKhamBenhNhanAdapter(listLichKham, listPhongKhamCoLichKham, LichKhamBenhNhan.this, new LichKhamBenhNhanAdapter.ILichKhamBenhNhanAdapter() {
            @Override
            public void clickThamGia(LichKham lichKham, PhongKham phongKham) {
                xuLyThamGia(lichKham, phongKham);
            }
        });

        rcvLichKham.setAdapter(lichKhamBenhNhanAdapter);
    }
}