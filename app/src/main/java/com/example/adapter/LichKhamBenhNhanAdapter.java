package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.LichKham;
import com.example.model.PhongKham;
import com.example.onclinic.R;

import java.util.List;

public class LichKhamBenhNhanAdapter extends RecyclerView.Adapter<LichKhamBenhNhanAdapter.LichKhamBenhNhanViewHolder> {

    private List<LichKham> lichKhamList;
    private List<PhongKham> listPhongKham;
    Context context;
    private ILichKhamBenhNhanAdapter onClickListener;

    public interface ILichKhamBenhNhanAdapter{
        void clickThamGia(LichKham lichKham, PhongKham phongKham);
    }

    public LichKhamBenhNhanAdapter(List<LichKham> lichKhamList, List<PhongKham> phongKhamList, Context context, ILichKhamBenhNhanAdapter onClickListener) {
        this.lichKhamList = lichKhamList;
        this.listPhongKham = phongKhamList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LichKhamBenhNhanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_kham_benh_nhan,parent,false);
        return new LichKhamBenhNhanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhamBenhNhanViewHolder holder, int position) {
        PhongKham phongKham = listPhongKham.get(position);
        LichKham lichKham = lichKhamList.get(position);
        if(lichKham == null || lichKham.getTrangThai() >= LichKham.KhamXong) return;
        holder.txtThoiGian.setText("Thời gian: "+lichKham.getGioKham()+" ngày "+lichKham.getNgayKham());
        holder.txtHinhThuc.setText("Hình thức: "+lichKham.getHinhThucKham());
        holder.txtTenPhongKham.setText("Phòng khám: "+phongKham.getTenPhongKham());
        if(phongKham.getHinhAnh()!=null)
        {
            byte[] decodedString = Base64.decode(phongKham.getHinhAnh(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgPhongKham.setImageBitmap(decodedByte);
        }

        if(lichKham.getHinhThucKham().equals(LichKham.TrucTiep)) {
            holder.btnThamGia.setVisibility(View.GONE);
        }
        else {
            holder.btnThamGia.setVisibility(View.VISIBLE);
        }

        holder.btnThamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.clickThamGia(lichKham, phongKham);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(lichKhamList != null)
            return lichKhamList.size();
        return 0;
    }

    public void release()
    {
        context = null;
    }

    public class LichKhamBenhNhanViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhongKham;
        TextView txtTenPhongKham,txtThoiGian,txtHinhThuc;
        Button btnThamGia;

        public LichKhamBenhNhanViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhongKham = itemView.findViewById(R.id.imgAnhPhongKhamItemLKBN);
            txtTenPhongKham = itemView.findViewById(R.id.txtTenPhongKhamItemLKBN);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGianItemLKBN);
            txtHinhThuc = itemView.findViewById(R.id.txtHinhThucItemLKBN);
            btnThamGia = itemView.findViewById(R.id.btnThamGia);
        }
    }
}
