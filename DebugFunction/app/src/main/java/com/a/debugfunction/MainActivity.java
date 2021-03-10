package com.a.debugfunction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.a.debugfunction.kotlin.ResourcesUtils2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        ButterKnife.bind(this);

        init(this);
        String url = "https://www.wanandroid.com/resources/image/pc/logo.png";
        RequestBuilder<Drawable> requestBuilder = Glide.with(this)
                .load(url);

        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model,
                                           Target<Drawable> target, DataSource dataSource,
                                           boolean isFirstResource) {
                return false;
            }
        };
        requestBuilder
                .listener(requestListener)
                .load(url)
                .into(imageView);
    }

    private void init(Context context) {
        int statusBarHeight = ResourcesUtils2.Companion.getStatusBarHeight(context);
        Toast.makeText(context, "状态栏高度为：" + statusBarHeight + "像素", Toast.LENGTH_SHORT).show();
    }
}


