package com.a.router_processor;

import com.a.router_processor.utils.ProcessorConfig;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Parameter注解处理器
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(ProcessorConfig.PARAMETER_PACKAGE)
public class ParameterProcessor extends AbstractProcessor {

    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!set.isEmpty()) {
            // 获取被@Parameter注解的变量
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Parameter.class);
            for (Element element : elements) {
                // 获取父节点
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                if (!tempParameterMap.containsKey(enclosingElement)) {
                    List<Element> elementList = new ArrayList<>();
                    elementList.add(element);
                    tempParameterMap.put(enclosingElement, elementList);
                } else {
                    tempParameterMap.get(enclosingElement).add(element);
                }
            }

            if (tempParameterMap.isEmpty()) {
                return true;
            }

            //public class Personal_MainActivity$$Parameter implements ParameterGet {
            //    @Override
            //    public void getParameter(Object targetParameter) {
            //        Personal_MainActivity t = (Personal_MainActivity) targetParameter;
            //        t.name = t.getIntent().getStringExtra("name");
            //        t.sex = t.getIntent().getStringExtra("sex");
            //        t.age = t.getIntent().getIntExtra("age", t.age);
            //    }
            //}

            TypeElement activityType = elementUtils.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
            messager.printMessage(Diagnostic.Kind.NOTE, activityType.asType().toString());
            TypeElement parameterType = elementUtils.getTypeElement(ProcessorConfig.AROUTER_AIP_PARAMETER_GET);
            messager.printMessage(Diagnostic.Kind.NOTE, parameterType.asType().toString());
            // getParameter()方法的传参
            ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, ProcessorConfig.PARAMETER_NAME).build();

            for (TypeElement typeElement : tempParameterMap.keySet()) {

                messager.printMessage(Diagnostic.Kind.NOTE, typeElement.asType() + "typeElement.asType()");
                messager.printMessage(Diagnostic.Kind.NOTE, activityType.asType() + "activityType.asType()");
                if (!typeUtils.isSubtype(typeElement.asType(), activityType.asType())) {
                    // @Parameter注解需要注解在Activity类的字段上
                    throw new RuntimeException("@Parameter注解需要注解在Activity类的字段上" + typeElement.asType());
                }

                // 获取使用@Parameter的类名
                ClassName className = ClassName.get(typeElement);
                MethodSpec methodSpec = new ParameterMethodFactory.Builder(parameterSpec)
                        .setClassName(className)
                        .setMessager(messager)
                        .setElementUtils(elementUtils)
                        .setTypeUtils(typeUtils)
                        .build()
                        .buildStatement(tempParameterMap.get(typeElement))
                        .buildMethodSpec();

                String finalClassName = className.simpleName() + ProcessorConfig.PARAMETER_FILE_NAME;

                // 生成类 类名
                TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                        // 类的修饰符
                        .addModifiers(Modifier.PUBLIC)
                        // 类的继承关系
                        .addSuperinterface(ClassName.get(parameterType))
                        // 类的方法
                        .addMethod(methodSpec)
                        .build();

                try {
                    JavaFile.builder(className.packageName(), typeSpec)
                            .build()
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
