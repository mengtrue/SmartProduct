package com.gtk.smartmanagement.tool;

import com.blankj.utilcode.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpUtils {
    private static final String TAG = OkhttpUtils.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient okHttpClient;
    private long timeout = 30000;

    private static final String login_url = "http://10.212.4.86:5003/getdata/?token=123456";
    private static final String getToken_url = "http://10.212.4.86:5003/gettoken/";
    private static final String getOrg_url = "http://10.212.4.86:5003/getdata/?token=123456";
    private static final String getOrgData_url = "http://10.212.4.86:5003/getdata/?token=123456";

    static {
        if (okHttpClient == null) {
            new OkhttpUtils();
        }
    }

    private OkhttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS);

        LogUtils.dTag(TAG, "OkhttpUtils init ...");
        okHttpClient = builder.build();
    }

    public static void postLoginRequest(Callback callback, Map<String, String> map) {
        post(login_url, callback, map);
    }

    public static void postGetTokenRequest(Callback callback, Map<String, String> map) {
        post(getToken_url, callback, map);
    }

    public static void postGetOrgRequest(Callback callback, Map<String, String> map) {
        post(getOrg_url, callback, map);
    }

    public static void postGetOrgDataRequest(Callback callback, Map<String, String> map) {
        post(getOrgData_url, callback, map);
    }

    private static void post(String url, Callback callback, Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
