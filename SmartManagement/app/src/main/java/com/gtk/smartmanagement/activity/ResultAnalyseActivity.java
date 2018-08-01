package com.gtk.smartmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gtk.smartmanagement.MainApplication;
import com.gtk.smartmanagement.R;
import com.gtk.smartmanagement.data.ResultData;
import com.gtk.smartmanagement.tool.GsonParser;
import com.gtk.smartmanagement.tool.OkhttpUtils;
import com.gtk.smartmanagement.widget.GtkSpinner;
import com.gtk.smartmanagement.widget.OrgDataFragmentAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResultAnalyseActivity extends BaseActivity {
    private static final String TAG = ResultAnalyseActivity.class.getSimpleName();

    private TextView title;
    private ImageView iv_info;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private List<View> mViews = new ArrayList<>();
    private TextView tv_dayTab, tv_weekTab, tv_monthTab;
    private RecyclerView mRecyclerView;
    private View network_error, mProgressView;
    private Button mRefreshButton;

    private Context mContext;
    private PopupWindow mPopupWindow;
    private TextView message, guest, loginOut;

    private int fragment_num = -1;

    //private GtkSpinner sp_area, sp_serial;
    private Spinner sp_area, sp_serial;
    private String sArea = null, sSerie = null, tempArea = null, tempSerie = null;
    private ResultData.OrgData[] orgDatas;

    private Handler mHandler;

    private int index = 0;
    private String orgTitle, orgSubTitle = null;
    private boolean posting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        Intent intent = getIntent();
        fragment_num = intent.getIntExtra(BaseResultActivity.EXTRA_MESSAGE, -1);

        setContentView(R.layout.result_analyse);

        mHandler = new Handler();
        index = 0;
        orgTitle = null;
        orgSubTitle = null;
        initView();
        initViewPager();
    }

    @Override
    public void onBackPressed() {
        if (index != 0) {
            index--;
            getData();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        switch (fragment_num) {
            case 0:
                title.setText(getText(R.string.OLE_Total_Title));
                break;
            case 1:
                title.setText(getText(R.string.OPE_Total_Title));
                break;
            case 2:
                title.setText(getText(R.string.OEE_Total_Title));
                break;
            default:
                title.setText(getText(R.string.OLE_Total_Title));
                fragment_num = 0;
                break;
        }
        iv_info = (ImageView) findViewById(R.id.info);
        iv_info.setClickable(true);
        iv_info.setOnClickListener(info_listener);

        //sp_area = (GtkSpinner) findViewById(R.id.gtk_area);
        //sp_serial = (GtkSpinner) findViewById(R.id.gtk_serial);
        sp_area = findViewById(R.id.gtk_area);
        sp_serial = findViewById(R.id.gtk_serial);
        sp_area.setOnItemSelectedListener(areaItemClickListener);
        sp_serial.setOnItemSelectedListener(serialItemClickListener);
        /*String[] area_list = getResources().getStringArray(R.array.gtk_area);
        String[] serial_list = getResources().getStringArray(R.array.gtk_serial);
        ArrayAdapter<String> area_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, area_list);
        ArrayAdapter<String> serial_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, serial_list);
        sp_area.setAdapter(area_adapter);
        sp_serial.setAdapter(serial_adapter);
        sp_area.setOnItemClickListener(areaItemClickListener);
        sp_serial.setOnItemClickListener(serialItemClickListener);*/
    }

    public void initViewPager() {
        tv_dayTab = (TextView) findViewById(R.id.day_tab);
        tv_weekTab = (TextView) findViewById(R.id.week_tab);
        tv_monthTab = (TextView) findViewById(R.id.month_tab);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View dayTab = mLayoutInflater.inflate(R.layout.result_fragment, null);
        View weekTab = mLayoutInflater.inflate(R.layout.result_fragment, null);
        View monthTab = mLayoutInflater.inflate(R.layout.result_fragment, null);
        mViews.add(dayTab);
        mViews.add(weekTab);
        mViews.add(monthTab);

        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup viewGroup, int position, Object object) {
                LogUtils.eTag(TAG, "page destroy position = " + position);
                viewGroup.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup viewGroup, int position) {
                View view = mViews.get(position);
                viewGroup.addView(view);
                return view;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                resetBottomTab();
                int color = Color.rgb(0x19, 0x9E, 0xD8);
                switch (position) {
                    case 0:
                        //tv_dayTab.setTextColor(Color.RED);
                        tv_dayTab.setBackgroundColor(color);
                        break;
                    case 1:
                        //tv_weekTab.setTextColor(Color.RED);
                        tv_weekTab.setBackgroundColor(color);
                        break;
                    case 2:
                        //tv_monthTab.setTextColor(Color.RED);
                        tv_monthTab.setBackgroundColor(color);
                        break;
                }
                getData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        getData();
    }

    public void resetBottomTab() {
        int color = Color.rgb(0xE8, 0xE8, 0xE8);
        tv_dayTab.setBackgroundColor(color);
        tv_weekTab.setBackgroundColor(color);
        tv_monthTab.setBackgroundColor(color);
    }

    private void getData() {
        mRecyclerView = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.recycler_view);
        network_error = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.network_error);
        mProgressView = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.progressbar);
        mRecyclerView.setVisibility(View.GONE);
        network_error.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        sp_area.setEnabled(false);
        sp_serial.setEnabled(false);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                debugData();
                updateView();
                //onNetworkError();
            }
        }, 2000);
    }

    private void debugData() {
        orgDatas = new ResultData.OrgData[6];
        String name, subName = null;

        for (int i = 0; i < 6; i++) {
            if (index == 0) {
                name = "BU" + (i + 1);
                subName = null;
            } else {
                name = orgTitle;
                if (index == 1)
                    subName = "第" + convertString(i + 1) + "制造中心";
                else if (index == 2)
                    subName = orgSubTitle + "制造" + convertString(i + 1) + "部";
            }

            orgDatas[i] = new ResultData.OrgData(name, subName,
                    String.valueOf((new Random().nextInt(100)) % (101)) + "%");
            orgTitle = name;
        }

        for (int i = 0; i < 6; i++) {
            LogUtils.dTag(TAG, "data[" + i + "] name = " + orgDatas[i].getName() + ", value = " + orgDatas[i].getValue());
        }
        network_error = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.network_error);
        mProgressView = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.progressbar);
        network_error.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
    }

    private String convertString(int i) {
        switch (i) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
        }
        return null;
    }

    private void onNetworkError() {
        LogUtils.dTag(TAG, "onNetworkError");
        mProgressView = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.progressbar);
        network_error = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.network_error);
        mRefreshButton = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.refresh);
        mProgressView.setVisibility(View.GONE);
        network_error.setVisibility(View.VISIBLE);
        sp_area.setEnabled(true);
        sp_serial.setEnabled(true);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    private void showPopupWindow(View view) {
        iv_info.setClickable(false);
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.info_popup, null);
        message = (TextView) contentView.findViewById(R.id.my_message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
            }
        });
        guest = (TextView) contentView.findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GuestActivity.class);
                startActivity(intent);
            }
        });
        loginOut = (TextView) contentView.findViewById(R.id.loginOut);
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
                iv_info.setClickable(true);
            }
        });
        mPopupWindow.showAsDropDown(view);
    }

    private void checkAndPostServer() {
        /*if (sArea != null && sSerie != null) {
            if (!sArea.equals(tempArea) || !sSerie.equals(tempSerie)) {
                OkhttpUtils.postGetOrgRequest(orgCallback,
                        new ResultData.OrgMap("2018-7-15", "2018-7-16", sArea, sSerie)
                                .toMap());
                tempArea = sArea;
                tempSerie = sSerie;
            }
        }*/
        index = 0;
        getData();
    }

    public void updateView() {
        mRecyclerView = mViews.get(mViewPager.getCurrentItem()).findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,
                orgDatas.length > 4 ? 2 : 1));
        OrgDataFragmentAdapter mAdapter = new OrgDataFragmentAdapter(mContext, orgDatas);
        mAdapter.setOnRecyclerViewListener(onRecyclerViewListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        sp_area.setEnabled(true);
        sp_serial.setEnabled(true);
    }

    private View.OnClickListener info_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtils.dTag(TAG, "info click");
            showPopupWindow(v);
        }
    };

    private AdapterView.OnItemSelectedListener areaItemClickListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sArea = getResources().getStringArray(R.array.gtk_area)[position];
            LogUtils.dTag(TAG, "area select " + sArea);
            checkAndPostServer();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener serialItemClickListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sSerie = getResources().getStringArray(R.array.gtk_serial)[position];
            LogUtils.dTag(TAG, "series select " + sSerie);
            checkAndPostServer();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private Callback orgCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            LogUtils.dTag(TAG, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            orgDatas = GsonParser.parserOrg(body);
            for(ResultData.OrgData orgData : orgDatas) {
                LogUtils.dTag(TAG, "name = " + orgData.getName() + ", value = " + orgData.getValue());
            }
            response.body().close();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateView();
                }
            }, 1000);
        }
    };

    private Callback orgDataCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            LogUtils.dTag(TAG, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Type type = new TypeToken<ResultData.OrgDetailDataJsonBean>(){}.getType();
            response.body().close();
        }
    };

    private OrgDataFragmentAdapter.OnRecyclerViewListener onRecyclerViewListener =
            new OrgDataFragmentAdapter.OnRecyclerViewListener() {
        @Override
        public void onItemClick(int position) {
            LogUtils.dTag(TAG, "item click : " + position);
            if (index < 2) {
                orgTitle = orgDatas[position].getName();
                if (index >= 1)
                    orgSubTitle = orgDatas[position].getSubName();
                index++;
                getData();
            } else {
                Intent intent = new Intent(mContext, OrgDetailsActivity.class);
                intent.putExtra("line", title.getText());
                intent.putExtra("title", orgDatas[position].getName());
                intent.putExtra("subTitle", orgDatas[position].getSubName());
                intent.putExtra("time", mViewPager.getCurrentItem());
                startActivity(intent);
            }
        }
    };
}
