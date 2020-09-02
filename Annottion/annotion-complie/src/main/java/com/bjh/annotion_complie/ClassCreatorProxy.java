package com.bjh.annotion_complie;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class ClassCreatorProxy {

    private String className;//类的名字
    public String classPack;//类的路径
    private Map<Integer, VariableElement> variableMap = new HashMap<>();
    private TypeElement typeElement;//类

    public ClassCreatorProxy(TypeElement typeElement, Elements elementUtils) {
        className = typeElement.getSimpleName().toString();
        PackageElement packageElement = elementUtils.getPackageOf(typeElement);
        classPack = packageElement.getQualifiedName().toString();
        this.typeElement = typeElement;
    }


    public void put(int id, VariableElement variableElement) {
        variableMap.put(id, variableElement);
    }

    public TypeSpec generateJavaCodeByPoet() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind").addModifiers(Modifier.PUBLIC).returns(void.class).addParameter(ClassName.bestGuess(typeElement.getQualifiedName().toString()), "host");
        for (Integer i : variableMap.keySet()) {
            VariableElement variableElement = variableMap.get(i);
            //
            String variableName = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
//            TextView textView=(TextView) findViewById(R.id.texts);
            methodBuilder.addCode("host." + variableName + "=" + "(" + type + ")(((android.app.Activity)host).findViewById(" + i + "));");
        }
        methodBuilder.build();
        TypeSpec typeSpec = TypeSpec.classBuilder(className + "_ViewBinding").addModifiers(Modifier.PUBLIC).addMethod(methodBuilder.build()).build();
        return typeSpec;
    }
}
