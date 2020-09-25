package com.jakutenshi.projects.umlplugin.container.entities;



import com.jakutenshi.projects.umlplugin.container.entities.attributes.Field;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Keyword;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Method;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.TypeParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by JAkutenshi on 28.05.2016.
 */
public class Class extends UMLEntity{
    private ArrayList<TypeParameter> typeParameters = new ArrayList<>();
    private ArrayList<Field> fields = new ArrayList<>();
    private ArrayList<Method> methods = new ArrayList<>();
    private String extendsClass = null;
    private ArrayList<String> implementInterfaces = new ArrayList<>();
    private boolean isUtility = false;

    //TODO GENERIC CLASS

    @Override
    public String titleToUML() {
        StringBuilder builder = new StringBuilder();
//если класс является службой
        if (isUtility) {
            builder.append("<<utility>> ");
        }
        builder.append(getName());
        return builder.toString();
    }

    @Override
    public String toUML() {
        StringBuilder builder = new StringBuilder();
        builder.append(titleToUML())
                .append("\n_____________________\n");
        for (Field field : fields) {
            builder.append(field.toUML())
                    .append("\n");
        }
        builder.append("_____________________\n");
        for (Method method : methods) {
            builder.append(method.toUML())
                    .append("\n");
        }
        return builder.toString();
    }

    @Override
    public String toCode() {
        //TODO
        return null;
    }

    public Class(String name, String packagePath, HashSet<Keyword> keywords, ArrayList<Field> fields, ArrayList<Method> methods, boolean isUtility) {
        super(name, packagePath, keywords);
        this.fields = fields;
        this.methods = methods;
        this.isUtility = isUtility;
    }

    public Class(String name, String packagePath, ArrayList<Field> fields, ArrayList<Method> methods, boolean isUtility) {
        super(name, packagePath);
        this.fields = fields;
        this.methods = methods;
        this.isUtility = isUtility;
    }

    public Class(ArrayList<Field> fields, ArrayList<Method> methods, boolean isUtility) {
        this.fields = fields;
        this.methods = methods;
        this.isUtility = isUtility;
    }

    public Class(String name, String packagePath, HashSet<Keyword> keywords) {
        super(name, packagePath, keywords);
    }

    public Class(String name, String packagePath) {
        super(name, packagePath);
    }

    public Class() {
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public ArrayList<Method> getMethods() {
        ArrayList<Method> methods = this.methods;
        Collections.reverse(methods);
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public boolean isUtility() {
        return isUtility;
    }

    public void setUtility(boolean utility) {
        isUtility = utility;
    }

    public ArrayList<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(ArrayList<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        typeParameters.add(typeParameter);
    }

    public String getExtendsClass() {
        return extendsClass;
    }

    public void setExtendsClass(String extendsClass) {
        this.extendsClass = extendsClass;
    }

    public ArrayList<String> getImplementInterfaces() {
        return implementInterfaces;
    }

    public void setImplementInterfaces(ArrayList<String> implementInterfaces) {
        this.implementInterfaces = implementInterfaces;
    }

    public void addImplementInterface(String anInterface) {
        implementInterfaces.add(anInterface);
    }
}
