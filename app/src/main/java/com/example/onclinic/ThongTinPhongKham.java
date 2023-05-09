package com.example.onclinic;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.local_data.DataLocalManager;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.example.helper.NoteFireBase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ThongTinPhongKham extends AppCompatActivity {

    TextView txtTenPhongKham, txtTenBS, txtEmail, txtChuyenKhoa, txtDiaChi, txtMoTa;
    Button bntDanhGia;
    ImageView avatar;
    NguoiDung nguoiDung;
    PhongKham phongKham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_phong_kham);
        phongKham = DataLocalManager.getPhongKham();
        nguoiDung = DataLocalManager.getNguoiDung();
        AnhXa();
        addEvents();
    }

    private void addEvents(){
        bntDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThongTinPhongKham.this,ViewDanhGiaBS.class);
                startActivity(intent);
            }
        });
    }

    private void AnhXa(){
        txtTenBS = findViewById(R.id.txtTenBSViewBS);
        txtTenBS.setText("Bác sĩ: "+nguoiDung.getTenNguoiDung());
        txtEmail = findViewById(R.id.txtEmailViewBS);
        txtEmail.setText("Thử điện tử: "+nguoiDung.getEmail_sdt());
        txtTenPhongKham = findViewById(R.id.txtTenPhongKhamViewBS);
        txtTenPhongKham.setText(phongKham.getTenPhongKham());
        txtChuyenKhoa = findViewById(R.id.txtChuyenKhoaViewBS);
        txtChuyenKhoa.setText("Chuyên khoa: "+phongKham.getChuyenKhoa());
        txtDiaChi = findViewById(R.id.txtDiaChiViewBS);
        txtDiaChi.setText("Địa chỉ: "+phongKham.getDiaChi());
        txtMoTa = findViewById(R.id.txtMoTaViewBS);
        txtMoTa.setText("Mô tả: "+phongKham.getMoTa());
        avatar = findViewById(R.id.PhongkhamAvatarViewBS);
        if(phongKham.getHinhAnh()!=null) {
            byte[] decodedString = Base64.decode(phongKham.getHinhAnh(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar.setImageBitmap(decodedByte);
        }

        bntDanhGia = findViewById(R.id.btnDanhGiaViewBS);
    }
}
