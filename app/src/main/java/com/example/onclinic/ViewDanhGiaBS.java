package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.DanhGiaAdapter;
import com.example.local_data.DataLocalManager;
import com.example.model.DanhGia;
import com.example.model.PhongKham;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDanhGiaBS extends AppCompatActivity {

    DanhGiaAdapter nDanhGiaAdapter;
    List<DanhGia> nListDanhGia;

    TextView tenPK;
    PhongKham phongKham;
    RecyclerView rcvDanhGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_danh_gia_bs);
        phongKham = DataLocalManager.getPhongKham();
        getListDanhGiaFromRealtimeDatabase();
        anhXa();
    }


    private void getListDanhGiaFromRealtimeDatabase(){
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
        DatabaseReference refDanhGiaPK = myRef.child(phongKham.getIdPhongKham()).child(NoteFireBase.DANHGIA);
        refDanhGiaPK.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nListDanhGia.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    DanhGia danhGia = data.getValue(DanhGia.class);
                    nListDanhGia.add(danhGia);
                }
                nDanhGiaAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDanhGiaBS.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void anhXa(){
        tenPK = findViewById(R.id.txt_DanhGiaViewBS_TenPK);
        tenPK.setText("#Đánh giá của phòng khám " + phongKham.getTenPhongKham());

        rcvDanhGia = findViewById(R.id.rcv_DanhGiaViewBS);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDanhGia.setLayoutManager(linearLayoutManager);

        nListDanhGia = new ArrayList<>();
        nDanhGiaAdapter = new DanhGiaAdapter(nListDanhGia, ViewDanhGiaBS.this);

        rcvDanhGia.setAdapter(nDanhGiaAdapter);
    }
}