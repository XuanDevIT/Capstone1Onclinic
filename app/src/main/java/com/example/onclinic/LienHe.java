package com.example.onclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.local_data.DataLocalManager;

public class LienHe extends MyBaseActivity {

    TextView txtDoiTac,txtLoiPhatSinh, txtPhatHienViPham, txtViTri, txtEmail, txtFb, txtLoi;

    EditText edtFeedBack;
    Button btnGui, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_lien_he,null,false);
        mDrawerLayout.addView(view,0);
        //setContentView(R.layout.activity_lien_he);

        AnhXa();
        XuLy();
    }

    private void XuLy() {
        txtLoiPhatSinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFeedBackDialog(Gravity.CENTER);
            }
        });

        txtPhatHienViPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhatHienViPham(Gravity.CENTER);
            }
        });
        txtDoiTac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LienHe.this,TaoPhongKham.class);
                startActivity(intent);
            }
        });
    }

    private void openFeedBackDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        } else{
            dialog.setCancelable(false);
        }

        edtFeedBack =dialog.findViewById(R.id.edtFeedBack);
        btnGui = dialog.findViewById(R.id.btnGui);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LienHe.this, "Gửi thông tin thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openPhatHienViPham(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_feedback);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        } else{
            dialog.setCancelable(false);
        }

        edtFeedBack =dialog.findViewById(R.id.edtFeedBack);
        btnGui = dialog.findViewById(R.id.btnGui);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        txtLoi = dialog.findViewById(R.id.txtLoi);

        txtLoi.setText("Phát hiện vi phạm");
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LienHe.this, "Gửi thông tin thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void AnhXa() {
        txtLoiPhatSinh = findViewById(R.id.txtLoiPhatSinh);
        txtPhatHienViPham = findViewById(R.id.txtPhatHienViPham);
        txtViTri = findViewById(R.id.txtViTri);
        txtEmail = findViewById(R.id.txtEmail);
        txtFb = findViewById(R.id.txtFb);
        txtDoiTac = findViewById(R.id.txtDKLamDoiTac);
        if(DataLocalManager.getRole() == 1)
            txtDoiTac.setVisibility(View.GONE);
        else txtDoiTac.setVisibility(View.VISIBLE);
    }
}