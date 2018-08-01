package com.gtk.smartmanagement.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gtk.smartmanagement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuestActivity extends BaseActivity {
    private static final String TAG = GuestActivity.class.getSimpleName();

    private Context mContext;
    private View mNetworkError, mProgressView;
    private TextView mTitle, mProgressContent;
    private Button mRefreshButton;
    private ListView mList;

    private GuestAdapter adapter;
    private List<Guest> list = new ArrayList<>();

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_guest);

        mTitle = findViewById(R.id.title);
        mTitle.setTextColor(Color.rgb(0x66, 0x66, 0x66));
        mTitle.setText(getString(R.string.latest_guest));
        mNetworkError = findViewById(R.id.network_error);
        mRefreshButton = findViewById(R.id.refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessage();
            }
        });
        mProgressView = findViewById(R.id.progressbar);
        mProgressContent = findViewById(R.id.progress_content);
        mList = findViewById(R.id.result_listView);

        mHandler = new Handler();

        postMessage();
    }

    private void postMessage() {
        mNetworkError.setVisibility(View.GONE);
        mProgressContent.setText(R.string.getting_data);
        mProgressView.setVisibility(View.VISIBLE);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showMessage();
            }
        }, 2000);
    }

    private void onNetworkError() {
        mProgressView.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.VISIBLE);
    }

    private void showMessage() {
        mProgressView.setVisibility(View.GONE);
        for (int i = 0; i < 5; i++)
            list.add(new Guest(getString(R.string.guest_name), getString(R.string.guest_time)));
        adapter = new GuestAdapter((ArrayList<Guest>) list);
        mList.setAdapter(adapter);
    }

    private class GuestAdapter extends BaseAdapter {
        private ArrayList<Guest> arrayList = new ArrayList<>();
        private HashMap<Integer, View> vMap = new HashMap<>();

        public GuestAdapter(ArrayList<Guest> list) {
            this.arrayList = list;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(vMap.get(position) == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.guest_list,
                        parent, false);
                TextView name = convertView.findViewById(R.id.name);
                TextView time = convertView.findViewById(R.id.time);
                name.setText(arrayList.get(position).getName());
                time.setText(arrayList.get(position).getTime());
            } else {
                convertView = vMap.get(position);
            }
            return convertView;
        }
    }

    private class Guest {
        private String name, time;

        public Guest(String name, String time) {
            this.name = name;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }
    }
}
