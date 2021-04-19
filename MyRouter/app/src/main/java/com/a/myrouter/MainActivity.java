package com.a.myrouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.a.router_processor.ARouter;
import com.a.router_processor.Parameter;
import com.a.api.arouter_api.ParameterManager;
import com.a.api.arouter_api.RouterManager;
import com.a.api.call.ICall;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity" ;
    @Parameter(name = "/one_library/MyCall")
    ICall iCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParameterManager.getInstance().loadParameter(this);
        Log.d(TAG, "iCall.getId: "+        iCall.getId());
    }

    public void toOneLibrary(View view) {
        RouterManager.getInstance()
                .build("/one_library/OneMainActivity")
                .withString("mField", "mFieldOfMainActivity")
                .navigation(this);
    }
}
