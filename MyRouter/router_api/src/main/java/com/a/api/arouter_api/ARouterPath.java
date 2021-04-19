package com.a.api.arouter_api;


import com.a.router_processor.bean.RouterBean;

import java.util.Map;

public interface ARouterPath {

    /**
     * path和RouterBean 信息对应
     *
     * @return map
     */
    Map<String, RouterBean> getPathMap();
}
