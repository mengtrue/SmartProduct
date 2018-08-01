package com.gtk.smartmanagement.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gtk.smartmanagement.R;

import java.util.ArrayList;

public class BaseResultAdapter extends BaseAdapter {
    private ArrayList<BaseResult> arrayList;
    private Context mContext;

    public BaseResultAdapter(ArrayList<BaseResult> data, Context context) {
        this.arrayList = data;
        this.mContext = context;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.result_listview_item,
                    parent, false);
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            TextView value = (TextView) convertView.findViewById(R.id.result_num);
            ImageView png = (ImageView) convertView.findViewById(R.id.result_png);
            TextView name = (TextView) convertView.findViewById(R.id.result_name);

            icon.setBackgroundResource(arrayList.get(position).getIcon());
            value.setText(arrayList.get(position).getResultNum());
            png.setBackgroundResource(arrayList.get(position).getPng());
            name.setText(arrayList.get(position).getResultName());
        }
        return convertView;
    }

    public static class BaseResult {
        private int bIcon;
        private String bResult_num;
        private int bResult_png;
        private String bResult_name;

        public BaseResult(int icon, String value, int png, String name) {
            this.bIcon = icon;
            this.bResult_num = value;
            this.bResult_png = png;
            this.bResult_name = name;
        }

        public int getIcon() {
            return bIcon;
        }

        public String getResultNum() {
            return bResult_num;
        }

        public int getPng() {
            return bResult_png;
        }

        public String getResultName() {
            return bResult_name;
        }
    }
}
