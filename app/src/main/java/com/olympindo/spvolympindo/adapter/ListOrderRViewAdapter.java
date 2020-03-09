package com.olympindo.spvolympindo.adapter;

/**
 * Created by Vigaz on 8/30/2018.
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

public class ListOrderRViewAdapter extends RecyclerView.Adapter<ListOrderRViewAdapter.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListModel> ListItems = null;
    private ArrayList<ListModel> arraylist;

    public ListOrderRViewAdapter(Activity activity, List<ListModel> ListMobilLelangItems) {
        this.activity = activity;
        this.ListItems = ListMobilLelangItems;
        this.arraylist = new ArrayList<ListModel>();
        this.arraylist.addAll(ListMobilLelangItems);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        ListModel j = ListItems.get(position);

        holder.tvNameCustomer.setText(j.getData2());
        holder.tvStatusDataCust.setText(j.getData3());
        holder.tvJenisKendaraan.setText(j.getData4());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_rv_item, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView tvNameCustomer,tvStatusDataCust,tvJenisKendaraan;

        public ViewHolder(View v) {
            super(v);
            tvNameCustomer      = (TextView) v.findViewById(R.id.tv_name_customer);
            tvStatusDataCust    = (TextView) v.findViewById(R.id.tv_status_data_cust);
            tvJenisKendaraan    = (TextView) v.findViewById(R.id.tv_jenis_kredit);
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
