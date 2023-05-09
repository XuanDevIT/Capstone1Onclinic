package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.local_data.DataLocalManager;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.example.helper.CheckData;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TaoPhongKham extends AppCompatActivity {

    EditText edtTenPhongKham,edtChuyenKhoa,edtDiaChi,edtMoTa;
    CheckBox chkOnline,chkTrucTiep;
    TextView btnChonAnh;
    ImageView imgAnh;
    Bitmap selectedBitmap;
    Button btnHuy, btnXacNhan;
    String idNguoiDung;
    NguoiDung nguoiDung;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_phong_kham);

        idNguoiDung = DataLocalManager.getIDNguoiDung();
        nguoiDung = DataLocalManager.getNguoiDung();

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonAnh();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXacNhan();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyHuy();
            }
        });
    }

    private void xuLyChonAnh() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(TaoPhongKham.this);
    }

    //xử lí hiển thị và lưu ảnh vào bitmap
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(TaoPhongKham.this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgAnh.setImageBitmap(selectedBitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void xuLyXacNhan() {
        if(checkInput()) {
            try
            {
                xoaBenhNhanThemBacSi();
                DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
                String keyID = myRef.child(NoteFireBase.PHONGKHAM).push().getKey();

                //đưa ảnh về kiểu chuỗi base64
                String imgEncoded = "";
                if(selectedBitmap!=null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    imgEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                }

                //đưa checkbox về chuỗi
                String hinhThuc = "";
                if (chkTrucTiep.isChecked())
                {
                    if(chkOnline.isChecked()) hinhThuc += "Online - Trực tiếp";
                    else hinhThuc += "Trực tiếp";
                }
                else hinhThuc = "Online";

                //tạo đối tượng và đưa lên firebase
                PhongKham phongKham = new PhongKham(
                        edtTenPhongKham.getText().toString().trim(),
                        edtChuyenKhoa.getText().toString().trim(),
                        edtDiaChi.getText().toString().trim(),
                        edtMoTa.getText().toString().trim(),
                        imgEncoded, hinhThuc);
                phongKham.setIdPhongKham(keyID);
                phongKham.setIdBacSi(idNguoiDung);
                myRef.child(NoteFireBase.PHONGKHAM).child(keyID).setValue(phongKham, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        troVeTrangChu();
                    }
                });
            }
            catch (Exception ex)
            {
                Toast.makeText(TaoPhongKham.this, "Lỗi tạo phòng khám", Toast.LENGTH_LONG).show();
            }
        }
        else Toast.makeText(TaoPhongKham.this, "Hãy hoàn thành thông tin đăng ký", Toast.LENGTH_SHORT).show();
    }

    private void troVeTrangChu()
    {
        Toast.makeText(TaoPhongKham.this, "Quản lí phòng khám của bạn tại chức năng [Quản Lí Phòng Khám]", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TaoPhongKham.this,TrangChuBacSi.class);
        startActivity(intent);
        finish();
    }

    private void xoaBenhNhanThemBacSi()
    {
        DatabaseReference myRefBN = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        myRefBN.child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).child(idNguoiDung).removeValue();
        DatabaseReference myRefBS = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        myRefBS.child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI).child(idNguoiDung).setValue(nguoiDung);
    }

    private boolean checkInput()
    {
        if(CheckData.isEmpty(edtTenPhongKham) && CheckData.isEmpty(edtChuyenKhoa)
        && CheckData.isEmpty(edtDiaChi))
        {
            if(!chkTrucTiep.isChecked() && !chkOnline.isChecked())
            {
                chkOnline.setTextColor(Color.RED);
                chkTrucTiep.setTextColor(Color.RED);
                return false;
            }
            return true;
        }
        return false;
    }

    private void xuLyHuy() {
        AlertDialog alertDialog = new AlertDialog.Builder(TaoPhongKham.this)
                .setTitle("Thông báo").setMessage("Bạn muốn Hủy tạo phòng khám?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TaoPhongKham.this,TrangChuBenhNhan.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void addControls() {
        edtTenPhongKham = findViewById(R.id.edtTenPhongKham);
        edtChuyenKhoa = findViewById(R.id.edtChuyenKhoa);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtMoTa = findViewById(R.id.edtMoTa);
        chkOnline = findViewById(R.id.chkOnline);
        chkTrucTiep = findViewById(R.id.chkTrucTiep);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        imgAnh = findViewById(R.id.imgTaoPhongKham);
        btnHuy = findViewById(R.id.btnHuyPK);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }
}