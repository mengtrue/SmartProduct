package com.smart.httpdemo.tool;

import android.text.TextUtils;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.CookieJar;
import okhttp3.Headers;

public class OkhttpConfiguration {

    private List<Part> commonParams;
    protected Headers commonHeaders;
    private HostnameVerifier hostnameVerifier;
    private long timeout = 30000;
    private CookieJar cookieJar;
    private Cache cache;
    private Authenticator authenticator;
    private CertificatePinner certificatePinner;
    private boolean retryOnConnectFailure;
    private SSLSocketFactory sslSocketFactory;

    private OkhttpConfiguration(final Builder builder) {
        this.commonParams = builder.commonParams;
        this.commonHeaders = builder.commonHeaders;
        this.hostnameVerifier = builder.hostnameVerifier;
        this.timeout = builder.timeout;
        this.cookieJar = builder.cookieJar;
        this.cache = builder.cache;
        this.authenticator = builder.authenticator;
        this.certificatePinner = builder.certificatePinner;
        this.retryOnConnectFailure = builder.retryOnConnectFailure;
        this.sslSocketFactory = builder.sslSocketFactory;
    }

    public List<Part> getCommonParams() {
        return commonParams;
    }

    public Headers getCommonHeaders() {
        return commonHeaders;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public long getTimeout() {
        return timeout;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public Cache getCache() {
        return cache;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public CertificatePinner getCertificatePinner() {
        return certificatePinner;
    }

    public boolean isRetryOnConnectFailure() {
        return retryOnConnectFailure;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public Part createPart(String key, String value) {
        return new Part(key, value);
    }

    public static class Builder {
        private List<Part> commonParams;
        protected Headers commonHeaders;
        private HostnameVerifier hostnameVerifier;
        private long timeout;
        private CookieJar cookieJar = CookieJar.NO_COOKIES;
        private Cache cache;
        private Authenticator authenticator;
        private CertificatePinner certificatePinner;
        private boolean retryOnConnectFailure;
        private SSLSocketFactory sslSocketFactory;

        public Builder() {
            retryOnConnectFailure = true;
        }

        public Builder setCommonParams(List<Part> params) {
            this.commonParams = params;
            return this;
        }

        public Builder setCommonHeaders(Headers headers) {
            this.commonHeaders = headers;
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public Builder setCertificatePinner(CertificatePinner certificatePinner) {
            this.certificatePinner = certificatePinner;
            return this;
        }

        public Builder setTimeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setCookieJar(CookieJar cookieJar) {
            this.cookieJar = cookieJar;
            return this;
        }

        public Builder setCache(Cache cache) {
            this.cache = cache;
            return this;
        }

        public Builder setAuthenticator(Authenticator authenticator) {
            this.authenticator = authenticator;
            return this;
        }

        public Builder setRetryOnConnectFailure(boolean retryOnConnectFailure) {
            this.retryOnConnectFailure = retryOnConnectFailure;
            return this;
        }

        public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public OkhttpConfiguration build() {
            return new OkhttpConfiguration(this);
        }
    }

    public final class Part {
        private String key;
        private String value;

        public Part(String key, String value) {
            setKey(key);
            setValue(value);
        }

        protected void setKey(String key) {
            this.key = (key == null) ? "" : key;
        }

        protected void setValue(String value) {
            this.value = (value == null) ? "" : value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || !(object instanceof Part)) {
                return false;
            }
            Part part = (Part) object;
            if (part == null)
                return false;
            if (TextUtils.equals(part.getKey(), getKey()) &&
                    TextUtils.equals(part.getValue(), getValue())) {
                return true;
            }
            return false;
        }
    }
}
