package com.jakutenshi.projects.umlplugin.parsers;

import com.intellij.psi.*;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.*;

import java.util.ArrayList;

/**
 * Created by JAkutenshi on 28.05.2016.
 */
public abstract class UMLEntityParser {
    protected static ModifierParser modifierParser;

    public abstract UMLEntity parse(PsiClass psiClass);

    public EnumConstant parseEnumConstant(PsiEnumConstant psiEnumConstant) {
        return new EnumConstant(psiEnumConstant.getName());
    }

    public Field parseField(PsiField psiField) {
        Field field = new Field();
//моификаторы
        ModifierParser modifierParser = new ModifierParser();
        modifierParser.parse(psiField.getModifierList());
        field.setScope(modifierParser.getParseScope());
        field.setKeywords(modifierParser.getParseKeywords());
//тип
        field.setType(psiField.getType().getPresentableText());
        //field.setTypePath(psiField.getType().getCanonicalText());
//имя
        field.setName(psiField.getName());

        return field;
    }

    public Method parseMethod(PsiMethod psiMethod) {
        Method method = new Method();
//модификаторы
        ModifierParser modifierParser = new ModifierParser();
        modifierParser.parse(psiMethod.getModifierList());
        method.setScope(modifierParser.getParseScope());
        method.setKeywords(modifierParser.getParseKeywords());
//генерик-типы
        PsiTypeParameter[] psiTypeParameters = psiMethod.getTypeParameters();
        if (psiTypeParameters != null) {
            for (PsiTypeParameter psiTypeParameter : psiTypeParameters) {
                method.addTypeParameter(parseTypeParameter(psiTypeParameter));
            }
        }
//возвращаемый тип
        if (psiMethod.getReturnType() != null) {
            method.setReturnType(psiMethod.getReturnType().getPresentableText());
        } else {
//если конструктор
            method.setReturnType(null);
        }
//имя
        method.setName(psiMethod.getName());

//параметры
        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        for (PsiParameter psiParameter : parameters) {
            method.addParameter(parseParameter(psiParameter));
        }

        return method;
    }

    public Parameter parseParameter(PsiParameter psiParameter) {
        Parameter parameter = new Parameter();
//имя
        parameter.setName(psiParameter.getName());
//тип
        parameter.setType(psiParameter.getType().getPresentableText());
//ключевые слова
        ModifierParser modifierParser = new ModifierParser();
        modifierParser.parse(psiParameter.getModifierList());
        parameter.setKeywords(modifierParser.getParseKeywords());

        return parameter;
    }

    public TypeParameter parseTypeParameter(PsiTypeParameter psiTypeParameter) {
        TypeParameter typeParameter = new TypeParameter(psiTypeParameter.getName());
        PsiClassType[] extendsTypes = psiTypeParameter.getExtendsListTypes();
        for (PsiClassType extendsType : extendsTypes) {
            typeParameter.setExtendsLimitation(extendsType.getCanonicalText());
        }
        return typeParameter;
    }

    public String parseExtendsEntity(PsiClass psiClass) {
        PsiClassType[] psiClassTypes = psiClass.getExtendsListTypes();
        for (PsiClassType psiClassType : psiClassTypes) {
            return psiClassType.getCanonicalText();
        }
        return null;
    }

    public ArrayList<String> parseImplementInterfases(PsiClass psiClass) {
        ArrayList<String> implementInterfacesList = new ArrayList<>();

        PsiClassType[] interfaces = psiClass.getImplementsListTypes();
        for (PsiClassType anInterface : interfaces) {
            implementInterfacesList.add(anInterface.getCanonicalText());
        }

        return implementInterfacesList;
    }
}
