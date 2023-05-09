package com.example.onclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onclinic.taikhoan.DangKy;
import com.example.onclinic.taikhoan.DangNhap;

public class LoiChao extends AppCompatActivity {

    Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loichao);

        btnDangKy = (Button) findViewById(R.id.btnDangky);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);

        Xuly();

    }

    /*private void Xuly() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            btnDangNhap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentDN = new Intent(LoiChao.this,DangNhap.class );
                    startActivity(intentDN);
                }
            });

            btnDangKy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentDK = new Intent(LoiChao.this, DangKy.class);
                    startActivity(intentDK);
                }
            });
        }else{
            Intent intent = new Intent(LoiChao.this, TrangChuBenhNhan.class);
            startActivity(intent);
        }
    }*/

    private void Xuly()
    {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDN = new Intent(LoiChao.this, DangNhap.class );
                startActivity(intentDN);
                finish();
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDK = new Intent(LoiChao.this, DangKy.class);
                startActivity(intentDK);
            }
        });
    }
}