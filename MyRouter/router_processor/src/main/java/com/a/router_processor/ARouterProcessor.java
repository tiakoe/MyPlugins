package com.a.router_processor;

import com.a.router_processor.bean.RouterBean;
import com.a.router_processor.utils.ProcessorConfig;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({ProcessorConfig.AROUTER_PACKAGE}) // 支持的注解类型
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({ProcessorConfig.OPTIONS, ProcessorConfig.APT_PACKAGE}) //注解处理接收的参数
public class ARouterProcessor extends AbstractProcessor {

    /**
     * 操作Element工具类（类、函数、属性）
     */
    private Elements elementUtils;

    /**
     * 打印消息日志
     */
    private Messager messager;

    /**
     * 文件生成器，
     */
    private Filer filer;

    /**
     * type(类信息)的工具类，包含用于操作TypeMirror的工具方法
     */
    private Types typeUtils;

    private String moduleName;
    private String packageName;

    private Map<String, List<RouterBean>> allPathMap = new HashMap<>();
    private Map<String, String> allGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        typeUtils = processingEnvironment.getTypeUtils();

        moduleName = processingEnvironment.getOptions().get(ProcessorConfig.OPTIONS);
//        messager.printMessage(Diagnostic.Kind.NOTE, "moduleName "+moduleName);
        packageName = processingEnvironment.getOptions().get(ProcessorConfig.APT_PACKAGE);
//        messager.printMessage(Diagnostic.Kind.NOTE, "packageName "+packageName);

        if (com.a.router_processor.utils.ProcessorUtils.isEmptyString(moduleName)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "使用@ARouter，需要传入模块名");
        }

        if (com.a.router_processor.utils.ProcessorUtils.isEmptyString(packageName)) {
            packageName = "com.a.new_module_custom_router";
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "并没有发现 被@ARouter注解的地方呀");
            return false;
            // 没有机会处理
        }
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        messager.printMessage(Diagnostic.Kind.NOTE, "开始process");

        TypeElement activityType = elementUtils.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
        TypeMirror activityMirror = activityType.asType();

        TypeElement callType = elementUtils.getTypeElement(ProcessorConfig.AROUTER_API_CALL);
        TypeMirror callMirror = callType.asType();

        // 循环被@ARouter注解的类
        for (Element element : elements) {
            // 获取简单类名
            String simpleName = element.getSimpleName().toString();
            // 获取注解
           ARouter aRouter = element.getAnnotation(ARouter.class);

            RouterBean routerBean = new RouterBean.Builder()
                    .addGroup(aRouter.group())
                    .addPath(aRouter.path())
                    .addElement(element)
                    .build();
            TypeMirror typeMirror = element.asType();

            if (typeUtils.isSubtype(typeMirror, activityMirror)) {
                routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
            } else if (typeUtils.isSubtype(typeMirror, callMirror)) {
                routerBean.setTypeEnum(RouterBean.TypeEnum.CALL);
                messager.printMessage(Diagnostic.Kind.NOTE, "callMirror: " + callMirror.toString());
            } else {
                throw new RuntimeException("暂不支持此类型");
            }

            if (checkRouterPath(routerBean)) {
                List<RouterBean> routerBeans = allPathMap.get(routerBean.getGroup());
                if (com.a.router_processor.utils.ProcessorUtils.isEmptyList(routerBeans)) {
                    routerBeans = new ArrayList<>();
                    routerBeans.add(routerBean);
                    allPathMap.put(routerBean.getGroup(), routerBeans);
                } else {
                    routerBeans.add(routerBean);
                }
            } else {
                messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/app/MainActivity");
            }
        }

        TypeElement pathType = elementUtils.getTypeElement(ProcessorConfig.AROUTER_API_PATH);
        TypeElement groupType = elementUtils.getTypeElement(ProcessorConfig.AROUTER_API_GROUP);

        try {
            messager.printMessage(Diagnostic.Kind.NOTE, "开始生成路由Path文件");
            createPathFile(pathType);
        } catch (Exception e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, "生成路由Path文件出错，errorMsg：" + e.getMessage());
        }

        try {
            createGroupFile(groupType, pathType);
        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, "生成路由Group文件出错，errorMsg：" + e.getMessage());
        }

        return true;
    }

    private boolean checkRouterPath(RouterBean routerBean) {
        String path = routerBean.getPath();
        String group = routerBean.getGroup();
        if (com.a.router_processor.utils.ProcessorUtils.isEmptyString(path) || !path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的path值，必须要以 / 开头");
            return false;
        }

        if (path.lastIndexOf("/") == 0) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/app/MainActivity");
            return false;
        }

        String finalGroup = path.substring(1, path.indexOf("/", 1));

        if (!com.a.router_processor.utils.ProcessorUtils.isEmptyString(group) && !group.equals(moduleName)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的group值必须和子模块名一致！");
            return false;
        } else {
            routerBean.setGroup(finalGroup);
        }
        return true;
    }

   /* public class ARouter$$Path$$app implements ARouterPath {
        @Override
        public Map<String, RouterBean> getPathMap() {
            Map<String, RouterBean> pathMap = new HashMap<>();
            pathMap.put("/app/Main2Activity", RouterBean.create(RouterBean.TypeEnum.ACTIVITY, Main2Activity.class, "/app/Main2Activity", "app"));
            pathMap.put("/app/MainActivity", RouterBean.create(RouterBean.TypeEnum.ACTIVITY, MainActivity.class, "/app/MainActivity", "app"));
            return pathMap;
        }
    }*/

    /**
     * @param pathType 生成模板类的夫类型
     * @throws IOException 生成失败抛出的异常
     */
    private void createPathFile(TypeElement pathType) throws IOException {
        if (allPathMap.isEmpty()) {
            return;
        }

        // 返回值类型 Map<String, RouterBean>
        TypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class));

        for (Map.Entry<String, List<RouterBean>> entry : allPathMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(ProcessorConfig.PATH_METHOD_NAME)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .returns(returnType);

            // Map<String, RouterBean> pathMap = new HashMap<>();
            methodBuilder.addStatement("$T<$T, $T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    ProcessorConfig.PATH_VAR1,
                    ClassName.get(HashMap.class)
            );

            List<RouterBean> routerBeans = entry.getValue();
            for (RouterBean routerBean : routerBeans) {
                // 循环遍历变量pathMap放入值
                // pathMap.put("/app/Main2Activity", RouterBean.create(RouterBean.TypeEnum.ACTIVITY, Main2Activity.class, "/app/Main2Activity", "app"));
                methodBuilder.addStatement("$N.put($S, $T.create($T.$L, $T.class, $S, $S))",
                        ProcessorConfig.PATH_VAR1,
                        routerBean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.TypeEnum.class),
                        routerBean.getTypeEnum(),
                        ClassName.get((TypeElement) routerBean.getElement()),
                        routerBean.getPath(),
                        routerBean.getGroup()
                );
            }

            // 方法添加返回值 return pathMap;
            methodBuilder.addStatement("return $N", ProcessorConfig.PATH_VAR1);

            // ARouter$$Path$$app 最终生成的类名
            String pathName = ProcessorConfig.PATH_FILE_NAME + entry.getKey();

            messager.printMessage(Diagnostic.Kind.NOTE, "APT生成路由Path类文件：" + packageName + "." + pathName);

            // 生成java文件
            JavaFile.builder(
                    // 包名  APT 存放的路径
                    packageName,
                    // 类名
                    TypeSpec.classBuilder(pathName)
                            // 实现ARouterLoadPath接口  implements ARouterPath==pathType
                            .addSuperinterface(ClassName.get(pathType))
                            // public修饰符
                            .addModifiers(Modifier.PUBLIC)
                            // 方法的构建（方法参数 + 方法体）
                            .addMethod(methodBuilder.build())
                            .build()) // 类构建完成
                    .build() // JavaFile构建完成
                    .writeTo(filer);

            allGroupMap.put(entry.getKey(), pathName);
        }

    }

    /*public class ARouter$$Group$$app implements ARouterGroup {
        @Override
        public Map<String, Class<? extends ARouterPath>> getGroupMap() {
            Map<String, Class<? extends ARouterPath>> groupMap = new HashMap<>();
            groupMap.put("app", ARouter$$Path$$app.class);
            return groupMap;
        }
    }*/
    private void createGroupFile(TypeElement groupType, TypeElement pathType) throws IOException {
        if (allGroupMap.isEmpty() || allPathMap.isEmpty()) {
            return;
        }

        // 方法的返回值类型 Map<String, Class<? extends ARouterPath>>
        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                // Map
                ClassName.get(Map.class),
                ClassName.get(String.class),
                // Class<? extends ARouterPath>
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        // ? extends ARouterPath
                        WildcardTypeName.subtypeOf(ClassName.get(pathType)))
        );

        MethodSpec.Builder builder = MethodSpec.methodBuilder(ProcessorConfig.GROUP_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(Override.class))
                .returns(returnType);

        //  Map<String, Class<? extends ARouterPath>> groupMap = new HashMap<>();
        builder.addStatement("$T<$T, $T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathType))),
                ProcessorConfig.GROUP_VAR1,
                ClassName.get(HashMap.class)
        );

        for (Map.Entry<String, String> entry : allGroupMap.entrySet()) {
            // groupMap.put("app", ARouter$$Path$$app.class);
            builder.addStatement("$N.put($S, $T.class)",
                    ProcessorConfig.GROUP_VAR1,
                    entry.getKey(),
                    ClassName.get(packageName, entry.getValue())
            );
        }

        // return groupMap;
        builder.addStatement("return $N", ProcessorConfig.GROUP_VAR1);

        String className = ProcessorConfig.GROUP_FILE_NAME + moduleName;
        messager.printMessage(Diagnostic.Kind.NOTE, "APT生成路由Path类文件：" + packageName + "." + className);

        JavaFile.builder(packageName,
                // 类名
                TypeSpec.classBuilder(className)
                        // 类的父类
                        .addSuperinterface(ClassName.get(groupType))
                        // 添加方法
                        .addMethod(builder.build())
                        // 添加修饰符
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .build()
                .writeTo(filer);
    }


/*
    //模板1
    //package com.example.helloworld;

    //public final class HelloWorld {

    //    public static void main(String[] args) {
    //        System.out.println("Hello, JavaPoet!");
    //    }
    //}

     // 方法
     MethodSpec methodSpec = MethodSpec.methodBuilder("main")  // 方法名
             .addModifiers(Modifier.PUBLIC, Modifier.FINAL)  // 方法限定符
             .returns(void.class)  // 返回值类型
             .addParameter(String[].class, "args") // 参数类型
             .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!") // 方法美容
             .build();

     // 类
     TypeSpec test = TypeSpec.classBuilder("Test")  //类名
             .addMethod(methodSpec) // 添加方法
             .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
             .build();

     // 包
     JavaFile javaFile = JavaFile.builder("com.a.test", test)
             .build();

     // 生成文件
     try {
         javaFile.writeTo(filer);
     } catch (IOException e) {
         e.printStackTrace();
         messager.printMessage(Diagnostic.Kind.ERROR,"生成test文件失败，msg：" + e.getMessage());
     }

    //模板：
    //public class MainActivity3$$$$$$$$$ARouter {

    //    public static Class findTargetClass(String path) {
    //        return path.equals("/app/MainActivity3") ? MainActivity3.class : null;
    //    }

    //}

    // 包信息
    String packName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, packName);

    // 简单类名
    String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE, "使用ARoute注解" + className);
    String targetName = className + "$$ARouter";

    ARouter aRouter = element.getAnnotation(ARouter.class);

    MethodSpec methodSpec = MethodSpec.methodBuilder("findTargetClass")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .returns(String.class)
            .addParameter(String.class, "path")
            .addStatement("return path.equals($S) ? $T.class.getName() : null",
                    aRouter.path(),
                    ClassName.get((TypeElement) element))
            .build();

    TypeSpec typeSpec = TypeSpec.classBuilder(targetName)
            .addMethod(methodSpec)
            .addModifiers(Modifier.PUBLIC)
            .build();

    JavaFile javaFile = JavaFile.builder(packName, typeSpec).build();

            try {
        javaFile.writeTo(filer);
    } catch (IOException e) {
        e.printStackTrace();
        messager.printMessage(Diagnostic.Kind.ERROR, "生成test文件失败，msg：" + e.getMessage());
    }*/
}
