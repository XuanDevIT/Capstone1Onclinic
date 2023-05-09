package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.adapter.DonThuocAdapter;
import com.example.local_data.DataLocalManager;
import com.example.model.Benh;
import com.example.model.DonThuoc;
import com.example.model.LichKham;
import com.example.model.LichSu;
import com.example.model.NguoiDung;
import com.example.model.PhongKham;
import com.example.helper.CheckData;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NhapDonThuoc extends AppCompatActivity {

    EditText edtTrieuChung, edtBenh, edtGhiChu, edtTenThuoc, edtSoLuongThuoc,edtDonViTinh,edtLieuDung, edtDonGia;
    Button btnGuiDonThuoc, btnThemThuoc;
    ImageButton btnXoa;
    ProgressDialog dialogProcess;

    RecyclerView rcvDonThuoc;
    DonThuocAdapter donThuocAdapter;
    List<DonThuoc> listDonThuoc;

    LichKham lichKham;
    NguoiDung benhNhan;
    LichSu lichSu;
    PhongKham phongKham;

    private static int nextIdDonThuoc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_don_thuoc);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) return;
        lichKham = (LichKham) bundle.getSerializable("LICH_KHAM_BAC_SI");
        benhNhan = (NguoiDung) bundle.getSerializable("BENH_NHAN");
        lichSu = (LichSu) bundle.getSerializable("LICH_SU_DON_THUOC");
        phongKham = DataLocalManager.getPhongKham();
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnThemThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemThuoc();
            }
        });
        btnGuiDonThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyGuiDonThuoc();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXoaThuoc();
            }
        });
    }

    private void xuLyGuiDonThuoc() {
        if(checkInput())
        {
            String trieuChung = edtTrieuChung.getText().toString().trim();
            String benh = edtBenh.getText().toString().trim();
            String ghiChu = edtGhiChu.getText().toString().trim();
            lichSu.setBenh(new Benh(trieuChung,benh,ghiChu));

            String keyLichSu = lichSu.getIdLichSu();
            lichKham.setTrangThai(LichKham.NhapDonThuoc);
            DatabaseReference refLichkham = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference(NoteFireBase.PHONGKHAM)
                    .child(phongKham.getIdPhongKham()).child(NoteFireBase.LICHKHAM).child(lichKham.getIdLichKham());
            refLichkham.setValue(lichKham);

            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.LICHSU);
            lichSu.getLichKham().setTrangThai(LichKham.NhapDonThuoc);
            if(listDonThuoc.size()!=0)
            {
                lichSu.setDonThuocList(listDonThuoc);
                myRef.child(keyLichSu).setValue(lichSu, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(NhapDonThuoc.this,"Gửi đơn thuốc thành công",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
            }
            else{
                AlertDialog alertDialog = new AlertDialog.Builder(NhapDonThuoc.this)
                        .setTitle("Thông báo").setMessage("Bạn muốn gửi mà không có đơn thuốc?")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef.child(keyLichSu).setValue(lichSu, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(NhapDonThuoc.this,"Gửi thành công",Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        }
    }

    private void xuLyXoaThuoc() {
        DonThuoc dt = DataLocalManager.getDonThuoc();
        AlertDialog alertDialog = new AlertDialog.Builder(NhapDonThuoc.this)
                .setTitle("Thông báo").setMessage("Bạn muốn xóa thuốc "+dt.getTenThuoc()+" ?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnThemThuoc.setText("Thêm");
                        listDonThuoc.remove(dt.getIdDonThuoc());
                        donThuocAdapter.notifyDataSetChanged();
                        DataLocalManager.setDonThuoc(null);
                        clearInputDonThuoc();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void xuLyThemThuoc() {
        if(checkInputDonThuoc()) {
            DonThuoc dt = DataLocalManager.getDonThuoc();
            String tenThuoc = edtTenThuoc.getText().toString().trim();
            String donViTinh = edtDonViTinh.getText().toString().trim();
            String lieuDung = edtLieuDung.getText().toString().trim();
            int soLuong = Integer.parseInt(edtSoLuongThuoc.getText().toString().trim());
            int donGia = Integer.parseInt(edtDonGia.getText().toString().trim());
            DonThuoc donThuoc = new DonThuoc(tenThuoc, donViTinh, lieuDung, soLuong, donGia);
            if(dt!=null)
            {
                donThuoc.setIdDonThuoc(dt.getIdDonThuoc());
                btnThemThuoc.setText("Thêm");
                listDonThuoc.set(dt.getIdDonThuoc(),donThuoc);
                DataLocalManager.setDonThuoc(null);
            }
            else
            {
                donThuoc.setIdDonThuoc(nextIdDonThuoc);
                nextIdDonThuoc++;
                listDonThuoc.add(donThuoc);
            }
            donThuocAdapter.notifyDataSetChanged();
            btnXoa.setVisibility(View.INVISIBLE);
            clearInputDonThuoc();
        }
    }

    private void clearInputDonThuoc()
    {
        edtTenThuoc.requestFocus();
        edtTenThuoc.setText("");
        edtDonViTinh.setText("");
        edtLieuDung.setText("");
        edtDonGia.setText("");
        edtSoLuongThuoc.setText("");
    }

    private boolean checkInput()
    {
        if(CheckData.isEmpty(edtTrieuChung) && CheckData.isEmpty(edtBenh))
        {
            return true;
        }
        return false;
    }

    private boolean checkInputDonThuoc()
    {
        if(CheckData.isEmpty(edtTenThuoc) && CheckData.isEmpty(edtSoLuongThuoc) &&
                CheckData.isEmpty(edtDonGia) && CheckData.isEmpty(edtLieuDung) && CheckData.isEmpty(edtDonViTinh))
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(donThuocAdapter!=null)
            donThuocAdapter.release();
    }

    private void addControls() {
        edtTrieuChung = findViewById(R.id.edtTrieuChung_DonThuoc);
        edtBenh = findViewById(R.id.edtBenh_DonThuoc);
        edtGhiChu = findViewById(R.id.edtGhiChu_DonThuoc);

        edtTenThuoc = findViewById(R.id.edtTenThuoc);
        edtSoLuongThuoc = findViewById(R.id.edtSoLuongThuoc);
        edtDonViTinh = findViewById(R.id.edtDonViTinh);
        edtLieuDung = findViewById(R.id.edtLieuDung);
        edtDonGia = findViewById(R.id.edtDonGia);
        btnXoa = findViewById(R.id.btnXoaDonThuoc);
        btnXoa.setVisibility(View.GONE);
        btnThemThuoc = findViewById(R.id.btnThemThuoc);
        btnGuiDonThuoc = findViewById(R.id.btnGuiDonThuoc);

        Toolbar toolbar = findViewById(R.id.toolbarNhapDonThuoc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bệnh nhân "+benhNhan.getTenNguoiDung());

        rcvDonThuoc = findViewById(R.id.rcvDonThuoc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NhapDonThuoc.this);
        rcvDonThuoc.setLayoutManager(layoutManager);

        listDonThuoc = new ArrayList<>();
        DataLocalManager.setDonThuoc(null);
        donThuocAdapter = new DonThuocAdapter(listDonThuoc, NhapDonThuoc.this, new DonThuocAdapter.IDonThuocAdapter() {
            @Override
            public void clickItemDonThuoc(DonThuoc donThuoc) {
                edtTenThuoc.setText(donThuoc.getTenThuoc());
                edtSoLuongThuoc.setText(String.valueOf(donThuoc.getSoLuong()));
                edtDonViTinh.setText(donThuoc.getDonViTinh());
                edtLieuDung.setText(donThuoc.getLieuDung());
                edtDonGia.setText(String.valueOf(donThuoc.getDonGia()));
                btnThemThuoc.setText("Cập nhật");
                btnXoa.setVisibility(View.VISIBLE);
                DataLocalManager.setDonThuoc(donThuoc);
            }
        });
        rcvDonThuoc.setAdapter(donThuocAdapter);
    }
}