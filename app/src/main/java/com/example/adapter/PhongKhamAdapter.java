package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.DanhGia;
import com.example.model.LichKham;
import com.example.model.PhongKham;
import com.example.onclinic.R;
import com.example.helper.NoteFireBase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhongKhamAdapter extends RecyclerView.Adapter<PhongKhamAdapter.PhongKhamViewHolder> implements Filterable {

    private List<PhongKham> phongKhamList;
    private List<PhongKham> phongKhamListOld;
    Context context;
    public IPhongKhamAdapter onClickListener;
    private int lastSelected = -1;

    public PhongKhamAdapter(List<PhongKham> phongKhamList, Context context, IPhongKhamAdapter onClickListener) {
        this.phongKhamList = phongKhamList;
        this.phongKhamListOld = phongKhamList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void release()
    {
        context = null;
    }

    @NonNull
    @Override
    public PhongKhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phong_kham,parent,false);
        return new PhongKhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongKhamViewHolder holder,final int position) {
        //holder.setIsRecyclable(false);
        PhongKham phongKham = phongKhamList.get(getItemViewType(position));//dùng getItemViewType để lấy chính xác vị trí item khi scroll
        if(phongKham == null) return;
        if(phongKham.getHinhAnh()!=null) {
            byte[] decodedString = Base64.decode(phongKham.getHinhAnh(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgPhongKham.setImageBitmap(decodedByte);
        }
        holder.txtPhongKham.setText("Phòng khám "+phongKham.getTenPhongKham());
        holder.txtChuyenKhoa.setText("Chuyên khoa "+phongKham.getChuyenKhoa());

        DatabaseReference refTBDanhGia = FirebaseDatabase.getInstance(NoteFireBase.firebaseSource).getReference()
                .child(NoteFireBase.PHONGKHAM).child(phongKham.getIdPhongKham()).child(NoteFireBase.DANHGIA);
        refTBDanhGia.addValueEventListener(new ValueEventListener() {
            float tongDanhGia = 0;
            int listSize = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        DanhGia danhGia = data.getValue(DanhGia.class);
                        tongDanhGia += danhGia.getRating();
                        listSize++;
                    }
                    if(listSize!=0) holder.ratingBar.setRating(tongDanhGia / listSize);
                    else holder.ratingBar.setRating(0);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.layoutPhongKham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelected = position;
                onClickListener.clickPhongKham(phongKham);
                notifyDataSetChanged();
            }
        });
        if(lastSelected == position ){
            holder.layoutPhongKham.setBackgroundColor(Color.GRAY);
            holder.txtChuyenKhoa.setTextColor(Color.WHITE);
            holder.txtPhongKham.setTextColor(Color.WHITE);
        }
        else{
            holder.layoutPhongKham.setBackgroundColor(Color.WHITE);
            holder.txtChuyenKhoa.setTextColor(Color.BLACK);
            holder.txtPhongKham.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(phongKhamList!=null)
            return phongKhamList.size();
        return 0;
    }

    public interface IPhongKhamAdapter
    {
        void clickPhongKham(PhongKham phongKham);
    }

    public class PhongKhamViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPhongKham;
        TextView txtPhongKham, txtChuyenKhoa;
        RatingBar ratingBar;
        ConstraintLayout layoutPhongKham;

        public PhongKhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhongKham = itemView.findViewById(R.id.imgItemPhongKham);
            txtPhongKham = itemView.findViewById(R.id.txtItemPhongKham);
            txtChuyenKhoa = itemView.findViewById(R.id.txtItemChuyenKhoa);
            ratingBar = itemView.findViewById(R.id.item_rating_bar);
            layoutPhongKham = itemView.findViewById(R.id.layout_phongkham);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if(strSearch.isEmpty()) {
                    phongKhamList = phongKhamListOld;
                }
                else{
                    List<PhongKham> list = new ArrayList<>();
                    for(PhongKham phongKham : phongKhamListOld)
                    {
                        if(phongKham.getTenPhongKham().toLowerCase().contains(strSearch.toLowerCase())
                                || phongKham.getChuyenKhoa().toLowerCase().contains(strSearch.toLowerCase()))
                        {
                            list.add(phongKham);
                        }
                    }
                    phongKhamList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = phongKhamList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                phongKhamList = (List<PhongKham>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
