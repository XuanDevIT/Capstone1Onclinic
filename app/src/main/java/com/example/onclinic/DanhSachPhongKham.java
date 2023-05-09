package com.example.onclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adapter.PhongKhamAdapter;
import com.example.helper.NoteFireBase;
import com.example.local_data.DataLocalManager;
import com.example.model.PhongKham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DanhSachPhongKham extends AppCompatActivity {

    RecyclerView rcvDSPhongKham;
    private PhongKhamAdapter phongKhamAdapter;
    private List<PhongKham> phongKhamList;

    private SearchView searchView;
    Button btnXemThongTin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_phong_kham);
        addControls();
        layDanhSachPhongKhamTuFireBase();
    }

    private void layDanhSachPhongKhamTuFireBase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.PHONGKHAM);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phongKhamList.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    PhongKham phongKham = data.getValue(PhongKham.class);
                    phongKhamList.add(phongKham);
                }
                phongKhamAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DanhSachPhongKham.this,"Lỗi đọc dữ liệu",Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if(phongKhamAdapter!= null)
            phongKhamAdapter.release();
    }

    private void xuLyXemThongTin(PhongKham phongKham) {
        Intent intent = new Intent(DanhSachPhongKham.this, ThongTinPhongKhamViewBenhNhan.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("OBJECT_PHONG_KHAM2",phongKham);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void addControls() {

        rcvDSPhongKham = findViewById(R.id.rcvDSPhongKham);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DanhSachPhongKham.this);
        rcvDSPhongKham.setLayoutManager(layoutManager);

        phongKhamList = new ArrayList<>();
        phongKhamAdapter = new PhongKhamAdapter(phongKhamList, DanhSachPhongKham.this, new PhongKhamAdapter.IPhongKhamAdapter() {
            @Override
            public void clickPhongKham(PhongKham phongKham) {
                btnXemThongTin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xuLyXemThongTin(phongKham);
                    }
                });
            }
        });

        rcvDSPhongKham.setAdapter(phongKhamAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvDSPhongKham.addItemDecoration(itemDecoration);

        btnXemThongTin = findViewById(R.id.btnXemThongTin);

        searchView = findViewById(R.id.search_bar);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                phongKhamAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                phongKhamAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}