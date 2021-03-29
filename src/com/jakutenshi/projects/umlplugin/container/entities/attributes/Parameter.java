package com.jakutenshi.projects.umlplugin.container.entities.attributes;

import java.util.HashSet;

/**
 * Created by JAkutenshi on 28.05.2016.
 */
public class Parameter extends EnumConstant {
    private HashSet<Keyword> keywords = getKeywords();
    private String type;
    private String typePath;
    private boolean toDraw = true;

    @Override
    public String toUML() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toUML())
                .append(" : ")
                .append(type);
        if(keywords.contains(Keyword.FINAL))
            builder.append(" {readOnly}");
        return builder.toString();
    }

    @Override
    public String toCode() {
        StringBuilder builder = new StringBuilder();
        if (keywords.contains(Keyword.FINAL))
            builder.append("final ");
        builder.append(type)
                .append(" ")
                .append(super.toUML());
        return builder.toString();
    }

    public Parameter(String name, HashSet<Keyword> keywords, String type) {
        super(name);
        this.keywords = keywords;
        this.type = type;
    }

    public Parameter(String name, String type) {
        super(name);
        this.type = type;
    }

    public Parameter(HashSet<Keyword> keywords, String type) {
        this.keywords = keywords;
        this.type = type;
    }

    public Parameter() {

    }

    public void setToDraw(boolean toDraw) {
        this.toDraw = toDraw;
    }

    public boolean isToDraw() {
        return toDraw;
    }

    public HashSet<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashSet<Keyword> keywords) {
        this.keywords = keywords;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypePath() {
        return typePath;
    }

    public void setTypePath(String typePath) {
        this.typePath = typePath;
    }
}
