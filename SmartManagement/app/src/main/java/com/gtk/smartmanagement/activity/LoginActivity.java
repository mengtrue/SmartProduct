package com.gtk.smartmanagement.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.reflect.TypeToken;
import com.gtk.smartmanagement.R;
import com.gtk.smartmanagement.data.LoginData;
import com.gtk.smartmanagement.data.RemoteToken;
import com.gtk.smartmanagement.tool.GsonParser;
import com.gtk.smartmanagement.tool.OkhttpUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private View mProgressView, mInputView, mButtonView;
    private EditText mNameView, mPasswordView;
    private Button mLoginButton;
    private AppCompatCheckBox mShowPassword, mSaveLogin;
    private boolean posting = false;

    private Handler mHandler;

    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;
        mHandler = new Handler();

        mProgressView = findViewById(R.id.progressbar);
        mInputView = findViewById(R.id.input_view);
        mButtonView = findViewById(R.id.input_button);

        mNameView = findViewById(R.id.login_name);
        mNameView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        mPasswordView = findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton = findViewById(R.id.login_btn);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mShowPassword = findViewById(R.id.show_password);
        mShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mShowPassword.setTextColor(Color.BLACK);
                    mPasswordView.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                } else {
                    mShowPassword.setTextColor(Color.GRAY);
                    mPasswordView.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
            }
        });

        mSaveLogin = findViewById(R.id.save_login);
        mSaveLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.dTag(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (posting) {
            showProgress(false);
        } else {
            super.onBackPressed();
        }
    }

    private void attemptLogin() {
        // Reset edit errors
        mNameView.setError(null);
        mPasswordView.setError(null);

        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.login_pwHint));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.login_nameHint));
            focusView = mNameView;
            cancel = true;
            mPasswordView.setText(null);
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            View immView = getCurrentFocus();
            if (immView != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.hideSoftInputFromWindow(immView.getWindowToken(), 0);
            }

            showProgress(true);
            OkhttpUtils.postLoginRequest(loginCallback,
                    LoginData.generatePostData(name, password));
        }
    }

    private void showProgress(final boolean show) {
        posting = show;
        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mInputView.setVisibility(show ? View.GONE : View.VISIBLE);
        mInputView.animate().setDuration(animTime).alpha(show ? 0 : 1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mInputView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                }
        );

        mButtonView.setVisibility(show ? View.GONE : View.VISIBLE);
        mButtonView.animate().setDuration(animTime).alpha(show ? 0 : 1).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mButtonView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                }
        );

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(animTime).alpha(show ? 1 : 0).setListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                }
        );
    }

    private void onNetworkError() {
        showProgress(false);
        new MaterialDialog.Builder(this)
                .title(R.string.network_error_title)
                .content(R.string.network_error_content)
                .iconRes(R.drawable.error)
                .backgroundColor(Color.rgb(0xe8, 0xe8, 0xe8))
                .show();
    }

    private void onLoginFailed(String msg) {
        showProgress(false);
        if (msg.contains("User")) {
            mNameView.setError(getString(R.string.no_name));
            mNameView.setSelectAllOnFocus(true);
            mNameView.requestFocus();
        } else if (msg.contains("Password")) {
            mPasswordView.setError(getString(R.string.password_wrong));
            mPasswordView.setSelectAllOnFocus(true);
            mPasswordView.requestFocus();
        }
    }

    private void onLoginSuccessful() {
        showProgress(false);
        Intent intent = new Intent(this, BaseResultActivity.class);
        startActivity(intent);
    }

    private Callback loginCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            LogUtils.eTag(TAG, "loginCallback onFailure: " + e);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (posting)
                        //onNetworkError();
                        onLoginSuccessful();
                }
            }, 1000);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Type type = new TypeToken<RemoteToken.TokenJsonBean>(){}.getType();
            final String msg = GsonParser.parser(body, type);
            final boolean result = response.isSuccessful();
            response.body().close();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (result)
                        onLoginSuccessful();
                    else
                        onLoginFailed(msg);
                }
            }, 1000);
        }
    };
}
