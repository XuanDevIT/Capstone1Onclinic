package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.DonThuoc;
import com.example.model.LichSu;
import com.example.model.NguoiDung;
import com.example.onclinic.R;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LichSuBenhNhanAdapter extends RecyclerView.Adapter<LichSuBenhNhanAdapter.LichKhamViewHolder>{

    private List<LichSu> lichSuList;
    Context context;
    private ILichSuBenhNhanAdapter onClickListener;

    public interface ILichSuBenhNhanAdapter{
        void clickChiTiet(LichSu lichSu);
    }

    public LichSuBenhNhanAdapter(List<LichSu> lichSuList, Context context, ILichSuBenhNhanAdapter onClickListener) {
        this.lichSuList = lichSuList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LichKhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_su_benh_nhan,parent,false);
        return new LichKhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhamViewHolder holder, int position) {
        LichSu lichSu = lichSuList.get(position);
        if(lichSu == null) return;
        holder.txtNgayKham.setText("Ngày khám: "+lichSu.getLichKham().getNgayKham());
        holder.txtTenPhongKham.setText("Phòng khám "+lichSu.getPhongKham().getTenPhongKham());
        holder.txtDiaChi.setText(lichSu.getPhongKham().getDiaChi());
        holder.txtHinhThucKham.setText(lichSu.getLichKham().getHinhThucKham());
        String idBacSi = lichSu.getPhongKham().getIdBacSi();
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference()
                .child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BACSI).child(idBacSi);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                holder.txtTenBacSi.setText(nguoiDung.getTenNguoiDung());
                holder.txtEmailBacSi.setText(nguoiDung.getEmail_sdt());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.clickChiTiet(lichSu);
            }
        });
        List<DonThuoc> donThuocList = lichSu.getDonThuocList();
        int tien = 0;
        if(donThuocList == null)
            holder.txtTongTien.setText(String.valueOf(tien)+" VNĐ");
        else{
            for(DonThuoc donThuoc: donThuocList)
            {
                tien += donThuoc.getDonGia()*donThuoc.getSoLuong();
            }
            holder.txtTongTien.setText(String.valueOf(tien)+" VNĐ");
        }
    }

    @Override
    public int getItemCount() {
        if(lichSuList!=null)
            return lichSuList.size();
        return 0;
    }

    public void release()
    {
        context = null;
    }

    public class LichKhamViewHolder extends RecyclerView.ViewHolder{

        TextView txtNgayKham, txtTenPhongKham, txtDiaChi, txtTenBacSi, txtEmailBacSi, txtHinhThucKham, txtTongTien;
        TextView txtChiTiet;

        public LichKhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNgayKham = itemView.findViewById(R.id.txtNgayKhamItemLichSuBN);
            txtTenPhongKham = itemView.findViewById(R.id.txtTenPhongKhamItemLichSuBN);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChiItemLichSuBN);
            txtTenBacSi = itemView.findViewById(R.id.txtBacSiItemLichSuBN);
            txtEmailBacSi = itemView.findViewById(R.id.txtEmailItemLichSuBN);
            txtHinhThucKham = itemView.findViewById(R.id.txtHinhThucItemLichSuBN);
            txtTongTien = itemView.findViewById(R.id.txtTongTienItemLichSuBN);
            txtChiTiet = itemView.findViewById(R.id.txtChiTietItemLichSuBN);
        }
    }
}
