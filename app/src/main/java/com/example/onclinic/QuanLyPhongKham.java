package com.example.onclinic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.local_data.DataLocalManager;
import com.example.model.DanhGia;
import com.example.model.LichKham;
import com.example.model.PhongKham;
import com.example.helper.NgayGio;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class QuanLyPhongKham extends AppCompatActivity {

    ImageView imgPhongKham, imgNgay, imgGio;
    ConstraintLayout layoutPhongKham;
    TextView txtTenPhongKham, txtChuyenKhoa, txtNgay, txtGio;
    Button btnTaoSuatKham, btnSuatKhamDaTao, btnLichKhamSapToi;
    RatingBar ratingBar;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

    String idNguoiDung, idPhongKham;

    ArrayList<String> dsNgay = new ArrayList<>();
    ArrayList<Date> dsNgayDangDate = new ArrayList<>();
    ArrayList<LichKham> dsLichKham = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_phong_kham);
        idNguoiDung = DataLocalManager.getIDNguoiDung();
        idPhongKham = DataLocalManager.getIDPhongKham();
        addControls();
        docDuLieuFireBase();
        addEvents();
    }

    private void docDuLieuFireBase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PhongKham phong = dataSnapshot.getValue(PhongKham.class);
                    if(idNguoiDung.equals(phong.getIdBacSi()))
                    {
                        DataLocalManager.setIDPhongKham(dataSnapshot.getKey());//lưu id vào data local để sử dụng về sau
                        DataLocalManager.setPhongKham(phong);
                        txtTenPhongKham.setText(phong.getTenPhongKham());
                        txtChuyenKhoa.setText(phong.getChuyenKhoa());
                        if(phong.getHinhAnh()!=null) {
                            byte[] decodedString = Base64.decode(phong.getHinhAnh(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imgPhongKham.setImageBitmap(decodedByte);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuanLyPhongKham.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });

        DatabaseReference refTBDanhGia = myRef.child(idPhongKham).child(NoteFireBase.DANHGIA);
        refTBDanhGia.addValueEventListener(new ValueEventListener() {
            float tongDanhGia = 0;
            int listSize = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    DanhGia danhGia = data.getValue(DanhGia.class);
                    tongDanhGia +=  danhGia.getRating();
                    listSize++;
                }
                if(listSize!=0) ratingBar.setRating(tongDanhGia/listSize);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ref = myRef.child(idPhongKham).child(NoteFireBase.LICHKHAM);
        //thêm ngày giờ trên firebase vào danh sách để kiểm tra
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsNgay.clear();dsLichKham.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    LichKham lichKham = data.getValue(LichKham.class);
                    if(!dsNgay.contains(lichKham.getNgayKham())) {
                        dsNgay.add(lichKham.getNgayKham());
                    }
                    dsLichKham.add(lichKham);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuanLyPhongKham.this, "Lỗi đọc dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
        for(int i = 0;i<dsNgay.size();i++)
        {
            try {
                Date date = NgayGio.ConvertStringToDate(dsNgay.get(i));
                dsNgayDangDate.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void addEvents() {
        imgNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiCalendar();
            }
        });
        imgGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiClock();
            }
        });
        btnTaoSuatKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTaoSuatKham();
            }
        });
        btnSuatKhamDaTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySuatKhamDaTao();
            }
        });
        btnLichKhamSapToi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLichKhamSapToi();
            }
        });

        layoutPhongKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyThongTinPhongKham();
            }
        });
    }

    private void xuLyThongTinPhongKham() {
        Intent intent = new Intent(QuanLyPhongKham.this, ThongTinPhongKham.class);
        startActivity(intent);
    }

    private void xuLyLichKhamSapToi() {
        Intent intent = new Intent(QuanLyPhongKham.this, LichKhamBacSi.class);
        startActivity(intent);
    }

    private void xuLySuatKhamDaTao() {
        Intent intent = new Intent(QuanLyPhongKham.this, SuatKhamDaTao.class);
        startActivity(intent);
    }

    private void xuLyTaoSuatKham() {
        if(kiemTraNgayGio()) {
            try {
                DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
                String idPhongKham = DataLocalManager.getIDPhongKham();//đã lấy từ hàm đọc dữ liệu firebase
                String keyLichKham = myRef.child(NoteFireBase.PHONGKHAM).child(idPhongKham).child(NoteFireBase.LICHKHAM).push().getKey();//lưu key để chỉnh sửa lịch khám về sau
                String ngay = txtNgay.getText().toString().trim();
                String gio = txtGio.getText().toString().trim();
                LichKham lichKham = new LichKham(ngay, gio);
                lichKham.setIdLichKham(keyLichKham);
                lichKham.setTrangThai(LichKham.ChuaDatLich);
                myRef.child(NoteFireBase.PHONGKHAM).child(idPhongKham).child(NoteFireBase.LICHKHAM).child(keyLichKham).setValue(lichKham, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(QuanLyPhongKham.this, "Tạo 1 lịch khám thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
                Toast.makeText(QuanLyPhongKham.this, "Lỗi tạo lịch khám", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean kiemTraNgayGio()
    {
        try{
            String ngay = txtNgay.getText().toString().trim();
            Date ngayDangChon = NgayGio.ConvertStringToDate(ngay);
            Date ngayHienTai = NgayGio.GetDateCurrent();

            String gio = txtGio.getText().toString().trim();
            Date gioDangChon = NgayGio.ConvertStringToTime(gio);
            Date gioHienTai = NgayGio.GetTimeCurrent();

            long hieu2GioDCvaHT = Math.abs(gioDangChon.getTime()-gioHienTai.getTime());//tính theo milisecond (1 giây = 1000 mili giây)
            if(ngayDangChon.getTime()>=ngayHienTai.getTime()) {

                if (ngayDangChon.getTime() == ngayHienTai.getTime()) {
                    if (hieu2GioDCvaHT >= 300000)//300000ms = 300s = 5p
                    {
                        return kiemTraNgayGio(ngayDangChon, gioDangChon);
                    }
                    else{
                        Toast.makeText(QuanLyPhongKham.this, "Nên tạo suất sau " + NgayGio.ConvertDateToString(ngayHienTai) + " " + NgayGio.ConvertTimeToString(gioHienTai)+" 5 phút"  , Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                else if(ngayDangChon.getTime()>ngayHienTai.getTime())
                {
                    return kiemTraNgayGio(ngayDangChon, gioDangChon);
                }
            }
            else{
                Toast.makeText(QuanLyPhongKham.this, "Không thể tạo suất khám trước ngày hiện tại", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean kiemTraNgayGio(Date ngayDangChon, Date gioDangChon) throws ParseException {
        for (LichKham lich : dsLichKham) {
            Date ngayFB = NgayGio.ConvertStringToDate(lich.getNgayKham());
            if (ngayFB.getTime() == ngayDangChon.getTime()) {
                Date gioFB = NgayGio.ConvertStringToTime(lich.getGioKham());
                //nếu time giữa giờ đã đặt và hiện tại < 15p
                if (Math.abs(gioFB.getTime() - gioDangChon.getTime()) < 900000) {
                    Toast.makeText(QuanLyPhongKham.this, "Có suất khám khác gần thời điểm này", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void hienThiClock() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                txtGio.setText(sdf2.format(calendar.getTime()));
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(QuanLyPhongKham.this,
                android.R.style.Theme_Holo_Light_Dialog,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void hienThiCalendar() {
        //bắt sự kiện khi click trên calendar
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(calendar.YEAR,year);
                calendar.set(calendar.MONTH,month);
                calendar.set(calendar.DAY_OF_MONTH,dayOfMonth);
                txtNgay.setText(sdf1.format(calendar.getTime()));
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(QuanLyPhongKham.this,
                android.R.style.Theme_Holo_Light_Dialog,
                dateSetListener,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
        //làm mờ màn hình chính sau khi hiện calendar
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addControls() {
        imgPhongKham = findViewById(R.id.imgPhongKham);
        imgNgay = findViewById(R.id.imgDate);
        imgGio = findViewById(R.id.imgClock);
        txtTenPhongKham = findViewById(R.id.txtTenPhongKham);
        txtChuyenKhoa = findViewById(R.id.txtChuyenKhoa);
        ratingBar = findViewById(R.id.ratingBarQLPK);
        txtNgay = findViewById(R.id.edtChonNgay);
        txtNgay.setText(NgayGio.GetDateCurrentString());
        txtGio = findViewById(R.id.edtChonGio);
        txtGio.setText(NgayGio.GetTimeCurrentString());
        btnTaoSuatKham = findViewById(R.id.btnTaoSuatKham);
        btnSuatKhamDaTao = findViewById(R.id.btnSuatKhamDaTao);
        btnLichKhamSapToi = findViewById(R.id.btnLichKhamSapToi);
        layoutPhongKham = findViewById(R.id.LayoutPhongKham);
    }
}
