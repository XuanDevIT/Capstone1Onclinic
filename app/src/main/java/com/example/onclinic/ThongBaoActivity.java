package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.adapter.LichSuBacSiAdapter;
import com.example.adapter.ThongBaoAdapter;
import com.example.helper.NgayGio;
import com.example.helper.NoteFireBase;
import com.example.local_data.DataLocalManager;
import com.example.model.DanhGia;
import com.example.model.LichKham;
import com.example.model.LichSu;
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

public class ThongBaoActivity extends AppCompatActivity {

    RecyclerView rcvThongBao;
    ThongBaoAdapter thongBaoAdapter;
    List<ThongBao> thongBaoList;

    String idNguoiDung;
    int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);
        idNguoiDung = DataLocalManager.getIDNguoiDung();
        role = DataLocalManager.getRole();
        addControls();
        layDanhSachThongBao();
    }

    private void layDanhSachThongBao() {
        if(role==0)
        {
            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference(NoteFireBase.NGUOIDUNG)
                    .child(NoteFireBase.BENHNHAN).child(idNguoiDung).child(NoteFireBase.THONGBAO);
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ThongBao thongBao = snapshot.getValue(ThongBao.class);
                    thongBaoList.add(thongBao);
                    Collections.sort(thongBaoList,ThongBao.ThongBaoDateDescendingComparator);
                    thongBaoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ThongBao thongBao = snapshot.getValue(ThongBao.class);
                    if(thongBao == null || thongBaoList == null || thongBaoList.isEmpty()) return;
                    for(int i = 0;i<thongBaoList.size();i++)
                    {
                        if(thongBao.getIdThongBao().equals(thongBaoList.get(i).getIdThongBao()))
                        {
                            thongBaoList.set(i,thongBao);
                            Collections.sort(thongBaoList,ThongBao.ThongBaoDateDescendingComparator);
                        }
                    }
                    thongBaoAdapter.notifyDataSetChanged();
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
        else if(role==1)
        {
            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference(NoteFireBase.NGUOIDUNG)
                    .child(NoteFireBase.BACSI).child(idNguoiDung).child(NoteFireBase.THONGBAO);
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ThongBao thongBao = snapshot.getValue(ThongBao.class);
                    thongBaoList.add(thongBao);
                    Collections.sort(thongBaoList,ThongBao.ThongBaoDateDescendingComparator);
                    thongBaoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    ThongBao thongBao = snapshot.getValue(ThongBao.class);
                    if(thongBao == null || thongBaoList == null || thongBaoList.isEmpty()) return;
                    for(int i = 0;i<thongBaoList.size();i++)
                    {
                        if(thongBao.getIdThongBao().equals(thongBaoList.get(i).getIdThongBao()))
                        {
                            thongBaoList.set(i,thongBao);
                            Collections.sort(thongBaoList,ThongBao.ThongBaoDateDescendingComparator);
                        }
                    }
                    thongBaoAdapter.notifyDataSetChanged();
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


    private void xuLyThongBao(ThongBao thongBao) {
        if(thongBao.getLoaiThongBao() == ThongBao.ThongBaoLichKham)
        {
            if(role == 0){
                Intent intent = new Intent(ThongBaoActivity.this,LichKhamBenhNhan.class);
                startActivity(intent);
            }
            else if(role == 1)
            {
                Intent intent = new Intent(ThongBaoActivity.this,LichKhamBacSi.class);
                startActivity(intent);
            }
        }
        else if(thongBao.getLoaiThongBao() == ThongBao.ThongBaoDanhGia)
        {
            Intent intent = new Intent(ThongBaoActivity.this, ViewDanhGiaBS.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(thongBaoAdapter != null)
            thongBaoAdapter.release();
    }

    private void addControls() {
        rcvThongBao = findViewById(R.id.rcvThongBao);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ThongBaoActivity.this);
        rcvThongBao.setLayoutManager(layoutManager);

        thongBaoList = new ArrayList<>();
        thongBaoAdapter = new ThongBaoAdapter(thongBaoList, ThongBaoActivity.this, new ThongBaoAdapter.IThongBao() {
            @Override
            public Date ngayHienTai() {
                Date ngay = null;
                try {
                    ngay = NgayGio.GetDateCurrent();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return ngay;
            }

            @Override
            public Date gioHienTai() {
                Date gio = null;
                try {
                    gio = NgayGio.GetTimeCurrent();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return gio;
            }

            @Override
            public Date ngayThongBao(ThongBao thongBao) {
                Date ngay = null;
                try {
                    ngay = NgayGio.ConvertStringToDate(thongBao.getNgayThongBao());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return ngay;
            }

            @Override
            public Date gioThongBao(ThongBao thongBao) {
                Date gio = null;
                try {
                    gio = NgayGio.ConvertStringToTime(thongBao.getGioThongBao());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return gio;
            }

            @Override
            public void clickLayoutThongBao(ThongBao thongBao) {
                xuLyThongBao(thongBao);
            }
        });
        rcvThongBao.setAdapter(thongBaoAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvThongBao.addItemDecoration(itemDecoration);
    }
}