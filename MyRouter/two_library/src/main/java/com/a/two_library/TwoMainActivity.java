package com.a.two_library;

import android.os.Bundle;

import com.a.router_processor.ARouter;
import com.a.api.arouter_api.ParameterManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


@ARouter(path = "/two_library/TwoMainActivity")
public class TwoMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParameterManager.getInstance().loadParameter(this);

    }
}
