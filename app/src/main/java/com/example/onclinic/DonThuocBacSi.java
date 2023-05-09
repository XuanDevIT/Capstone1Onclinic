package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.adapter.DonThuocBacSiAdapter;
import com.example.local_data.DataLocalManager;
import com.example.model.LichKham;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DonThuocBacSi extends AppCompatActivity {

    RecyclerView rcvDanhSachBN;
    DonThuocBacSiAdapter donThuocBacSiAdapter;
    private List<LichKham> lichKhamList;

    String idPhongKham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_thuoc_bac_si);
        idPhongKham = DataLocalManager.getIDPhongKham();
        layDanhSachLichKhamChuaCoDonThuoc();
        addControls();
        addEvents();
    }

    private void layDanhSachLichKhamChuaCoDonThuoc() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        DatabaseReference refLichKham = myRef.child(NoteFireBase.PHONGKHAM).child(idPhongKham).child(NoteFireBase.LICHKHAM);
        refLichKham.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lichKhamList.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    LichKham lichKham = data.getValue(LichKham.class);
                    if(lichKham.getTrangThai() == LichKham.KhamXong)
                    {
                        lichKhamList.add(lichKham);
                        Collections.sort(lichKhamList, LichKham.LichKhamDateAsendingComparator);//sắp xếp ngày tăng dần
                    }
                }
                donThuocBacSiAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DonThuocBacSi.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addEvents() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(donThuocBacSiAdapter != null)
            donThuocBacSiAdapter.release();
    }

    private void addControls() {
        rcvDanhSachBN = findViewById(R.id.rcvDanhSachDonThuocBS);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DonThuocBacSi.this);
        rcvDanhSachBN.setLayoutManager(layoutManager);

        lichKhamList = new ArrayList<>();
        donThuocBacSiAdapter = new DonThuocBacSiAdapter(lichKhamList, DonThuocBacSi.this);

        rcvDanhSachBN.setAdapter(donThuocBacSiAdapter);
    }
}