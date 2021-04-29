package com.jakutenshi.projects.umlplugin.container.entities;


import com.jakutenshi.projects.umlplugin.container.DiagramContainer;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Keyword;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Scope;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by JAkutenshi on 28.05.2016.
 */
public abstract class UMLEntity implements Generatable {
    private String name = "noName";
    private String packagePath = "noPackage";
    private Scope scope = Scope.PACKAGE;
    private HashSet<Keyword> keywords = new HashSet<>();

    private ArrayList<String> innerEntities = new ArrayList<>();

    public UMLEntity(String name, String packagePath, HashSet<Keyword> keywords) {
        this.name = name;
        this.packagePath = packagePath;
        this.keywords = keywords;
    }

    public UMLEntity(String name, String packagePath) {
        this.name = name;
        this.packagePath = packagePath;
    }

    public UMLEntity() {
    }

    public HashSet<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashSet<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public abstract String titleToUML();

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public ArrayList<String> getInnerEntities() {
        return innerEntities;
    }

    public void setInnerEntities(ArrayList<String> innerEntities) {
        this.innerEntities = innerEntities;
    }

    public void addInnerEntities(String packagePath) {
        innerEntities.add(packagePath);
    }

    public String getOuterEntityName() {
        int packagePrefix = packagePath.length() - name.length() - 1;
        String outerEntityName = null;
        if (packagePrefix >= 0) {
             outerEntityName = packagePath.substring(0, packagePrefix);
        }
        if (outerEntityName != null &&
                DiagramContainer.getInstance().containsKey(outerEntityName)) {
            return outerEntityName;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, packagePath);
    }
}
