package com.example.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.onclinic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GioKhamAdapter extends ArrayAdapter<Date> {
    Activity activity;
    int resource;
    List<Date> objects;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public GioKhamAdapter(@NonNull Activity activity, int resource, @NonNull ArrayList<Date> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);

        TextView txtGio = row.findViewById(R.id.txtItemGioKham);
        Date hour = this.objects.get(position);
        txtGio.setText(sdf.format(hour));
        return row;
    }

}
