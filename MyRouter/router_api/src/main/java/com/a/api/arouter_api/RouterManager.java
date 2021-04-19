package com.a.api.arouter_api;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.collection.LruCache;

import com.a.router_processor.bean.RouterBean;

import java.util.regex.Pattern;

/**
 * 路由管理器
 */
public class RouterManager {

    private final String TAG="RouterManager";
    private String FILE_SUFFIX_NAME = "ARouter$$Group$$";
    //目标group
    private String group;
    //目标path
    private String path;

    private static RouterManager instance;
    private LruCache<String, ARouterGroup> groupLruCache;
    private LruCache<String, ARouterPath> pathLruCache;

    private RouterManager() {
        groupLruCache = new LruCache<>(100);
        pathLruCache = new LruCache<>(100);
    }

    public static RouterManager getInstance() {
        if (instance == null) {
            synchronized (RouterManager.class) {
                if (instance == null) {
                    instance = new RouterManager();
                }
            }
        }
        return instance;
    }

    public BundleManager build(String path) {

        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("路径名不能为空");
        }

        String regex = "^/\\w+/\\w+$";
        boolean matches = Pattern.matches(regex, path);
        if (!matches) {
            throw new IllegalArgumentException("路径名不符合规范，正确写法：如 /order/Order_MainActivity");
        }

        String substring = path.substring(1, path.indexOf("/", 1));
        this.path = path;
        this.group = substring;
        return new BundleManager();
    }

    /**
     * ARouter$$Group$$app
     *
     * @param context
     * @param bundleManager
     * @return
     */
    public Object navigation(Context context, BundleManager bundleManager) {
        String groupClassName = context.getPackageName() + "." + FILE_SUFFIX_NAME + group;
        Log.d(TAG, "groupClassName: "+groupClassName);

        try {
            ARouterGroup aRouterGroup = groupLruCache.get(group);
            if (aRouterGroup == null) {
                Class<?> aClass = Class.forName(groupClassName);
                aRouterGroup = (ARouterGroup) aClass.newInstance();
                groupLruCache.put(group, aRouterGroup);
            }

            if (aRouterGroup.getGroupMap().isEmpty()) {
                throw new NullPointerException("路由表Group是空的");
            }

            //  缓存中没有取到，就创建一个
            ARouterPath aRouterPath = pathLruCache.get(path);
            if (aRouterPath == null) {
                //                aClass 指的是RouterBean
                Class<? extends ARouterPath> aClass = aRouterGroup.getGroupMap().get(group);
                Log.d(TAG, "navigation: "+aClass.getName());
                aRouterPath = aClass.newInstance();
                pathLruCache.put(path, aRouterPath);
            }

            if (!aRouterPath.getPathMap().isEmpty()) {
                RouterBean routerBean = aRouterPath.getPathMap().get(path);
                if (routerBean != null) {
                    switch (routerBean.getTypeEnum()) {
                        case ACTIVITY:
                            Log.d(TAG, "navigation: ACTIVITY");
                            Intent intent = new Intent(context, routerBean.getMyClass());
                            intent.putExtras(bundleManager.getBundle());
                            context.startActivity(intent, bundleManager.getBundle());
                            break;
                        case CALL:
                            Log.d(TAG, "navigation: CALL");
                            Class<?> clazz = routerBean.getMyClass();
                            Call call = (Call) clazz.newInstance();
                            bundleManager.setCall(call);
                            return bundleManager.getCall();
                        default:
                            Log.d(TAG, "navigation: default");
                            break;
                    }
                }
            } else {
                throw new NullPointerException("路由表Path是空的");
            }

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
