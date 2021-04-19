package com.a.router_processor.utils;

public interface ProcessorConfig {

    /**
     * @ARouter注解 的 包名 + 类名
     */
    String AROUTER_PACKAGE = "com.a.router_processor.ARouter";

    /**
     * @Parameter注解
     */
    String PARAMETER_PACKAGE = "com.a.router_processor.Parameter";

    /**
     * 接收参数的TAG标记 接收 每个module名称
     */
    String OPTIONS = "moduleName";

    /**
     * 接收 包名（APT 存放的包名）
     */
    String APT_PACKAGE = "packageNameForAPT";

    String ACTIVITY_PACKAGE = "android.app.Activity";

    /**
     * ARouter api 包名
     */
    String AROUTER_API_PACKAGE = "com.a.api.arouter_api";

    /**
     * ARouter api 的 ARouterGroup 高层标准
     */
    String AROUTER_API_GROUP = AROUTER_API_PACKAGE + ".ARouterGroup";

    /**
     * ARouter api 的 ARouterPath 高层标准
     */
    String AROUTER_API_PATH = AROUTER_API_PACKAGE + ".ARouterPath";
    String AROUTER_API_CALL = AROUTER_API_PACKAGE+".Call";

    String AROUTER_AIP_PARAMETER_GET = AROUTER_API_PACKAGE + ".ParameterGet";

    /**
     * 路由组，中的 Path 里面的 方法名
     */
    String PATH_METHOD_NAME = "getPathMap";

    /**
     * 路由组，中的 Group 里面的 方法名
     */
    String GROUP_METHOD_NAME = "getGroupMap";
    /**
     * 路由组，中的 Path 里面 的 变量名 1
     */
    String PATH_VAR1 = "pathMap";

    /**
     * 路由组，中的 Group 里面 的 变量名 1
     */
    String GROUP_VAR1 = "groupMap";

    /**
     * 路由组，PATH 最终要生成的 文件名
     */
    String PATH_FILE_NAME = "ARouter$$Path$$";

    /**
     * 路由组，GROUP 最终要生成的 文件名
     */
    String GROUP_FILE_NAME = "ARouter$$Group$$";

    String PARAMETER_NAME = "targetParameter";
    String PARAMETER_METHOD_NAME = "getParameter";
    String STRING = "java.lang.String";

    String PARAMETER_FILE_NAME = "$$Parameter";
    String ROUTER_MANAGER = "RouterManager";


}
