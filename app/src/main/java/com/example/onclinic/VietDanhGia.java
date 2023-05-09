package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.helper.NgayGio;
import com.example.local_data.DataLocalManager;
import com.example.model.DanhGia;
import com.example.model.PhongKham;
import com.example.helper.CheckData;
import com.example.helper.NoteFireBase;
import com.example.model.ThongBao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VietDanhGia extends AppCompatActivity {
    EditText edtBinhLuan;
    Button btnSubmitDanhGia;
    RatingBar rating_bar;
    String idDguoiDung;
    PhongKham phongKham;
    DanhGia DG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        phongKham = (PhongKham) bundle.getSerializable("OBJECT_PHONG_KHAM4");
        DG = (DanhGia) bundle.getSerializable("OBJECT_DANH_GIA");
        idDguoiDung = DataLocalManager.getIDNguoiDung();
        AnhXa();
        btnSubmitDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyHoanThanhDanhGia();
            }
        });
    }

    private void xulyHoanThanhDanhGia(){
        if(checkInput()) {
            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
            Float rating = rating_bar.getRating();
            String nhanxet = edtBinhLuan.getText().toString().trim();
            DanhGia danhGia = new DanhGia(idDguoiDung, rating, nhanxet);

            DatabaseReference refThongBaoBS = myRef.child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI).child(phongKham.getIdBacSi()).child(NoteFireBase.THONGBAO);
            String idThongBao = refThongBaoBS.push().getKey();
            String ngayThongBao = NgayGio.GetDateCurrentString();
            String gioThongBao = NgayGio.GetTimeCurrentString();
            ThongBao thongBao = new ThongBao(idThongBao,"Phòng khám có 1 đánh giá mới",
                    "Nhận xét về phòng khám: "+nhanxet,
                    ngayThongBao,gioThongBao, ThongBao.ThongBaoDanhGia);
            refThongBaoBS.child(idThongBao).setValue(thongBao);

            myRef.child(NoteFireBase.PHONGKHAM).child(phongKham.getIdPhongKham()).child(NoteFireBase.DANHGIA)
                    .child(idDguoiDung).setValue(danhGia, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(VietDanhGia.this, "Cảm ơn bạn đã phản hồi", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }
    }

    private boolean checkInput()
    {
        if(CheckData.isEmpty(edtBinhLuan) && rating_bar.getRating()!=0)
            return true;
        else return false;
    }

    private void AnhXa(){
        rating_bar = findViewById(R.id.rating_bar);
        edtBinhLuan = findViewById(R.id.edt_BinhLuan);
        btnSubmitDanhGia = findViewById(R.id.btnSubmitDanhGia);
        if(DG!=null)
        {
            rating_bar.setRating(DG.getRating());
            edtBinhLuan.setText(DG.getNhanXet());
        }
    }
}