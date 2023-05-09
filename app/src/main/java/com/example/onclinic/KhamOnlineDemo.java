package com.example.onclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.local_data.DataLocalManager;
import com.example.model.LichKham;
import com.example.model.LichSu;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KhamOnlineDemo extends AppCompatActivity {

    TextView txtThoiGian, txtHinhThuc,txtPhongKham;
    Button btnKetThuc;
    LichKham lichKham;
    PhongKham phongKham;
    NguoiDung benhNhan;
    int role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kham_online_demo);
        role = DataLocalManager.getRole();
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        if(role == 0)
        {
            lichKham = (LichKham) bundle.getSerializable("LICH_KHAM");
            phongKham = (PhongKham) bundle.getSerializable("PHONG_KHAM");
        }
        else{
            lichKham = (LichKham) bundle.getSerializable("LICH_KHAM");
            benhNhan = (NguoiDung) bundle.getSerializable("BENH_NHAN");
        }
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyKetThuc();
            }
        });
    }

    private void xuLyKetThuc() {
        duaLichSuKhamLenFirebase();//gồm phòng khám, lịch khám
    }

    private void duaLichSuKhamLenFirebase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
        String key = myRef.child(NoteFireBase.LICHSU).push().getKey();
        LichSu lichSu = new LichSu(phongKham, lichKham);
        //cập nhật lại trạng thái của lịch khám là khám xong
        lichKham.setTrangThai(LichKham.KhamXong);
        DatabaseReference refLichkham = myRef.child(NoteFireBase.PHONGKHAM).child(phongKham.getIdPhongKham()).child(NoteFireBase.LICHKHAM).child(lichKham.getIdLichKham());
        refLichkham.setValue(lichKham);
        //đưa dữ liệu lịch sử lên firebase
        lichSu.setIdLichSu(key);
        myRef.child(NoteFireBase.LICHSU).child(key).setValue(lichSu);
        troVeManHinhTrangChu();
        Toast.makeText(KhamOnlineDemo.this,"Đã tạo 1 lịch sử khám trên firebase",Toast.LENGTH_LONG).show();
    }

    private void troVeManHinhTrangChu() {
        Intent intent = new Intent(KhamOnlineDemo.this,TrangChuBenhNhan.class);
        startActivity(intent);
        finishAffinity();
    }

    private void addControls() {
        btnKetThuc = findViewById(R.id.btnKetThuc);
        if(role==0)
        {
            txtPhongKham = findViewById(R.id.txtDemoPhongKham);
            txtPhongKham.setText("Phòng khám "+phongKham.getTenPhongKham());
        }
        else{
            txtPhongKham = findViewById(R.id.txtDemoPhongKham);
            txtPhongKham.setText("Bệnh nhân "+benhNhan.getTenNguoiDung());
        }
        txtThoiGian = findViewById(R.id.txtDemoThoiGian);
        txtThoiGian.setText("Thời gian: "+lichKham.getNgayKham()+"vào lúc "+lichKham.getGioKham());
        txtHinhThuc = findViewById(R.id.txtDemoHinhThuc);
        txtHinhThuc.setText("Hình thức: "+lichKham.getHinhThucKham());
    }
}