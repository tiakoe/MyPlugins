package com.a.one_library;

import android.os.Bundle;

import com.a.router_processor.ARouter;
import com.a.router_processor.Parameter;
import com.a.api.arouter_api.ParameterManager;
import com.a.api.arouter_api.RouterManager;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

@ARouter(path = "/one_library/OneMainActivity")
public class OneMainActivity extends AppCompatActivity {


    @Parameter
    String mField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParameterManager.getInstance().loadParameter(this);
    }

    public void toTwoLibrary(View view) {
        RouterManager.getInstance()
                .build("/two_library/TwoMainActivity")
                .withString("mField", "mFieldOfOneLibrary")
                .navigation(this);
    }
}
