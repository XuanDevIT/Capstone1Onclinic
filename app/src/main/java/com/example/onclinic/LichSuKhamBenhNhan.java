package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.adapter.LichSuBenhNhanAdapter;
import com.example.local_data.DataLocalManager;
import com.example.model.LichKham;
import com.example.model.LichSu;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LichSuKhamBenhNhan extends AppCompatActivity {

    RecyclerView rcvLichSu;
    LichSuBenhNhanAdapter lichSuAdapter;
    List<LichSu> listLichSu;

    String idNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_kham_benh_nhan);
        idNguoiDung = DataLocalManager.getIDNguoiDung();
        layDanhSachLichSuKham();
        addControls();
    }

    private void layDanhSachLichSuKham() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference(NoteFireBase.LICHSU);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLichSu.clear();
                for(DataSnapshot data: snapshot.getChildren())
                {
                    LichSu lichSu = data.getValue(LichSu.class);
                    if(idNguoiDung.equals(lichSu.getLichKham().getIdBenhNhan()) && lichSu.getLichKham().getTrangThai()>=LichKham.NhapDonThuoc)
                    {
                        listLichSu.add(lichSu);
                        Collections.sort(listLichSu,LichSu.LichSuDateDescendingComparator);
                    }
                }
                lichSuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LichSuKhamBenhNhan.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void xuLyChiTiet(LichSu lichSu) {
        Intent intent = new Intent(LichSuKhamBenhNhan.this, ChiTietLichSuBenhNhan.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("CHI_TIET_LICH_SU_BENH_NHAN",lichSu);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(lichSuAdapter!=null)
            lichSuAdapter.release();
    }

    private void addControls() {
        rcvLichSu = findViewById(R.id.rcvLichSuBN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LichSuKhamBenhNhan.this);
        rcvLichSu.setLayoutManager(layoutManager);

        listLichSu = new ArrayList<>();
        lichSuAdapter = new LichSuBenhNhanAdapter(listLichSu, LichSuKhamBenhNhan.this, new LichSuBenhNhanAdapter.ILichSuBenhNhanAdapter() {
            @Override
            public void clickChiTiet(LichSu lichSu) {
                xuLyChiTiet(lichSu);
            }
        });

        rcvLichSu.setAdapter(lichSuAdapter);
    }
}