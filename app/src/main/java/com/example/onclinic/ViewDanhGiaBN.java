package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ViewDanhGiaBN extends AppCompatActivity {

    TextView tenPK;
    Button btnVietDanhGia;
    RecyclerView rcvDanhGia;
    DanhGiaAdapter mDanhGiaAdapter;
    List<DanhGia> mListDanhGia;
    PhongKham phongKham;
    String idNguoiDung;
    DanhGia danhGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_danh_gia_bn);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        phongKham = (PhongKham) bundle.getSerializable("OBJECT_PHONG_KHAM3");
        idNguoiDung = DataLocalManager.getIDNguoiDung();
        anhXa();
        getListDanhGiaFromRealtimeDatabase(new IViewDanhGiaBN() {
            @Override
            public void layDanhSachDanhGia(List<DanhGia> danhGiaList) {
                for(DanhGia dg : danhGiaList)
                {
                    if(idNguoiDung.equals(dg.getIdNguoiDungDG()))
                    {
                        btnVietDanhGia.setText("Chỉnh sửa đánh giá");
                        danhGia = dg;
                    }
                }
            }
        });
        addEvent();
    }

    private void addEvent(){
        btnVietDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyVietDanhGia(phongKham, danhGia);
            }
        });
    }

    private void getListDanhGiaFromRealtimeDatabase(IViewDanhGiaBN iViewDanhGiaBN){
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
        DatabaseReference refDanhGia = myRef.child(phongKham.getIdPhongKham()).child(NoteFireBase.DANHGIA);
        refDanhGia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListDanhGia.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    DanhGia danhGia = data.getValue(DanhGia.class);
                    mListDanhGia.add(danhGia);
                }
                iViewDanhGiaBN.layDanhSachDanhGia(mListDanhGia);
                mDanhGiaAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDanhGiaBN.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void xuLyVietDanhGia(PhongKham phongKham, DanhGia danhGia)
    {
        Intent intent = new Intent(ViewDanhGiaBN.this, VietDanhGia.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("OBJECT_PHONG_KHAM4",phongKham);
        bundle.putSerializable("OBJECT_DANH_GIA",danhGia);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private interface IViewDanhGiaBN{
        void layDanhSachDanhGia(List<DanhGia> danhGiaList);
    }

    private void anhXa(){
        tenPK = findViewById(R.id.txt_DanhGiaView_TenPK);
        tenPK.setText("#Đánh giá của phòng khám " + phongKham.getTenPhongKham());

        btnVietDanhGia = findViewById(R.id.btn_VietDanhGia);
        rcvDanhGia = findViewById(R.id.rcv_DanhGia);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDanhGia.setLayoutManager(linearLayoutManager);

        mListDanhGia = new ArrayList<>();
        mDanhGiaAdapter = new DanhGiaAdapter(mListDanhGia, ViewDanhGiaBN.this);

        rcvDanhGia.setAdapter(mDanhGiaAdapter);
    }
}