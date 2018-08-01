package com.gtk.smartmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.gtk.smartmanagement.MainApplication;
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
    private TextView mTitle;
    private ImageView mImageInfo;
    private PopupWindow mPopupWindow;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivity.finish();
        loginActivity = null;

        setContentView(R.layout.activity_total_result);
        //View root = findViewById(R.id.root);
        //root.setBackground(getResources().getDrawable(R.drawable.u26));

        mHandler = new Handler();

        mTitle = findViewById(R.id.title);
        mTitle.setText(R.string.app_title);
        mImageInfo = findViewById(R.id.info);
        mImageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();

        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
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

    private void showPopupWindow(View view) {
        mImageInfo.setClickable(false);
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.info_popup, null);
        TextView message = (TextView) contentView.findViewById(R.id.my_message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                startActivity(intent);
            }
        });
        TextView guest = (TextView) contentView.findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuestActivity.class);
                startActivity(intent);
            }
        });
        TextView loginOut = (TextView) contentView.findViewById(R.id.loginOut);
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainApplication mainApplication = (MainApplication) getApplication();
                mainApplication.exitLogin();
            }
        });

        mPopupWindow = new PopupWindow(contentView,
                ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.dTag(TAG, "onTouch");
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                LogUtils.dTag(TAG, "popup dismiss");
                mImageInfo.setClickable(true);
            }
        });
        mPopupWindow.showAsDropDown(view);
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
