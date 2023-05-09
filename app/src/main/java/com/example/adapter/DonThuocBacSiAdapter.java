package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.LichKham;
import com.example.model.LichSu;
import com.example.model.NguoiDung;
import com.example.onclinic.NhapDonThuoc;
import com.example.onclinic.R;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DonThuocBacSiAdapter extends RecyclerView.Adapter<DonThuocBacSiAdapter.LichKhamBacSiViewHolder>{

    private List<LichKham> lichKhamList;
    Context context;
    private NguoiDung benhNhan;
    private LichSu lichSu;

    public DonThuocBacSiAdapter(List<LichKham> lichKhamList, Context context) {
        this.lichKhamList = lichKhamList;
        this.context = context;
    }

    @NonNull
    @Override
    public LichKhamBacSiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_thuoc_bac_si,parent,false);
        return new LichKhamBacSiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichKhamBacSiViewHolder holder, int position) {
        LichKham lichKham = lichKhamList.get(position);
        if(lichKhamList == null || lichKham.getTrangThai() < LichKham.KhamXong) return;
        holder.txtThoiGian.setText(lichKham.getGioKham()+" ngày "+lichKham.getNgayKham());
        holder.txtHinhThuc.setText(lichKham.getHinhThucKham());
        if(lichKham.getTrangThai() == LichKham.KhamXong)
            holder.layoutLichKham.setBackgroundColor(Color.WHITE);
        else holder.layoutLichKham.setBackgroundColor(Color.CYAN);
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

        DatabaseReference refLichSu = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference().child(NoteFireBase.LICHSU);
        refLichSu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    LichSu ls = data.getValue(LichSu.class);
                    if(ls.getLichKham().getIdLichKham().equals(lichKham.getIdLichKham())) {
                        lichSu = ls;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.layoutLichKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NhapDonThuoc.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("LICH_KHAM_BAC_SI",lichKham);
                bundle.putSerializable("BENH_NHAN",benhNhan);
                bundle.putSerializable("LICH_SU_DON_THUOC",lichSu);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(lichKhamList!=null)
            return lichKhamList.size();
        return 0;
    }

    public void release()
    {
        context = null;
    }

    public class LichKhamBacSiViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhDaiDien;
        TextView txtTenBN,txtThoiGian,txtHinhThuc;
        ConstraintLayout layoutLichKham;

        public LichKhamBacSiViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhDaiDien = itemView.findViewById(R.id.imgAnhDaiDienItemDTBS);
            txtTenBN = itemView.findViewById(R.id.txtTenBNItemDTBS);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGianItemDTBS);
            txtHinhThuc = itemView.findViewById(R.id.txtHinhThucItemDTBS);
            layoutLichKham = itemView.findViewById(R.id.layout_donthuocbs);
        }
    }
}
