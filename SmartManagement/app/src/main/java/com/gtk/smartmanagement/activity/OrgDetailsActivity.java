package com.gtk.smartmanagement.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.gtk.smartmanagement.MainApplication;
import com.gtk.smartmanagement.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class OrgDetailsActivity extends BaseActivity {
    private static final String TAG = OrgDetailsActivity.class.getSimpleName();

    private Context mContext;
    private ImageView iv_info;
    private Button mRefreshButton;
    private ListView detail_list;
    private List<DetailResult> list = new ArrayList<DetailResult>();
    private DetailResultAdapter adapter;
    private List<Integer> colors = new ArrayList<Integer>(){{
        add(Color.rgb(00, 00, 0xee));
        add(Color.rgb(00, 0xbf, 0xff));
        add(Color.rgb(0x4b, 00, 0x82));
        add(Color.rgb(0x8b, 0x00, 0x00));
        add(Color.rgb(0xff, 0x8d, 0xa1));
        add(Color.rgb(0xee, 0xb4, 0x22));
        add(Color.rgb(0xee, 0x00, 0xee));
    }};

    private View network_error, mProgressView;

    private PopupWindow mPopupWindow;

    protected BarChart mChart;
    private XAxis xAxis;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_barchart);

        Intent intent = getIntent();

        mHandler = new Handler();
        network_error = (View) findViewById(R.id.network_error);
        mRefreshButton = findViewById(R.id.refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        mProgressView = findViewById(R.id.progressbar);

        TextView mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText(intent.getStringExtra("line"));

        iv_info = (ImageView) findViewById(R.id.info);
        iv_info.setClickable(true);
        iv_info.setOnClickListener(info_listener);

        TextView mLine = findViewById(R.id.org_title);
        mLine.setText(intent.getStringExtra("title"));
        TextView mBody = (TextView) findViewById(R.id.subbody);
        mBody.setText(intent.getStringExtra("subTitle"));
        TextView mTime = findViewById(R.id.time);
        int item = intent.getIntExtra("time", 0);
        switch (item) {
            case 0:
                mTime.setText("(" + getString(R.string.DAY) + ")");
                break;
            case 1:
                mTime.setText("(" + getString(R.string.WEEK) + ")");
                break;
            case 2:
                mTime.setText("(" + getString(R.string.MONTH) + ")");
                break;
        }

        mChart = (BarChart) findViewById(R.id.chart);
        detail_list = (ListView) findViewById(R.id.detail_list);
        detail_list.setClickable(false);

        getData();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    private void getData() {
        network_error.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showResult();
            }
        }, 2000);
    }

    private void onNetowrkError() {
        mProgressView.setVisibility(View.GONE);
        network_error.setVisibility(View.VISIBLE);
    }

    private void showResult() {
        mProgressView.setVisibility(View.GONE);
        showDetailResult();
        setChartSettings();
        fillChartData();
    }

    private void showPopupWindow(View view) {
        iv_info.setClickable(false);
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.info_popup, null);
        TextView message = (TextView) contentView.findViewById(R.id.my_message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
            }
        });
        TextView guest = (TextView) contentView.findViewById(R.id.guest);
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GuestActivity.class);
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
                iv_info.setClickable(true);
            }
        });
        mPopupWindow.showAsDropDown(view);
    }

    private View.OnClickListener info_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtils.dTag(TAG, "info click");
            showPopupWindow(v);
        }
    };

    private void setChartSettings() {
        mChart.setBackgroundColor(Color.rgb(0xe8, 0xe8, 0xe8));
        mChart.setExtraTopOffset(-30f);
        mChart.setExtraBottomOffset(10f);
        mChart.setExtraLeftOffset(0f);
        mChart.setExtraRightOffset(0f);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDrawGridBackground(false);

        mChart.getDescription().setEnabled(false);

        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);

        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setLabelCount(7);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setAxisLineWidth(2f);
        xAxis.setAxisLineColor(Color.GRAY);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setDrawLabels(true);
        yAxis.setSpaceTop(25f);
        yAxis.setSpaceBottom(0);
        yAxis.setTextSize(15f);
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawZeroLine(true);
        yAxis.setAxisLineWidth(2f);

        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
    }

    private void fillChartData() {
        final List<ChartData> data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            DetailResult result = (DetailResult) adapter.getItem(i);
            data.add(new ChartData((float)i, Float.valueOf(result.getfValue()), result.getName()));
        }

        /*data.add(new ChartData(0f, 24.1f, "12-29"));
        data.add(new ChartData(1f, 238.5f, "12-30"));
        data.add(new ChartData(2f, 1280.1f, "12-31"));
        data.add(new ChartData(3f, 442.3f, "01-01"));
        data.add(new ChartData(4f, 2280.1f, "01-02"));*/

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return data.get(Math.min(Math.max((int) v, 0), data.size() - 1)).xAxisValue;
            }
        });

        setChartData(data);
    }

    private void setChartData(List<ChartData> dataList) {
        ArrayList<BarEntry> values = new ArrayList<BarEntry>();

        for(int i = 0; i < dataList.size(); i++) {
            ChartData data = dataList.get(i);
            BarEntry entry = new BarEntry(data.xValue, data.yValue);
            values.add(entry);
        }

        BarDataSet barDataSet;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            barDataSet = new BarDataSet(values, "Values");
            barDataSet.setColors(colors);
            barDataSet.setValueTextColors(colors);

            BarData barData = new BarData(barDataSet);
            barData.setValueTextSize(13f);
            barData.setValueFormatter(new ValueFormatter());
            barData.setBarWidth(0.8f);

            mChart.setData(barData);
            mChart.invalidate();
        }

        mChart.setExtraOffsets(20, 20, 20, 20);
        mChart.animateXY(1000, 2500);
    }

    private class ChartData {
        public String xAxisValue;
        public float yValue, xValue;

        public ChartData(float x, float y, String axis) {
            this.xAxisValue = axis;
            this.yValue = y;
            this.xValue = x;
        }
    }

    private class ValueFormatter implements IValueFormatter {
        private DecimalFormat decimalFormat;

        public ValueFormatter() {
            decimalFormat = new DecimalFormat("##.00");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                        ViewPortHandler viewPortHandler) {
            return decimalFormat.format(value);
        }
    }

    private void showDetailResult() {
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        list.add(new DetailResult(getResources().getString(R.string.jiepai_loss),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.up));
        list.add(new DetailResult(getResources().getString(R.string.shouxian_loss),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.up));
        list.add(new DetailResult(getResources().getString(R.string.shoujian_loss),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.down));
        list.add(new DetailResult(getResources().getString(R.string.qiexian_loss),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.down));
        list.add(new DetailResult(getResources().getString(R.string.huangongdan_loss),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.up));
        list.add(new DetailResult(getResources().getString(R.string.guzhang_loss),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.down));
        list.add(new DetailResult(getResources().getString(R.string.xianti_efficiency),
                dcmFmt.format(new Random().nextFloat() * 100), R.drawable.up));
        LogUtils.dTag(TAG, "the detail result size is " + list.size());
        adapter = new DetailResultAdapter((ArrayList<DetailResult>) list);
        detail_list.setAdapter(adapter);
    }

    public class DetailResultAdapter extends BaseAdapter {

        private ArrayList<DetailResult> arrayList = new ArrayList<>();

        private HashMap<Integer, View> vMap = new HashMap<>();

        public DetailResultAdapter(ArrayList<DetailResult> list) {
            this.arrayList = list;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
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
            if (vMap.get(position) == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.details_view,
                        parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.detail_name);
                TextView value = (TextView) convertView.findViewById(R.id.detail_value);
                ImageView img = (ImageView) convertView.findViewById(R.id.detail_img);

                name.setText(arrayList.get(position).getName());
                value.setText(arrayList.get(position).getValue());
                //img.setBackgroundResource(arrayList.get(position).getImg());

                vMap.put(position, convertView);
            } else {
                convertView = vMap.get(position);
            }
            return convertView;
        }
    }

    public class DetailResult {
        private String name, value, fValue;
        private int img;

        public DetailResult(String name, String value, int img) {
            this.name = name;
            this.value = value + "%";
            this.fValue = value;
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getfValue() {
            return fValue;
        }

        public int getImg() {
            return img;
        }
    }
}
