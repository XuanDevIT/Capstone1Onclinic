package com.example.onclinic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.local_data.DataLocalManager;
import com.example.model.NguoiDung;
import com.example.helper.NoteFireBase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TrangCaNhan extends AppCompatActivity {

    //control trang cá nhân
    private Button btn_DangXuat;
    private TextView txtThuDienTu, txtDiaChi, txtNgaySinh, txt_ten;
    private TextView txt_DoiMK, txt_Doi_ttcn;
    ImageView avatar;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    NguoiDung nguoiDung;
    String idNguoiDung;
    Bitmap selectedBitmap;

    //control của dialog thông tin cá nhân
    Button btnCapNhatTTCN, btnHuyTTCN;
    TextView chonAnh;
    EditText ten, quan_huyen, tinh_tp, mail, ngaysinh;
    ImageView imgAnhCaNhan ;

    //control của dialog đổi mật khẩu
    Button btnCapNhatDMK, btnHuyDMK;
    EditText mk1, mk2, mk3;

    private int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);
        idNguoiDung = DataLocalManager.getIDNguoiDung();
        nguoiDung = DataLocalManager.getNguoiDung();
        role = DataLocalManager.getRole();
        AnhXa();
        addEvents();
    }

    private void addEvents() {
        btn_DangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangXuat();
            }
        });
        //button edit tt cá nhân
        txt_Doi_ttcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditThongTinCaNhan(Gravity.CENTER);
            }
        });
        //button edit mật khẩu
        txt_DoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditMatKhau(Gravity.CENTER);
            }
        });
    }

    private void xuLyDangXuat() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TrangCaNhan.this)
            .setMessage("Bạn muốn đăng xuất?").setPositiveButton("Xác nhận",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(TrangCaNhan.this, LoiChao.class));
                    finishAffinity();
                }
            }).setNegativeButton("Hủy",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void openEditMatKhau(int gravity) {
        final Dialog dialog = openDialog(gravity, R.layout.dialog_thaydoi_matkhau);
        if (dialog == null) return;
        AnhXaCapNhatMatKhau(dialog);
        DatabaseReference myRefBN = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN);
        btnCapNhatDMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mk1.getText().toString())
                        || TextUtils.isEmpty(mk2.getText().toString())
                        || TextUtils.isEmpty(mk3.getText().toString()))
                {
                    Toast.makeText(TrangCaNhan.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (mk2.getText().toString().equals(mk3.getText().toString()))
                    {
                        if (mk1.getText().toString().trim().equals(nguoiDung.getMatKhau()))
                        {
                            String mkmoi = mk2.getText().toString();
                            HashMap map = new HashMap();
                            map.put("matKhau", mkmoi);
                            if(role == 0) {
                                DatabaseReference myRefBN = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN);
                                myRefBN.child(idNguoiDung).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(TrangCaNhan.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                        dialog.dismiss();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(TrangCaNhan.this, LoiChao.class));
                                        finishAffinity();
                                    }
                                });
                            }
                            else if(role == 1){
                                DatabaseReference myRefBS = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI);
                                myRefBS.child(idNguoiDung).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(TrangCaNhan.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                        dialog.dismiss();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(TrangCaNhan.this, LoiChao.class));
                                        finishAffinity();
                                    }
                                });
                            }
                        }
                        else
                        {
                            Toast.makeText(TrangCaNhan.this,"Mật khẩu cũ không đúng!",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(TrangCaNhan.this, "Nhập lại mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnHuyDMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Nullable
    private Dialog openDialog(int gravity, int p) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(p);

        Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        dialog.show();
        return dialog;
    }

    private void openEditThongTinCaNhan(int gravity) {
        final Dialog dialog = openDialog(gravity, R.layout.dialog_thaydoi_thongtin_canhan);
        if (dialog == null) return;
        AnhXaThongTinCaNhan(dialog);
        chonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChonAnh();
            }
        });

        ngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyHienCalendar(ngaysinh);
            }
        });

        btnCapNhatTTCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ten.getText().toString())
                        || TextUtils.isEmpty(quan_huyen.getText().toString())
                        || TextUtils.isEmpty(tinh_tp.getText().toString())
                        || TextUtils.isEmpty(mail.getText().toString())
                ) {
                    Toast.makeText(TrangCaNhan.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                } else {
                    String name = ten.getText().toString();
                    String district = quan_huyen.getText().toString();
                    String city = tinh_tp.getText().toString();
                    String email = mail.getText().toString();
                    String ngaySinh = ngaysinh.getText().toString();

                    HashMap hashMap = new HashMap();
                    hashMap.put("tenNguoiDung", name);
                    hashMap.put("email_sdt", email);
                    hashMap.put("quan", district);
                    hashMap.put("thanhpho", city);
                    hashMap.put("ngaySinh",ngaySinh);
                    if(selectedBitmap!=null)
                    {
                        //đưa ảnh về kiểu chuỗi base64
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String imgEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        hashMap.put("hinhAnh",imgEncoded);
                    }

                    //kiểm tra vai trò đã lưu ở đăng nhập
                    if(role == 0)
                    {
                        DatabaseReference myRefBN = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN);
                        myRefBN.child(nguoiDung.getUserID()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(TrangCaNhan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });
                    }
                    else if(role == 1){
                        DatabaseReference myRefBS = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI);
                        myRefBS.child(nguoiDung.getUserID()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(TrangCaNhan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });
                    }
                }
            }
        });

        btnHuyTTCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void xuLyChonAnh() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(TrangCaNhan.this);
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
                    selectedBitmap = MediaStore.Images.Media.getBitmap(TrangCaNhan.this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgAnhCaNhan.setImageBitmap(selectedBitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void xuLyHienCalendar(EditText ngaysinh) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(calendar.YEAR,year);
                calendar.set(calendar.MONTH,month);
                calendar.set(calendar.DAY_OF_MONTH,dayOfMonth);
                ngaysinh.setText(sdf1.format(calendar.getTime()));
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(TrangCaNhan.this,
                android.R.style.Theme_Holo_Light_Dialog,
                dateSetListener,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
        //làm mờ màn hình chính sau khi hiện calendar
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void AnhXaThongTinCaNhan(Dialog dialog) {
        btnCapNhatTTCN = dialog.findViewById(R.id.btnCapNhatTTCN);
        btnHuyTTCN = dialog.findViewById(R.id.btnHuyTTCN);
        ten = dialog.findViewById(R.id.edt_update_ten);
        quan_huyen = dialog.findViewById(R.id.edt_update_dia_chi_quan);
        tinh_tp = dialog.findViewById(R.id.edt_update_dia_chi_tp);
        mail = dialog.findViewById(R.id.edt_update_mail);
        ngaysinh = dialog.findViewById(R.id.edt_update_ngaysinh);
        chonAnh = dialog.findViewById(R.id.txtChonAnh);
        imgAnhCaNhan = dialog.findViewById(R.id.imgAnhCaNhan);

        ten.setText(nguoiDung.getTenNguoiDung());
        quan_huyen.setText(nguoiDung.getQuan());
        tinh_tp.setText(nguoiDung.getThanhpho());
        mail.setText(nguoiDung.getEmail_sdt());
        ngaysinh.setText(nguoiDung.getNgaySinh());

        if(nguoiDung.getHinhAnh()!=null) {
            byte[] decodedString = Base64.decode(nguoiDung.getHinhAnh(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgAnhCaNhan.setImageBitmap(decodedByte);
        }
    }

    private void AnhXaCapNhatMatKhau(Dialog dialog) {
        btnCapNhatDMK = dialog.findViewById(R.id.btnCapNhatTDMK);
        btnHuyDMK = dialog.findViewById(R.id.btnHuyTDMK);
        mk1 = dialog.findViewById(R.id.edt_thay_doi_mk1);
        mk2 = dialog.findViewById(R.id.edt_thay_doi_mk2);
        mk3 = dialog.findViewById(R.id.edt_thay_doi_mk3);
    }

    private void AnhXa(){
        txt_ten = findViewById(R.id.txt_ten);
        txt_ten.setText(nguoiDung.getTenNguoiDung());
        txtThuDienTu = findViewById(R.id.txt_Mail);
        txtThuDienTu.setText("Thư điện tử: "+nguoiDung.getEmail_sdt());
        txtDiaChi = findViewById(R.id.txt_address);
        txtDiaChi.setText("Địa chỉ: "+ nguoiDung.getQuan() +", "+ nguoiDung.getThanhpho());
        txtNgaySinh = findViewById(R.id.txt_ngaysinh);
        txtNgaySinh.setText("Ngày sinh: "+ nguoiDung.getNgaySinh());
        avatar = findViewById(R.id.Avatar);
        if(nguoiDung.getHinhAnh()!=null) {
            byte[] decodedString = Base64.decode(nguoiDung.getHinhAnh(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            avatar.setImageBitmap(decodedByte);
        }

        txt_DoiMK = findViewById(R.id.txt_DoiMK);
        txt_Doi_ttcn = findViewById(R.id.txt_Doi_ttcn);
        btn_DangXuat = findViewById(R.id.btn_DangXuat);
    }
}
