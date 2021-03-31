package com.jakutenshi.projects.umlplugin.parsers;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeParameter;
import com.jakutenshi.projects.umlplugin.container.entities.Class;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Keyword;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Method;
import java.util.ArrayList;

/**
 * Created by JAkutenshi on 28.05.2016.
 */
public class ClassParser extends UMLEntityParser {
    @Override
    public UMLEntity parse(PsiClass psiClass){
        Class aClass = new Class();
//пакет
        aClass.setPackagePath(psiClass.getQualifiedName());
//модификаторы
        ModifierParser modifierParser = new ModifierParser();
        modifierParser.parse(psiClass.getModifierList());
        aClass.setScope(modifierParser.getParseScope());
        aClass.setKeywords(modifierParser.getParseKeywords());
//имя
        aClass.setName(psiClass.getName());
//генерик-типы
        PsiTypeParameter[] psiTypeParameters = psiClass.getTypeParameters();
        if (psiTypeParameters != null) {
            for (PsiTypeParameter psiTypeParameter : psiTypeParameters) {
                aClass.addTypeParameter(parseTypeParameter(psiTypeParameter));
            }
        }
//поля
        PsiField[] fields = psiClass.getFields();
        for (PsiField psiField : fields) {
            aClass.addField(parseField(psiField));
        }
//методы
        PsiMethod[] psiMethods = psiClass.getMethods();
        for (PsiMethod psiMethod : psiMethods) {
            aClass.addMethod(parseMethod(psiMethod));
        }
        //если класс является службой
        System.out.println(aClass.getMethods());
        ArrayList<Method> methods = aClass.getMethods();
        boolean isUtility=true;
        int i = 0;

        while(isUtility && i < methods.size()) {
            if(!methods.get(i).getKeywords().contains(Keyword.STATIC)){
                isUtility = false;
            }
            i++;
        }
        if (isUtility) aClass.setUtility(true);


//является ли наследником
        aClass.setExtendsClass(parseExtendsEntity(psiClass));
//список реализующих интерфейсов
        aClass.setImplementInterfaces(parseImplementInterfases(psiClass));

        return aClass;
    }
}
