package com.olympindo.spvolympindo.adapter;

/**
 * Created by Vigaz on 9/1/2018.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.olympindo.spvolympindo.R;
import com.olympindo.spvolympindo.modal.ListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListSurveyorRViewAdapter extends RecyclerView.Adapter<ListSurveyorRViewAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListModel> ListItems = null;
    private ArrayList<ListModel> arraylist;
    private String data_active;

    public ListSurveyorRViewAdapter(Activity activity, List<ListModel> ListItems) {
        this.activity = activity;
        this.ListItems = ListItems;
        this.arraylist = new ArrayList<ListModel>();
        this.arraylist.addAll(ListItems);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        ListModel j = ListItems.get(position);

        holder.tvNamaSurveyor.setText(j.getData2());
        holder.tvAlamat.setText(j.getData3());
        String active = j.getData4();
        if(active.equals("0")){
            data_active = "Non Active";
        }else{
            data_active = "Active";
        }
        holder.tvActive.setText(data_active);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_surveyor_rv_item, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView tvNamaSurveyor,tvAlamat,tvActive;

        public ViewHolder(View v) {
            super(v);
            tvNamaSurveyor  = (TextView) v.findViewById(R.id.tv_nama_surveyor);
            tvAlamat        = (TextView) v.findViewById(R.id.tv_alamat);
            tvActive        = (TextView) v.findViewById(R.id.tv_active);
        }
    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return ListItems.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ListItems.clear();
        if (charText.length() == 0) {
            ListItems.addAll(arraylist);
        } else {
            for (ListModel postDetail : arraylist) {
                //if (charText.length() != 0 && postDetail.getData4().toLowerCase(Locale.getDefault()).contains(charText)) {
                if (postDetail.getData2().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ListItems.add(postDetail);
                }else if (postDetail.getData3().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ListItems.add(postDetail);
                }else if (postDetail.getData4().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ListItems.add(postDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
