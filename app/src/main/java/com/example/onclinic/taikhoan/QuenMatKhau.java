package com.example.onclinic.taikhoan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onclinic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class QuenMatKhau extends AppCompatActivity {

    private EditText edtEmailHoacSdt;
    private Button btnTiepTuc;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);

        edtEmailHoacSdt = findViewById(R.id.edtEmailHoacSdt);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        progressDialog = new ProgressDialog(this);

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = edtEmailHoacSdt.getText().toString();
                progressDialog.show();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(QuenMatKhau.this, "Đã gửi email thay đổi về email của bạn.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(QuenMatKhau.this, DangNhap.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }
}