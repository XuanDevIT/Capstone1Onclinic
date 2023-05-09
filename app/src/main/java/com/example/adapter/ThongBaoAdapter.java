package com.example.adapter;

import static com.example.helper.NgayGio.ConvertStringToDate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helper.NgayGio;
import com.example.model.ThongBao;
import com.example.onclinic.CapNhatSuatKham;
import com.example.onclinic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder>{

    private List<ThongBao> thongBaoList;
    Context context;
    private IThongBao iThongBao;

    public interface IThongBao{
        Date ngayHienTai();
        Date gioHienTai();
        Date ngayThongBao(ThongBao thongBao);
        Date gioThongBao(ThongBao thongBao);
        void clickLayoutThongBao(ThongBao thongBao);
    }

    public ThongBaoAdapter(List<ThongBao> thongBaoList, Context context, IThongBao iThongBao) {
        this.thongBaoList = thongBaoList;
        this.context = context;
        this.iThongBao = iThongBao;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thong_bao,parent,false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBao thongBao = thongBaoList.get(position);

        Date ngayHienTai = iThongBao.ngayHienTai();
        Date gioHienTai = iThongBao.gioHienTai();
        Date ngayThongBao = iThongBao.ngayThongBao(thongBao);
        Date gioThongBao = iThongBao.gioThongBao(thongBao);
        if(ngayHienTai.getTime() - ngayThongBao.getTime() > 1728000000) return;//trên 20 ngày thì ko hiện
        if(ngayThongBao.getTime() == ngayHienTai.getTime())
        {
            if(gioHienTai.getTime()-gioThongBao.getTime()<3600000)//dưới 60p
            {
                long soPhut = (gioHienTai.getTime()-gioThongBao.getTime())/60000;
                holder.txtThoiGian.setText(soPhut+" phút trước");

            }
            else
            {
                long soGio = (gioHienTai.getTime()-gioThongBao.getTime())/3600000;
                holder.txtThoiGian.setText(soGio+" giờ trước");
            }
        }
        else{
            long soNgay = (ngayHienTai.getTime() - ngayThongBao.getTime())/86400000;
            holder.txtThoiGian.setText(soNgay+" ngày trước");
        }

        holder.txtTieuDe.setText(thongBao.getTieuDe());
        holder.txtNoiDung.setText(thongBao.getNoiDung());

        holder.layoutThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iThongBao.clickLayoutThongBao(thongBao);
            }
        });
    }

    public void release()
    {
        context = null;
    }

    @Override
    public int getItemCount() {
        if(thongBaoList != null)
            return thongBaoList.size();
        return 0;
    }

    public class ThongBaoViewHolder extends RecyclerView.ViewHolder {

        TextView txtTieuDe, txtNoiDung, txtThoiGian;
        ConstraintLayout layoutThongBao;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieuDe = itemView.findViewById(R.id.txtTieuDeThongBao);
            txtNoiDung = itemView.findViewById(R.id.txtNoiDungThongBao);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGianThongBao);
            layoutThongBao = itemView.findViewById(R.id.layout_thongbao);
        }
    }
}
