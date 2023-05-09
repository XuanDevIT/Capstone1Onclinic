package com.example.onclinic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.local_data.DataLocalManager;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongTinPhongKhamViewBenhNhan extends AppCompatActivity {

    PhongKham phongKham;

    ImageView avatarPK;
    TextView txt_TenPK, txt_TenBSPK, txt_EmailPK, txt_ChuyenKhoaPK, txt_DiaChiPK, txt_MoTaPK;
    Button bnt_PhanHoiPK;

    String idBacSi;

    int role;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_phong_kham_view);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        phongKham = (PhongKham) bundle.getSerializable("OBJECT_PHONG_KHAM2");
        idBacSi = phongKham.getIdBacSi();
        role = DataLocalManager.getRole();
        AnhXa();
        Event();
    }

    private void Event(){
       bnt_PhanHoiPK.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ThongTinPhongKhamViewBenhNhan.this, ViewDanhGiaBN.class);
               Bundle bundle = new Bundle();
               bundle.putSerializable("OBJECT_PHONG_KHAM3",phongKham);
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });
    }

    private void AnhXa()
    {
        txt_TenBSPK =findViewById(R.id.txtTenBSView);
        txt_EmailPK = findViewById(R.id.txtEmailView);
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI).child(idBacSi);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                txt_TenBSPK.setText("Bác sĩ: " + nguoiDung.getTenNguoiDung());
                txt_EmailPK.setText("Thư điện tử: " + nguoiDung.getEmail_sdt());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongTinPhongKhamViewBenhNhan.this, "Lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        avatarPK = findViewById(R.id.PhongkhamAvatarView);
        if(phongKham.getHinhAnh()!=null) {
            byte[] decodedString = Base64.decode(phongKham.getHinhAnh(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatarPK.setImageBitmap(decodedByte);
        }

        txt_TenPK = findViewById(R.id.txtTenPhongKhamView);
        txt_TenPK.setText(phongKham.getTenPhongKham());

        txt_ChuyenKhoaPK = findViewById(R.id.txtChuyenKhoaView);
        txt_ChuyenKhoaPK.setText("Chuyên khoa: " + phongKham.getChuyenKhoa());

        txt_DiaChiPK = findViewById(R.id.txtDiaChiView);
        txt_DiaChiPK.setText("Địa chỉ: " + phongKham.getDiaChi());

        txt_MoTaPK = findViewById(R.id.txtMoTaView);
        txt_MoTaPK.setText("Mô tả: " + phongKham.getMoTa());

        bnt_PhanHoiPK = findViewById(R.id.btnDanhGiaView);
    }
}
