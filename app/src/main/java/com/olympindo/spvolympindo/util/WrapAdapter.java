package com.olympindo.spvolympindo.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olympindo.spvolympindo.R;

import java.util.ArrayList;

public class WrapAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> mItems;

    /**
     * Instantiates a new Wrap adapter.
     *
     * @param context the context
     * @param items   the items
     */
    public WrapAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(mContext)
                .inflate(R.layout.spinner_item, parent, false);
        textView.setText(mItems.get(position));
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final TextView textView = (TextView) LayoutInflater.from(mContext)
                .inflate(R.layout.spinner_item, parent, false);
        textView.setText(mItems.get(position));
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setSingleLine(false);
            }
        });
        return textView;
    }
}
