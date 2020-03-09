package com.olympindo.spvolympindo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olympindo.spvolympindo.R;
import com.olympindo.spvolympindo.modal.ListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListSearchKendaraanRViewAdapter extends RecyclerView.Adapter<ListSearchKendaraanRViewAdapter.ViewHolder>{
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListModel> ListItems = null;
    private ArrayList<ListModel> arraylist;
    private String data_active;


    public ListSearchKendaraanRViewAdapter(Activity activity, List<ListModel> ListItems) {
        this.activity = activity;
        this.ListItems = ListItems;
        this.arraylist = new ArrayList<ListModel>();
        this.arraylist.addAll(ListItems);
    }

    @Override
    public void onBindViewHolder(ListSearchKendaraanRViewAdapter.ViewHolder holder, final int position) {
        ListModel j = ListItems.get(position);

        holder.tvKendaraan.setText(j.getData2());
    }

    @Override
    public ListSearchKendaraanRViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_kendaraan_rv_item, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ListSearchKendaraanRViewAdapter.ViewHolder vh = new ListSearchKendaraanRViewAdapter.ViewHolder(v);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvKendaraan;

        public ViewHolder(View v) {
            super(v);
            tvKendaraan  = (TextView) v.findViewById(R.id.tv_kendaraan);
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
                }
            }
        }
        notifyDataSetChanged();
    }
}
