package com.example.onclinic;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.local_data.DataLocalManager;

public class TrangChuBacSi extends MyBaseActivity {

    LinearLayout btnQuanly,btnLichKham,btnDoNhipTim,btnLienHe,btnLichSuKham,btnDonThuoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_trang_chu_bac_si,null,false);
        mDrawerLayout.addView(view,0);
        //setContentView(R.layout.activity_trang_chu_bac_si);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnQuanly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBacSi.this,QuanLyPhongKham.class);
                startActivity(intent);
            }
        });
        btnLichKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBacSi.this,LichKhamBacSi.class);
                startActivity(intent);
            }
        });
        btnDonThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBacSi.this,DonThuocBacSi.class);
                startActivity(intent);
            }
        });
        btnLichSuKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBacSi.this, LichSuKhamBacSi.class);
                startActivity(intent);
            }
        });
        btnLienHe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuBacSi.this, LienHe.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(TrangChuBacSi.this)
                .setTitle("Thông báo").setMessage("Bạn muốn thoát ứng dụng?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TrangChuBacSi.this,LoiChao.class);
                        startActivity(intent);
                        finishAffinity();
                        killActivity();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void killActivity() {
        this.finish();
        return;
    }


    private void addControls() {
        btnQuanly = findViewById(R.id.btnQuanLyPhongKhamBS);
        btnLichKham = findViewById(R.id.btnLichKhamBS);
        btnDonThuoc = findViewById(R.id.btnDonThuocBS);
        btnLienHe = findViewById(R.id.btnLienHeBS);
        btnLichSuKham = findViewById(R.id.btnLichSuKhamBS);
    }
}