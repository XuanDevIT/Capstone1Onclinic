package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.local_data.DataLocalManager;
import com.example.model.DonThuoc;
import com.example.onclinic.R;

import java.util.List;

public class DonThuocAdapter extends RecyclerView.Adapter<DonThuocAdapter.DonThuocViewHolder> {

    private List<DonThuoc> donThuocList;
    Context context;
    private IDonThuocAdapter onClickListener;
    public interface IDonThuocAdapter
    {
        void clickItemDonThuoc(DonThuoc donThuoc);
    }

    public DonThuocAdapter(List<DonThuoc> donThuocList, Context context, IDonThuocAdapter onClickListener) {
        this.donThuocList = donThuocList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public DonThuocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_thuoc,parent,false);
        return new DonThuocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonThuocViewHolder holder, int position) {
        DonThuoc donThuoc = donThuocList.get(position);
        holder.txtSTT.setText(String.valueOf(donThuoc.getIdDonThuoc()));
        holder.txtTenThuoc.setText(donThuoc.getTenThuoc());
        holder.txtDonGia.setText(String.valueOf(donThuoc.getDonGia()));
        holder.txtSoLuong.setText(String.valueOf(donThuoc.getSoLuong()));
        holder.txtDVT.setText(donThuoc.getDonViTinh());
        holder.txtLieuDung.setText(donThuoc.getLieuDung());

        holder.layoutItemDonThuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.clickItemDonThuoc(donThuoc);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(donThuocList != null)
            return donThuocList.size();
        return 0;
    }

    public void release()
    {
        context = null;
    }

    public class DonThuocViewHolder extends RecyclerView.ViewHolder{

        TextView txtSTT,txtTenThuoc,txtDonGia, txtSoLuong, txtDVT, txtLieuDung;
        TableLayout layoutItemDonThuoc;

        public DonThuocViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSTT = itemView.findViewById(R.id.txtSTT);
            txtTenThuoc = itemView.findViewById(R.id.txtTenThuocItem);
            txtDonGia = itemView.findViewById(R.id.txtDonGiaItem);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuongItem);
            txtDVT = itemView.findViewById(R.id.txtDonViTinhItem);
            txtLieuDung = itemView.findViewById(R.id.txtLieuDungItem);
            layoutItemDonThuoc = itemView.findViewById(R.id.layout_itemdonthuoc);
        }
    }
}
