package com.smart.httpdemo.tool;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

public class OkhttpUtils {
    private static final String TAG = OkhttpUtils.class.getSimpleName();

    private OkHttpClient okHttpClient;
    private static OkhttpUtils okhttpUtils;
    private OkhttpConfiguration configuration;

    private OkhttpUtils() {}

    public synchronized void init(OkhttpConfiguration configuration) {
        this.configuration = configuration;

        long timeout = configuration.getTimeout();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS);
        if (configuration.getHostnameVerifier() != null) {
            builder.hostnameVerifier(configuration.getHostnameVerifier());
        }

        CookieJar cookieJar = configuration.getCookieJar();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }

        if (configuration.getCache() != null) {
            builder.cache(configuration.getCache());
        }

        if (configuration.getAuthenticator() != null) {
            builder.authenticator(configuration.getAuthenticator());
        }

        if (configuration.getCertificatePinner() != null) {
            builder.certificatePinner(configuration.getCertificatePinner());
        }

        if (configuration.getSslSocketFactory() != null) {
            builder.sslSocketFactory(configuration.getSslSocketFactory());
        }

        builder.retryOnConnectionFailure(configuration.isRetryOnConnectFailure());

        LogUtils.dTag(TAG, "OkhttpUtils init ...");
        okHttpClient = builder.build();
    }

    public static OkhttpUtils getInstance() {
        if (okhttpUtils == null)
            okhttpUtils = new OkhttpUtils();
        return okhttpUtils;
    }

    public void updateCommonParams(String key, String value) {
        boolean added = false;
        List<OkhttpConfiguration.Part> commonParams = configuration.getCommonParams();
        if (commonParams != null) {
            for(OkhttpConfiguration.Part param : commonParams) {
                if (param != null && TextUtils.equals(param.getKey(), key)) {
                    param.setValue(value);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            commonParams.add(configuration.createPart(key, value));
        }
    }

    public void updateCommonHeader(String key, String value) {
        Headers headers = configuration.getCommonHeaders();
        if (headers == null)
            headers = new Headers.Builder().build();
        configuration.commonHeaders = headers.newBuilder().set(key, value).build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return okHttpClient.newBuilder();
    }

    public List<OkhttpConfiguration.Part> getCommonParams() {
        return configuration.getCommonParams();
    }

    public HostnameVerifier getHostnameVerifier() {
        return configuration.getHostnameVerifier();
    }

    public long getTimeout() {
        return configuration.getTimeout();
    }

    public Headers getCommonHeaders() {
        return configuration.getCommonHeaders();
    }
}
