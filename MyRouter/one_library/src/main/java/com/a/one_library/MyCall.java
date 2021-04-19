package com.a.one_library;


import com.a.router_processor.ARouter;
import com.a.api.call.ICall;

@ARouter(path = "/one_library/MyCall")
public class MyCall implements ICall {

    @Override
    public int getId() {
        return 1;
    }
}
