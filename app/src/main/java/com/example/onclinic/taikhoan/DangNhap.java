package com.example.onclinic.taikhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.local_data.DataLocalManager;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.example.onclinic.R;
import com.example.onclinic.TrangChuBacSi;
import com.example.onclinic.TrangChuBenhNhan;
import com.example.helper.ActivityState;
import com.example.helper.CheckData;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DangNhap extends AppCompatActivity {

    private EditText edtEmailHoacSdt, edtMatKhau;
    private Button btnDangNhap;
    private TextView txtQuenMk;
    private TextView txtDangNhap;

    private CheckBox chkLuuThongTin;
    String thongTinDangNhap = "LOGIN";
    String strEmail, strMatKhau;

    private ProgressDialog progressDialog;

    List<NguoiDung> listBenhNhan;
    List<NguoiDung> listBacSi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        AnhXa();
        layDanhSachNguoiDungTuFireBase(new IDangNhap() {
            @Override
            public void layDanhSachBenhNhan(List<NguoiDung> listBN) {
                listBenhNhan = listBN;
            }

            @Override
            public void layDanhSachBacSi(List<NguoiDung> listBS) {
               listBacSi = listBS;
            }
        });
        DataLocalManager.setActivityNumber(ActivityState.ACTIVITY_TRANGCHU);
        addEvents();
    }

    private void addEvents() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        xuLyDangNhap();
                        if(xuLyDangNhap()==-1) Toast.makeText(DangNhap.this, "Email hoặc mật khẩu đã sai.", Toast.LENGTH_SHORT).show();
                    }
                },3000);
            }
        });
        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyDangKy();
            }
        });
        txtQuenMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyQuenMatKhau();
            }
        });
    }

    private void xuLyDangKy() {
        Intent intent = new Intent(DangNhap.this, DangKy.class);
        startActivity(intent);
        finish();
    }

    private void xuLyQuenMatKhau() {
        Intent intent = new Intent(DangNhap.this, QuenMatKhau.class);
        startActivity(intent);
    }

    private int xuLyDangNhap() {
        strEmail = edtEmailHoacSdt.getText().toString().trim();
        strMatKhau = edtMatKhau.getText().toString().trim();
        for (NguoiDung nguoiDung : listBenhNhan) {
            if (strEmail.equals(nguoiDung.getEmail_sdt()) && strMatKhau.equals(nguoiDung.getMatKhau())) {
                DataLocalManager.setIDNguoiDung(nguoiDung.getUserID().toString());//lưu id vào dữ liệu local
                DataLocalManager.setNguoiDung(nguoiDung);//lưu người dùng vào dữ liệu local
                DataLocalManager.setRole(0);

                Intent intent = new Intent(DangNhap.this, TrangChuBenhNhan.class);
                startActivity(intent);
                finishAffinity();
                return 0;
            }
        }
        for (NguoiDung nguoiDung : listBacSi) {
            if (strEmail.equals(nguoiDung.getEmail_sdt()) && strMatKhau.equals(nguoiDung.getMatKhau())) {
                DataLocalManager.setIDNguoiDung(nguoiDung.getUserID().toString());//lưu id vào dữ liệu local
                DataLocalManager.setNguoiDung(nguoiDung);//lưu người dùng vào dữ liệu local
                DataLocalManager.setRole(1);

                DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            PhongKham phong = data.getValue(PhongKham.class);
                            if (nguoiDung.getUserID().equals(phong.getIdBacSi())) {
                                DataLocalManager.setIDPhongKham(phong.getIdPhongKham());
                                DataLocalManager.setPhongKham(phong);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(DangNhap.this, TrangChuBacSi.class);
                startActivity(intent);
                finishAffinity();
                return 1;
            }
        }
        return -1;
    }

    private boolean checkInput() {
        //kiểm tra dữ liệu nhập vào
        if (CheckData.isEmpty(edtEmailHoacSdt) && CheckData.isEmpty(edtMatKhau))
            return true;
        return false;
    }

    private void layDanhSachNguoiDungTuFireBase(IDangNhap iDangNhap)
    {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference(NoteFireBase.NGUOIDUNG);//đang ở note roof
        DatabaseReference refBenhNhan = myRef.child(NoteFireBase.BENHNHAN);//đến note bệnh nhân
        refBenhNhan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBenhNhan.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    NguoiDung nguoiDung = data.getValue(NguoiDung.class);
                    listBenhNhan.add(nguoiDung);//them benh nhan tu fb vao danh sach
                }
                iDangNhap.layDanhSachBenhNhan(listBenhNhan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DangNhap.this, "Lỗi đọc dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference refBacSi = myRef.child(NoteFireBase.BACSI);
        refBacSi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBacSi.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    NguoiDung nguoiDung = data.getValue(NguoiDung.class);
                    listBacSi.add(nguoiDung);//them bac si tu fb vao danh sach
                }
                iDangNhap.layDanhSachBacSi(listBacSi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DangNhap.this, "Lỗi đọc dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface IDangNhap{
        void layDanhSachBenhNhan(List<NguoiDung> listBenhNhan);
        void layDanhSachBacSi(List<NguoiDung> listBacSi);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(thongTinDangNhap, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName",edtEmailHoacSdt.getText().toString());
        editor.putString("PassWord",edtMatKhau.getText().toString());
        editor.putBoolean("Save",chkLuuThongTin.isChecked());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(thongTinDangNhap, MODE_PRIVATE);
        String userName = preferences.getString("UserName","");
        String passWord = preferences.getString("PassWord","");
        boolean save = preferences.getBoolean("Save",false);
        if(save)
        {
            chkLuuThongTin.setChecked(true);
            edtEmailHoacSdt.setText(userName);
            edtMatKhau.setText(passWord);
        }
    }

    private void AnhXa() {
        edtEmailHoacSdt = findViewById(R.id.edtEmailHoacSdt);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMk = findViewById(R.id.txtQuenMK);
        txtDangNhap = findViewById(R.id.txtDangNhap);
        chkLuuThongTin = findViewById(R.id.chkLuuThongTin);
        progressDialog = new ProgressDialog(DangNhap.this);
        listBenhNhan = new ArrayList<>();
        listBacSi = new ArrayList<>();
    }
}