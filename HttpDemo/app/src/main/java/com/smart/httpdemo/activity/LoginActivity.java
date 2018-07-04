package com.smart.httpdemo.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.smart.httpdemo.R;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etUsername;
    private EditText etPassword;
    private Button btSignin;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LogUtils.dTag(TAG, "login start finish");
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btSignin = findViewById(R.id.bt_enter);
        cardView = findViewById(R.id.cardView);
    }
}
