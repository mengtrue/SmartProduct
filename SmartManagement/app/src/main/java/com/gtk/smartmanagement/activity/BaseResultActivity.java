package com.gtk.smartmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;
import com.gtk.smartmanagement.R;
import com.gtk.smartmanagement.widget.BaseResultAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.gtk.smartmanagement.activity.LoginActivity.loginActivity;

public class BaseResultActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = BaseResultActivity.class.getSimpleName();
    public static final String EXTRA_MESSAGE = "FRAGMENT_NUM";

    private List<BaseResultAdapter.BaseResult> list = null;
    private BaseResultAdapter mAdapter = null;
    private ListView listView;
    private View network_error, mProgressView;
    private Button mRefreshButton;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivity.finish();
        loginActivity = null;

        setContentView(R.layout.activity_total_result);
        View root = findViewById(R.id.root);
        root.setBackground(getResources().getDrawable(R.drawable.u26));

        mHandler = new Handler();

        network_error = findViewById(R.id.network_error);
        mRefreshButton = findViewById(R.id.refresh);
        mRefreshButton.setOnClickListener(refreshListener);
        network_error.setVisibility(View.GONE);

        mProgressView = findViewById(R.id.progressbar);

        listView = findViewById(R.id.result_listView);
        listView.setOnItemClickListener(this);
        list = new ArrayList<>();
        //addTotalResultList();

        getTotalResult();
    }

    private void getTotalResult() {
        network_error.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onGetDataSuccess();
                //onNetworkError();
            }
        }, 2000);
    }

    private void onNetworkError() {
        mProgressView.setVisibility(View.GONE);
        network_error.setVisibility(View.VISIBLE);
    }

    private void onGetDataSuccess() {
        mProgressView.setVisibility(View.GONE);
        network_error.setVisibility(View.GONE);
        addTotalResultList();
    }

    private void addTotalResultList() {
        LogUtils.dTag(TAG, "addTotalResultList");
        list.add(new BaseResultAdapter.BaseResult(
                R.drawable.happy, "80%", R.drawable.up, getString(R.string.OLE)
        ));
        list.add(new BaseResultAdapter.BaseResult(
                R.drawable.crying, "90%", R.drawable.down, getString(R.string.OPE)
        ));
        list.add(new BaseResultAdapter.BaseResult(
                R.drawable.happy, "100%", R.drawable.up, getString(R.string.OEE)
        ));
        mAdapter = new BaseResultAdapter((ArrayList<BaseResultAdapter.BaseResult>)list,
                getApplicationContext());
        LogUtils.dTag(TAG, "size is " + mAdapter.getCount());
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseResultAdapter.BaseResult result = list.get(position);
        LogUtils.iTag(TAG, "press position : " + position);
        Intent intent = new Intent(this, ResultAnalyseActivity.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        startActivity(intent);
    }

    private Callback postCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onNetworkError();
                }
            }, 1000);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onGetDataSuccess();
                }
            }, 1000);
        }
    };

    private View.OnClickListener refreshListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getTotalResult();
        }
    };
}
