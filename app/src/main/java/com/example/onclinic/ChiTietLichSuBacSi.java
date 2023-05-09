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

import java.util.List;

public class ChiTietLichSuBacSi extends AppCompatActivity {

    TextView txtTenBenhNhan, txtDiaChi, txtEmail,txtNgayKham, txtHinhThuc, txtTroVe;
    TextView txtTrieuChung,txtBenh, txtChuY,txtTongTien;

    RecyclerView rcvDonThuoc;
    List<DonThuoc> listDonThuoc;
    DonThuocAdapter donThuocAdapter;
    LichSu lichSu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_lich_su_bac_si);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        lichSu = (LichSu) bundle.getSerializable("CHI_TIET_LICH_SU_BAC_SI");
        layDuLieuBenhNhan();
        addControls();
        addEvents();
    }

    private void layDuLieuBenhNhan() {
        String idBenhNhan = lichSu.getLichKham().getIdBenhNhan();
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference()
                .child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).child(idBenhNhan);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                txtTenBenhNhan.setText("Bệnh nhân "+nguoiDung.getTenNguoiDung());
                txtDiaChi.setText(nguoiDung.getQuan()+" - "+nguoiDung.getThanhpho());
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
        txtTenBenhNhan = findViewById(R.id.txtTenBenhNhanCTLSViewBS);
        txtDiaChi = findViewById(R.id.txtDiaChiCTLSViewBS);
        txtEmail = findViewById(R.id.txtEmailCTLSViewBS);
        txtNgayKham = findViewById(R.id.txtNgayKhamCTLSViewBS);
        txtNgayKham.setText(lichSu.getLichKham().getNgayKham());
        txtHinhThuc = findViewById(R.id.txtHinhThucCTLSViewBS);
        txtHinhThuc.setText(lichSu.getLichKham().getHinhThucKham());
        txtTroVe = findViewById(R.id.txtTroVeViewBS);
        txtTrieuChung = findViewById(R.id.txtTrieuChungCTLSViewBS);
        txtTrieuChung.setText("Triệu chứng: "+lichSu.getBenh().getTrieuChung());
        txtBenh = findViewById(R.id.txtBenhCTLSViewBS);
        txtBenh.setText("Bệnh: "+lichSu.getBenh().getBenh());
        txtChuY = findViewById(R.id.txtChuYCTLSViewBS);
        txtChuY.setText("Ghi chú: "+lichSu.getBenh().getGhiChu());
        rcvDonThuoc = findViewById(R.id.rcvDonThuocCTLSViewBS);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChiTietLichSuBacSi.this);
        rcvDonThuoc.setLayoutManager(layoutManager);

        listDonThuoc = lichSu.getDonThuocList();
        donThuocAdapter = new DonThuocAdapter(listDonThuoc, ChiTietLichSuBacSi.this, new DonThuocAdapter.IDonThuocAdapter() {
            @Override
            public void clickItemDonThuoc(DonThuoc donThuoc) {

            }
        });
        rcvDonThuoc.setAdapter(donThuocAdapter);

        txtTongTien = findViewById(R.id.txtTongTienCTLSViewBS);
        int tien = 0;
        for(DonThuoc donThuoc: listDonThuoc)
        {
            tien += donThuoc.getDonGia()*donThuoc.getSoLuong();
        }
        txtTongTien.setText("Tổng tiền: "+String.valueOf(tien)+" VNĐ");
    }
}