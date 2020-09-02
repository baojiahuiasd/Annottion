package com.bjh.annotion_complie;

import com.bjh.annotion_lib.BindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    private Filer filer;
    private int i = 0;
    private Messager messager;
    private Map<String, ClassCreatorProxy> map = new HashMap<>();
    private Elements elementUtils;

    @Override

    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        System.out.println("-------BindViewProcessor init");
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("-------BindViewProcessor process");
//        if (i == 0) {
//            MethodSpec methodSpec = MethodSpec.methodBuilder("main").addModifiers(Modifier.PUBLIC)
//                    .addParameter(String.class, "string")
//                    .addStatement("$T.out.print($S+$N)", System.class, "helloWorld", "string")
//                    .build();
//            TypeSpec typeSpec = TypeSpec.classBuilder("HelloWord").addMethod(methodSpec).build();
//            JavaFile javaFile = JavaFile.builder("com.bjh.annottion", typeSpec).build();
//            try {
//                javaFile.writeTo(filer);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            i++;
//        }
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsAnnotatedWith) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            String className = typeElement.getQualifiedName().toString();
            System.out.print("-----------------------------------" + className);
            System.out.print("-----------------------------------" + typeElement.getSimpleName().toString());
            //得到创建好的类
            ClassCreatorProxy classCreatorProxy = map.get(className);
            if (classCreatorProxy == null) {
                classCreatorProxy = new ClassCreatorProxy(typeElement, elementUtils);
                map.put(className, classCreatorProxy);
            }
            //获取注解的参数
            BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
            int id = bindAnnotation.value();
            classCreatorProxy.put(id, variableElement);
        }
        createFile();
        return true;
    }

    private void createFile() {
        for (String className : map.keySet()) {
            ClassCreatorProxy classCreatorProxy = map.get(className);
            try {
                JavaFile.builder(classCreatorProxy.classPack, classCreatorProxy.generateJavaCodeByPoet()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet();
        set.add(BindView.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}