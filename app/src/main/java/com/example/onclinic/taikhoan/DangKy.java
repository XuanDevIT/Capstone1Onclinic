package com.example.onclinic.taikhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.model.NguoiDung;
import com.example.onclinic.R;
import com.example.helper.CheckData;
import com.example.helper.NoteFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class DangKy extends AppCompatActivity {

    private EditText edtEmailHoacSdt, edtTen,edtMatKhau, edtNLMatKhau;
    private Spinner spnQuan, spnThanhPho;
    private Button btnDangKy;
    private TextView txtDangNhap,txtNgaySinh;
    private ImageButton imgNgaySinh;
    private ProgressDialog progressDialog;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    //int lastSelected = -1;
    ArrayList<String>dsQuan,dsThanhPho;
    ArrayAdapter<String>adapterQuan,adapterThanhPho;
    FirebaseAuth auth;
    private String email,matkhau,nlmatkhau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        auth = FirebaseAuth.getInstance();
        AnhXa();
        addEvents();
    }

    private void addEvents() {
        spnThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                    {
                        HienThiQuanHuyen(R.array.quanAnGiang);
                        break;
                    }
                    case 1:
                    {
                        HienThiQuanHuyen(R.array.quanBaRiaVungTau);
                        break;
                    }
                    case 2:
                    {
                        HienThiQuanHuyen(R.array.quanBacGiang);
                        break;
                    }

                    case 14:
                    {
                        HienThiQuanHuyen(R.array.quanDaNang);
                        break;
                    }

                    case 55:
                    {
                        HienThiQuanHuyen(R.array.quanTTHue);
                        break;
                    }
                    default:
                        adapterQuan = null;
                        break;

                }
                //lastSelected = position;
                spnQuan.setAdapter(adapterQuan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangKy.this, DangNhap.class);
                startActivity(intent);
                finish();
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyDangKy();
            }
        });
        imgNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiCalendar();
            }
        });
    }

    private void xuLyDangKy() {
        if (checkInput())
        {
            progressDialog.show();
            try
            {
                auth.createUserWithEmailAndPassword(email,matkhau)
                        .addOnCompleteListener(DangKy.this,new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DangKy.this,DangNhap.class));
                                    duaDuLieuLenFireBase();
                                    finish();
                                }
                                else Toast.makeText(DangKy.this, "Lỗi đăng ký tài khoản", Toast.LENGTH_LONG).show();
                            }
                        });

            }
            catch (Exception ex)
            {
                Toast.makeText(DangKy.this, "Lỗi đăng ký tài khoản", Toast.LENGTH_LONG).show();
            }
        }
        else Toast.makeText(DangKy.this, "Hãy hoàn thành thông tin đăng ký", Toast.LENGTH_LONG).show();
    }

    private boolean checkInput() {
        //kiểm tra dữ liệu nhập vào
        if(CheckData.isEmpty(edtTen) && CheckData.isEmpty(edtEmailHoacSdt)
        && CheckData.isEmpty(edtMatKhau) && CheckData.isEmpty(edtNLMatKhau))
        {
            email = edtEmailHoacSdt.getText().toString().trim();
            matkhau = edtMatKhau.getText().toString().trim();
            nlmatkhau = edtNLMatKhau.getText().toString().trim();
            if(matkhau.length()<6)
            {
                edtMatKhau.requestFocus();
                edtMatKhau.setError("Mật khẩu quá ngắn");
                return false;
            }
            if(nlmatkhau.equals(matkhau)==false) {
                edtNLMatKhau.requestFocus();
                edtNLMatKhau.setError("Mật khẩu không giống");
                return false;
            }
            return true;
        }
        return false;
    }

    private void duaDuLieuLenFireBase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        //lấy ra key từ firebase (giống primary key)
        String keyID = myRef.child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).push().getKey();
        NguoiDung nguoiDung = new NguoiDung(email, matkhau,
                edtTen.getText().toString().trim(),
                txtNgaySinh.getText().toString(),
                spnThanhPho.getSelectedItem().toString(),
                spnQuan.getSelectedItem().toString());
        nguoiDung.setUserID(keyID);
        myRef.child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).child(keyID).setValue(nguoiDung);
    }
    private void hienThiCalendar() {
        //bắt sự kiện khi click trên calendar
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(calendar.YEAR,year);
                calendar.set(calendar.MONTH,month);
                calendar.set(calendar.DAY_OF_MONTH,dayOfMonth);
                txtNgaySinh.setText(sdf.format(calendar.getTime()));
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(DangKy.this,
                android.R.style.Theme_Holo_Light_Dialog,
                dateSetListener,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
        //làm mờ màn hình chính sau khi hiện calendar
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void HienThiQuanHuyen(int position) {
        dsQuan = new ArrayList<>();
        dsQuan.addAll(Arrays.asList(getResources().getStringArray(position)));
        adapterQuan = new ArrayAdapter<String>(
                DangKy.this, android.R.layout.simple_spinner_item, dsQuan);
    }

    private void AnhXa() {
        edtEmailHoacSdt = findViewById(R.id.edtEmailHoacSdt);
        edtTen = findViewById(R.id.edtTen);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtNLMatKhau = findViewById(R.id.edtNLMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        txtDangNhap = findViewById(R.id.txtDangNhap);
        txtNgaySinh = findViewById(R.id.txtNgaySinh);
        txtNgaySinh.setText(sdf.format(calendar.getTime()));
        imgNgaySinh = findViewById(R.id.imgNgaySinh);
        progressDialog = new ProgressDialog(this);

        spnQuan = findViewById(R.id.spnQuan);
        spnThanhPho = findViewById(R.id.spnThanhPho);
        dsThanhPho = new ArrayList<>();
        dsThanhPho.addAll(Arrays.asList(getResources().getStringArray(R.array.arrThanhPho)));
        adapterThanhPho = new ArrayAdapter<String>(
                DangKy.this, android.R.layout.simple_spinner_item, dsThanhPho
        );
        adapterThanhPho.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnThanhPho.setAdapter(adapterThanhPho);
    }
}