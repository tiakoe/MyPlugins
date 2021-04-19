package com.a.api.arouter_api;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.io.Serializable;

/**
 * 传递参数
 */
public class BundleManager {

    private Bundle bundle = new Bundle();

    private Call call;

    public Bundle getBundle() {
        return bundle;
    }

    public BundleManager withString(@NonNull String key, @Nullable String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleManager withInt(@NonNull String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleManager withBoolean(@NonNull String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleManager withBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public BundleManager withSerializable(@NonNull String key, @Nullable Serializable object) {
        bundle.putSerializable(key, object);
        return this;
    }

    public Object navigation(Context context) {
        return RouterManager.getInstance().navigation(context, this);
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Call getCall() {
        return call;
    }
}
