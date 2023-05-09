package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.LichKham;
import com.example.onclinic.CapNhatSuatKham;
import com.example.onclinic.QuanLyPhongKham;
import com.example.onclinic.R;

import java.util.List;

public class SuatKhamAdapter extends RecyclerView.Adapter<SuatKhamAdapter.SuatKhamViewHolder>{

    private List<LichKham> lichKhamList;
    Context context;

    public SuatKhamAdapter(List<LichKham> lichKhamList, Context context) {
        this.lichKhamList = lichKhamList;
        this.context = context;
    }

    @NonNull
    @Override
    public SuatKhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suat_kham,parent,false);
        return new SuatKhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuatKhamViewHolder holder, int position) {
        LichKham lichKham = lichKhamList.get(position);
        if(lichKham == null || lichKham.getTrangThai() >= LichKham.KhamXong) return;
        holder.txtThoiGian.setText(lichKham.getGioKham()+" ngày "+lichKham.getNgayKham());
        holder.txtHinhThuc.setText(lichKham.getHinhThucKham());
        if(lichKham.getIdBenhNhan() == null) {
            lichKham.setTrangThai(LichKham.ChuaDatLich);
            holder.txtTrangThai.setText("Chưa được đặt lịch");
        }
        else {
            lichKham.setTrangThai(LichKham.DatLich);
            holder.txtTrangThai.setText("Đã được đặt lịch");
        }
        holder.layoutSuatKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyClickItemSuatKham(lichKham);
            }
        });
    }

    private void xuLyClickItemSuatKham(LichKham lichKham) {
        Intent intent = new Intent(context, CapNhatSuatKham.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("OBJECT_SUAT_KHAM",lichKham);
        intent.putExtras(bundle);
        context.startActivity(intent);
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

    public class SuatKhamViewHolder extends RecyclerView.ViewHolder{
        TextView txtThoiGian,txtHinhThuc,txtTrangThai;
        ConstraintLayout layoutSuatKham;

        public SuatKhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txtThoiGian = itemView.findViewById(R.id.txtThoigianSK);
            txtHinhThuc = itemView.findViewById(R.id.txtHinhThucSK);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThaiSK);
            layoutSuatKham = itemView.findViewById(R.id.layout_suatkham);
        }
    }
}
