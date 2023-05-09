package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.local_data.DataLocalManager;
import com.example.model.LichKham;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CapNhatSuatKham extends AppCompatActivity {
    TextView txtNgay,txtGio,txtHinhThuc,txtTrangThai;
    Button btnCapNhat,btnXoa;
    LichKham lichKham;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_suat_kham);
        Bundle bundle = getIntent().getExtras();
        if(bundle==null) return;
        lichKham = (LichKham) bundle.get("OBJECT_SUAT_KHAM");
        addControls();
        addEvents();
    }

    private void addEvents() {
        txtNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiCalendar();
            }
        });
        txtGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hienThiClock();
            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyCapNhat();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXoa();
            }
        });
    }

    private void xuLyXoa() {
        AlertDialog alertDialog = new AlertDialog.Builder(CapNhatSuatKham.this)
                .setTitle("Thông báo").setMessage("Bạn muốn xóa suất khám này?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference();
                        myRef.child(NoteFireBase.PHONGKHAM).child(DataLocalManager.getIDPhongKham()).child(NoteFireBase.LICHKHAM).child(lichKham.getIdLichKham()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(CapNhatSuatKham.this,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                troVeManHinhSuatKham();
                            }
                        });
                    }
                })
                .setNegativeButton("Không", null)
                .show();

    }

    private void xuLyCapNhat() {
        try {
            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
            lichKham.setGioKham(txtGio.getText().toString());
            lichKham.setNgayKham(txtNgay.getText().toString());
            myRef.child(DataLocalManager.getIDPhongKham()).child(NoteFireBase.LICHKHAM).child(lichKham.getIdLichKham()).setValue(lichKham, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(CapNhatSuatKham.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                    troVeManHinhSuatKham();
                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(CapNhatSuatKham.this, "Lỗi cập nhật", Toast.LENGTH_LONG).show();
        }
    }

    private void troVeManHinhSuatKham() {
        onBackPressed();
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
        TimePickerDialog dialog = new TimePickerDialog(CapNhatSuatKham.this,
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
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, month);
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
                txtNgay.setText(sdf1.format(calendar.getTime()));
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(CapNhatSuatKham.this,
                android.R.style.Theme_Holo_Light_Dialog,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
        //làm mờ màn hình chính sau khi hiện calendar
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addControls() {
        txtNgay = findViewById(R.id.txtNgayCN);
        txtGio = findViewById(R.id.txtGioCN);
        txtHinhThuc = findViewById(R.id.txtHinhThucCN);
        txtTrangThai = findViewById(R.id.txtTrangThaiCN);
        Toolbar toolbar = findViewById(R.id.toolbarCNSK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(lichKham.getGioKham()+" ngày "+lichKham.getNgayKham());
        btnCapNhat = findViewById(R.id.btnCapNhatSK);
        btnXoa = findViewById(R.id.btnXoaSK);
        txtNgay.setText(sdf1.format(calendar.getTime()));
        txtGio.setText(sdf2.format(calendar.getTime()));
        txtHinhThuc.setText(lichKham.getHinhThucKham());
        if(lichKham.getIdBenhNhan() == null) {
            txtTrangThai.setText("Chưa được đặt lịch");
        }
        else {
            btnXoa.setVisibility(View.INVISIBLE);
            txtTrangThai.setText("Đã được đặt lịch");
        }
    }
}