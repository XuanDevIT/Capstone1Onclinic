package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adapter.PhongKhamAdapter;
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

public class DatPhong extends AppCompatActivity {

    private RecyclerView rcvPhongKham;
    private PhongKhamAdapter phongKhamAdapter;
    private List<PhongKham> phongKhamList;

    Button btnDongY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_phong);
        addControls();
        layDanhSachPhongKhamTuFireBase();
    }

    private void xyLyDongY(PhongKham phongKham) {
        DataLocalManager.setPhongKham(phongKham);
        Intent intent = new Intent(DatPhong.this, DatPhong2.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("OBJECT_PHONG_KHAM",phongKham);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void layDanhSachPhongKhamTuFireBase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phongKhamList.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    PhongKham phongKham = data.getValue(PhongKham.class);
                    phongKhamList.add(phongKham);
                }
                phongKhamAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DatPhong.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if(phongKhamAdapter!= null)
            phongKhamAdapter.release();
    }

    private void addControls() {
        rcvPhongKham = findViewById(R.id.dsphongkham);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DatPhong.this);
        rcvPhongKham.setLayoutManager(layoutManager);

        phongKhamList = new ArrayList<>();
        phongKhamAdapter = new PhongKhamAdapter(phongKhamList, DatPhong.this, new PhongKhamAdapter.IPhongKhamAdapter() {
            @Override
            public void clickPhongKham(PhongKham phongKham) {
                btnDongY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xyLyDongY(phongKham);
                    }
                });
            }
        });

        rcvPhongKham.setAdapter(phongKhamAdapter);

        btnDongY = findViewById(R.id.btnDongY);
    }

}