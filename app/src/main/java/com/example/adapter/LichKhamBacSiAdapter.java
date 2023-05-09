package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.LichKham;
import com.example.model.NguoiDung;
import com.example.onclinic.R;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LichKhamBacSiAdapter extends RecyclerView.Adapter<LichKhamBacSiAdapter.LichKhamBacSiViewHolder>{

    private List<LichKham> lichKhamList;
    Context context;
    private ILichKhamBacSiAdapter onClickListener;
    private NguoiDung benhNhan;

    public interface ILichKhamBacSiAdapter{
        void clickBatDau(LichKham lichKham, NguoiDung nguoiDung);
        void clickHoanThanh(LichKham lichKham);
    }

    public LichKhamBacSiAdapter(List<LichKham> lichKhamList, Context context, ILichKhamBacSiAdapter onClickListener) {
        this.lichKhamList = lichKhamList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LichKhamBacSiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_kham_bac_si,parent,false);
        return new LichKhamBacSiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhamBacSiViewHolder holder, int position) {
        LichKham lichKham = lichKhamList.get(position);
        if(lichKham == null || lichKham.getTrangThai() >= LichKham.KhamXong) return;
        holder.txtThoiGian.setText(lichKham.getGioKham()+" ngày "+lichKham.getNgayKham());
        holder.txtHinhThuc.setText(lichKham.getHinhThucKham());
        if(lichKham.getIdBenhNhan() == null) {
            lichKham.setTrangThai(LichKham.ChuaDatLich);
            holder.btnBatDau.setVisibility(View.GONE);
            holder.txtTrangThai.setText("Chưa được đặt lịch");
            holder.layoutLichKham.setBackgroundColor(Color.WHITE);
        }
        else
        {
            DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().
                    child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).child(lichKham.getIdBenhNhan());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                    benhNhan = nguoiDung;
                    holder.txtTenBN.setText(nguoiDung.getTenNguoiDung());
                    if(nguoiDung.getHinhAnh()!=null) {
                        byte[] decodedString = Base64.decode(nguoiDung.getHinhAnh(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        holder.imgAnhDaiDien.setImageBitmap(decodedByte);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            lichKham.setTrangThai(LichKham.DatLich);
            if(lichKham.getHinhThucKham().equals(LichKham.TrucTiep)) {
                holder.btnBatDau.setVisibility(View.GONE);
            }
            else {
                holder.btnBatDau.setVisibility(View.VISIBLE);
            }
            holder.txtTrangThai.setText("Đã được đặt lịch");
            holder.layoutLichKham.setBackgroundResource(R.drawable.item_lich_kham_da_dat_lich);
        }
        holder.btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.clickBatDau(lichKham, benhNhan);
                holder.btnBatDau.setVisibility(View.GONE);
                holder.btnChuaHoanThanh.setVisibility(View.VISIBLE);
                holder.btnHoanThanh.setVisibility(View.VISIBLE);
            }
        });
        holder.btnChuaHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnBatDau.setVisibility(View.VISIBLE);
                holder.btnChuaHoanThanh.setVisibility(View.GONE);
                holder.btnHoanThanh.setVisibility(View.GONE);
            }
        });
        holder.btnHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.clickHoanThanh(lichKham);
                holder.btnBatDau.setVisibility(View.VISIBLE);
                holder.btnChuaHoanThanh.setVisibility(View.GONE);
                holder.btnHoanThanh.setVisibility(View.GONE);
            }
        });
    }

    public void release()
    {
        context = null;
    }

    @Override
    public int getItemCount() {
        if(lichKhamList!=null)
            return lichKhamList.size();
        return 0;
    }

    public class LichKhamBacSiViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhDaiDien;
        TextView txtTenBN,txtThoiGian,txtHinhThuc,txtTrangThai;
        ConstraintLayout layoutLichKham;
        Button btnBatDau, btnChuaHoanThanh, btnHoanThanh;

        public LichKhamBacSiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhDaiDien = itemView.findViewById(R.id.imgAnhDaiDienItemLKBS);
            txtTenBN = itemView.findViewById(R.id.txtTenBNItemLKBS);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGianItemLKBS);
            txtHinhThuc = itemView.findViewById(R.id.txtHinhThucItemLKBS);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThaiItemLKBS);
            layoutLichKham = itemView.findViewById(R.id.layout_lichkhambs);
            btnBatDau = itemView.findViewById(R.id.btnBatDau);
            btnChuaHoanThanh = itemView.findViewById(R.id.btnChuaHoanThanh);
            btnHoanThanh = itemView.findViewById(R.id.btnHoanThanh);
        }
    }
}