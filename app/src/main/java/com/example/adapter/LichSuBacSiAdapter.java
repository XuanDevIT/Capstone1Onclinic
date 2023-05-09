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

public class LichSuBacSiAdapter extends RecyclerView.Adapter<LichSuBacSiAdapter.LichKhamViewHolder>{

    private List<LichSu> lichSuList;
    Context context;
    private ILichSuBacSiAdapter onClickListener;

    public interface ILichSuBacSiAdapter{
        void clickChiTiet(LichSu lichSu);
    }

    public LichSuBacSiAdapter(List<LichSu> lichSuList, Context context, ILichSuBacSiAdapter onClickListener) {
        this.lichSuList = lichSuList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LichKhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lich_su_bac_si,parent,false);
        return new LichKhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhamViewHolder holder, int position) {
        LichSu lichSu = lichSuList.get(position);
        if(lichSu == null) return;
        holder.txtNgayKham.setText("Ngày khám: "+lichSu.getLichKham().getNgayKham());
        holder.txtHinhThucKham.setText(lichSu.getLichKham().getHinhThucKham());
        String idBenhNhan = lichSu.getLichKham().getIdBenhNhan();
        DatabaseReference myRef = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference()
                .child(NoteFireBase.NGUOIDUNG).child(NoteFireBase.BENHNHAN).child(idBenhNhan);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                holder.txtTenBenhNhan.setText("Bệnh nhân "+nguoiDung.getTenNguoiDung());
                holder.txtDiaChi.setText(nguoiDung.getQuan()+" - "+nguoiDung.getThanhpho());
                holder.txtEmailBenhNhan.setText(nguoiDung.getEmail_sdt());
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

    public class LichKhamViewHolder extends RecyclerView.ViewHolder {

        TextView txtNgayKham, txtTenBenhNhan, txtDiaChi, txtEmailBenhNhan, txtHinhThucKham, txtTongTien;
        TextView txtChiTiet;

        public LichKhamViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNgayKham = itemView.findViewById(R.id.txtNgayKhamItemLichSuBS);
            txtTenBenhNhan = itemView.findViewById(R.id.txtTenBenhNhanItemLichSuBS);
            txtDiaChi = itemView.findViewById(R.id.txtDiaChiItemLichSuBS);
            txtEmailBenhNhan = itemView.findViewById(R.id.txtEmailItemLichSuBS);
            txtHinhThucKham = itemView.findViewById(R.id.txtHinhThucItemLichSuBS);
            txtTongTien = itemView.findViewById(R.id.txtTongTienItemLichSuBS);
            txtChiTiet = itemView.findViewById(R.id.txtChiTietItemLichSuBS);
        }
    }
}
