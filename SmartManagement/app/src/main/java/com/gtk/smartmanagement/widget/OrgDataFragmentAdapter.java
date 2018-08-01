package com.gtk.smartmanagement.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.gtk.smartmanagement.R;
import com.gtk.smartmanagement.data.ResultData;

public class OrgDataFragmentAdapter extends RecyclerView.Adapter {
    private static final String TAG = OrgDataFragmentAdapter.class.getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private ResultData.OrgData[] orgDatas;
    private OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public OrgDataFragmentAdapter(Context context, ResultData.OrgData[] orgDatas) {
        LogUtils.dTag(TAG, "OrgDataFragmentAdapter");
        mLayoutInflater = LayoutInflater.from(context);
        this.orgDatas = orgDatas;
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener listener) {
        this.onRecyclerViewListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogUtils.dTag(TAG, "onBindViewHolder");
        OrgViewHolder orgViewHolder = (OrgViewHolder) holder;
        orgViewHolder.position = position;
        orgViewHolder.mTxResultValue.setText(orgDatas[position].getValue());
        orgViewHolder.mIvResultPng.setImageResource(R.drawable.up);
        orgViewHolder.mTxResultName.setText(orgDatas[position].getName());
        String subName = orgDatas[position].getSubName();
        if (subName != null) {
            orgViewHolder.mTxResultSubName.setText(subName);
            orgViewHolder.mTxResultSubName.setVisibility(View.VISIBLE);
        } else {
            orgViewHolder.mTxResultSubName.setVisibility(View.GONE);
        }
        orgViewHolder.badge.setBadge(true);
    }

    @Override
    public int getItemCount() {
        return orgDatas.length;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtils.dTag(TAG, "onCreateViewHolder");
        View view = mLayoutInflater.inflate(R.layout.card_org, parent, false);
        return new OrgViewHolder(view);
    }

    public class OrgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int position;
        private CardView cardView;
        public GtkBadge badge;
        public TextView mTxResultValue, mTxResultName, mTxResultSubName;
        public ImageView mIvResultPng;

        public OrgViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.org_card);
            cardView.setOnClickListener(this);
            mTxResultValue = (TextView) view.findViewById(R.id.result_num);
            mTxResultName = (TextView) view.findViewById(R.id.result_name);
            mTxResultSubName = view.findViewById(R.id.result_sub_name);
            mIvResultPng = (ImageView) view.findViewById(R.id.result_png);
            badge = (GtkBadge) view.findViewById(R.id.badge);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener)
                onRecyclerViewListener.onItemClick(position);
        }
    }
}
