package com.a.api.arouter_api;

import android.app.Activity;
import android.util.Log;

import androidx.collection.LruCache;


public class ParameterManager {

    private static final String TAG ="ParameterManager" ;
    private static ParameterManager instance;

    private LruCache<String, ParameterGet> cache;

    private static String FILE_SUFFIX_NAME = "$$Parameter";

    private ParameterManager() {
        cache = new LruCache<>(100);
    }

    public static ParameterManager getInstance() {
        if (instance == null) {
            synchronized (ParameterManager.class) {
                if (instance == null) {
                    instance = new ParameterManager();
                }
            }
        }
        return instance;
    }

    public void loadParameter(Activity activity) {
        String name = activity.getClass().getName();
        ParameterGet parameterGet = cache.get(name);
        if (parameterGet == null) {
            try {
                Log.d(TAG, "Class.forName: "+name + FILE_SUFFIX_NAME);
                Class<?> aClass = Class.forName(name + FILE_SUFFIX_NAME);
                Log.d(TAG, "loadParameter: "+aClass.getName());
                parameterGet = (ParameterGet) aClass.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        assert parameterGet != null;
        parameterGet.getParameter(activity);
    }

}
