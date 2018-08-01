package com.gtk.smartmanagement.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gtk.smartmanagement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends BaseActivity {

    private static final String TAG = MessageActivity.class.getSimpleName();

    private Context mContext;
    private View mNetworkError, mProgressView;
    private TextView mTitle, mProgressContent;
    private Button mRefreshButton;
    private ListView mList;

    private MessageAdapter adapter;
    private List<Message> list = new ArrayList<>();

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_message);
        mTitle = findViewById(R.id.title);
        mTitle.setText(R.string.user_message);

        ImageView info = findViewById(R.id.info);
        info.setVisibility(View.GONE);

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
                //onNetworkError();
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
            list.add(new Message(getString(R.string.message_title), getString(R.string.message_demo)));
        adapter = new MessageAdapter((ArrayList<Message>) list);
        mList.setAdapter(adapter);
    }

    private class MessageAdapter extends BaseAdapter {
        private ArrayList<Message> arrayList = new ArrayList<>();
        private HashMap<Integer, View> vMap = new HashMap<>();

        public MessageAdapter(ArrayList<Message> list) {
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
            if (vMap.get(position) == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.message_list,
                        parent, false);
                TextView message = convertView.findViewById(R.id.message_content);
                message.setText(arrayList.get(position).getContent());
            } else {
                convertView = vMap.get(position);
            }
            return convertView;
        }
    }

    private class Message {
        private String title;
        private String content;

        public Message(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }
        public String getContent() {
            return content;
        }
    }
}
