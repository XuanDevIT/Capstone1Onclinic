package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.local_data.DataLocalManager;
import com.example.model.NguoiDung;
import com.example.helper.ActivityState;
import com.example.helper.NoteFireBase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MyBaseActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    ImageView imgAnhDaiDien;
    TextView txtTenNguoiDung,txtEmail;
    NguoiDung nguoiDung;
    String idNguoiDung;

    Button btnCapNhatDMK, btnHuyDMK;
    EditText mk1, mk2, mk3;

    private int currentActivity;
    private int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_base);
        nguoiDung = DataLocalManager.getNguoiDung();
        idNguoiDung = DataLocalManager.getIDNguoiDung();

        currentActivity = DataLocalManager.getActivityNumber();

        role = DataLocalManager.getRole();
        addControls();
        addEvents();
    }

    private void addEvents() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_trangchu)
                {
                    if(currentActivity!= ActivityState.ACTIVITY_TRANGCHU)
                    {
                        if(role == 0) {
                            DataLocalManager.setActivityNumber(ActivityState.ACTIVITY_TRANGCHU);
                            Intent intent = new Intent(MyBaseActivity.this, TrangChuBenhNhan.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else if(role == 1)
                        {
                            DataLocalManager.setActivityNumber(ActivityState.ACTIVITY_TRANGCHU);
                            Intent intent = new Intent(MyBaseActivity.this, TrangChuBacSi.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
                else if(id == R.id.nav_thongbao)
                {
                    if(currentActivity!=ActivityState.ACTIVITY_THONGBAO)
                    {
                        DataLocalManager.setActivityNumber(ActivityState.ACTIVITY_THONGBAO);
                        Intent intent = new Intent(getApplicationContext(),ThongBaoActivity.class);
                        startActivity(intent);
                    }
                }
                else if(id == R.id.nav_thongtincanhan)
                {
                    if(currentActivity!=ActivityState.ACTIVITY_THONGTINCANHAN)
                    {
                        DataLocalManager.setActivityNumber(ActivityState.ACTIVITY_THONGTINCANHAN);
                        Intent intent = new Intent(getApplicationContext(),TrangCaNhan.class);
                        startActivity(intent);
                    }
                }
                else if(id == R.id.nav_doimatkhau)
                {
                    final Dialog dialog = openDialog(Gravity.CENTER, R.layout.dialog_thaydoi_matkhau);
                    addControlsNhatMatKhau(dialog);
                    DatabaseReference myRefBN = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN);
                    btnCapNhatDMK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(mk1.getText().toString())
                                    || TextUtils.isEmpty(mk2.getText().toString())
                                    || TextUtils.isEmpty(mk3.getText().toString()))
                            {
                                Toast.makeText(MyBaseActivity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(MyBaseActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                                    onBackPressed();
                                                    dialog.dismiss();
                                                    FirebaseAuth.getInstance().signOut();
                                                    startActivity(new Intent(MyBaseActivity.this, LoiChao.class));
                                                    finish();
                                                }
                                            });
                                        }
                                        else if(role == 1){
                                            DatabaseReference myRefBS = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI);
                                            myRefBS.child(idNguoiDung).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(MyBaseActivity.this, "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                                    onBackPressed();
                                                    dialog.dismiss();
                                                    FirebaseAuth.getInstance().signOut();
                                                    startActivity(new Intent(MyBaseActivity.this, LoiChao.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(MyBaseActivity.this,"Mật khẩu cũ không đúng!",Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(MyBaseActivity.this, "Nhập lại mật khẩu mới!", Toast.LENGTH_SHORT).show();
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
                else if(id == R.id.nav_dangxuat)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MyBaseActivity.this)
                            .setTitle("Thông báo").setMessage("Bạn muốn đăng xuất?")
                            .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(),LoiChao.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

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

    private void addControlsNhatMatKhau(Dialog dialog) {
        btnCapNhatDMK = dialog.findViewById(R.id.btnCapNhatTDMK);
        btnHuyDMK = dialog.findViewById(R.id.btnHuyTDMK);
        mk1 = dialog.findViewById(R.id.edt_thay_doi_mk1);
        mk2 = dialog.findViewById(R.id.edt_thay_doi_mk2);
        mk3 = dialog.findViewById(R.id.edt_thay_doi_mk3);
    }

    private void addControls() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(nguoiDung!=null)
        {
            imgAnhDaiDien = navigationView.getHeaderView(0).findViewById(R.id.imgLayoutNavBN);
            if(nguoiDung.getHinhAnh()!=null) {
                byte[] decodedString = Base64.decode(nguoiDung.getHinhAnh(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgAnhDaiDien.setImageBitmap(decodedByte);
            }
            txtTenNguoiDung = navigationView.getHeaderView(0).findViewById(R.id.txtLayoutTenBN);
            txtTenNguoiDung.setText("Xin chào "+nguoiDung.getTenNguoiDung());
            txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtLayoutEmailBN);
            txtEmail.setText(nguoiDung.getEmail_sdt());
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}