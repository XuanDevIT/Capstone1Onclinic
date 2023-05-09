package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.adapter.DonThuocAdapter;
import com.example.model.DonThuoc;
import com.example.model.LichSu;
import com.example.model.NguoiDung;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChiTietLichSuBenhNhan extends AppCompatActivity {

    TextView txtTenPhongKham, txtDiaChi, txtBacSi, txtEmail,txtNgayKham, txtHinhThuc, txtTroVe;
    TextView txtTrieuChung,txtBenh, txtChuY,txtTongTien;

    RecyclerView rcvDonThuoc;
    List<DonThuoc> listDonThuoc;
    DonThuocAdapter donThuocAdapter;
    LichSu lichSu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_lich_su_benh_nhan);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        lichSu = (LichSu) bundle.getSerializable("CHI_TIET_LICH_SU_BENH_NHAN");
        layDuLieuBacSi();
        addControls();
        addEvents();
    }

    private void layDuLieuBacSi() {
        String idBacSi = lichSu.getPhongKham().getIdBacSi();
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference()
                .child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI).child(idBacSi);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                txtBacSi.setText(nguoiDung.getTenNguoiDung());
                txtEmail.setText(nguoiDung.getEmail_sdt());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvents() {
        txtTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addControls() {
        txtTenPhongKham = findViewById(R.id.txtTenPhongKhamCTLSViewBN);
        txtTenPhongKham.setText("Phòng khám "+lichSu.getPhongKham().getTenPhongKham());
        txtDiaChi = findViewById(R.id.txtDiaChiCTLSViewBN);
        txtDiaChi.setText(lichSu.getPhongKham().getDiaChi());
        txtBacSi = findViewById(R.id.txtBacSiCTLSViewBN);
        txtEmail = findViewById(R.id.txtEmailCTLSViewBN);
        txtNgayKham = findViewById(R.id.txtNgayKhamCTLSViewBN);
        txtNgayKham.setText(lichSu.getLichKham().getNgayKham());
        txtHinhThuc = findViewById(R.id.txtHinhThucCTLSViewBN);
        txtHinhThuc.setText(lichSu.getLichKham().getHinhThucKham());
        txtTroVe = findViewById(R.id.txtTroVeViewBN);
        txtTrieuChung = findViewById(R.id.txtTrieuChungCTLSViewBN);
        txtTrieuChung.setText("Triệu chứng: "+lichSu.getBenh().getTrieuChung());
        txtBenh = findViewById(R.id.txtBenhCTLSViewBN);
        txtBenh.setText("Bệnh: "+lichSu.getBenh().getBenh());
        txtChuY = findViewById(R.id.txtChuYCTLSViewBN);
        if(lichSu.getBenh().getGhiChu() == null) txtChuY.setText("Ghi chú: Trống");
        else txtChuY.setText("Ghi chú: "+lichSu.getBenh().getGhiChu());
        rcvDonThuoc = findViewById(R.id.rcvDonThuocCTLSViewBN);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChiTietLichSuBenhNhan.this);
        rcvDonThuoc.setLayoutManager(layoutManager);

        listDonThuoc = new ArrayList<>();
        listDonThuoc = lichSu.getDonThuocList();
        donThuocAdapter = new DonThuocAdapter(listDonThuoc, ChiTietLichSuBenhNhan.this, new DonThuocAdapter.IDonThuocAdapter() {
            @Override
            public void clickItemDonThuoc(DonThuoc donThuoc) {

            }
        });
        rcvDonThuoc.setAdapter(donThuocAdapter);

        txtTongTien = findViewById(R.id.txtTongTienCTLSViewBN);
        int tien = 0;
        if(listDonThuoc == null)
            txtTongTien.setText(String.valueOf(tien)+" VNĐ");
        else{
            for(DonThuoc donThuoc: listDonThuoc)
            {
                tien += donThuoc.getDonGia()*donThuoc.getSoLuong();
            }
            txtTongTien.setText(String.valueOf(tien)+" VNĐ");
        }
    }
}