package com.jakutenshi.projects.umlplugin.drawers;

import com.jakutenshi.projects.umlplugin.container.entities.Class;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Field;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Keyword;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Method;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by JAkutenshi on 31.05.2016.
 */
public class ClassDrawer extends UMLDrawer {
    private LinkedList<DrawnLine> drawnFields;
    private LinkedList<DrawnLine> drawnMethods;



    public ClassDrawer(UMLEntity entity) {
        super(entity);
    }

    @Override
    public void draw(Graphics2D g) {
        int currentY = getY() + FRAME_MARGIN + SYMBOL_HEIGHT;

        // draw a rectangle
        drawFrame(g);

        // header
        currentY = drawTitle(currentY, g);

        // separator
        currentY = drawSeparator(currentY, g);

        // fields
        currentY = drawSection(currentY, drawnFields, g);

        // separator
        currentY = drawSeparator(currentY, g);

        // methods
        drawSection(currentY, drawnMethods, g);

        // generics
        if (generics != null) {
            generics.draw(g);
        }

    }

    @Override
    protected void fillContent(UMLEntity entity) {
        Class aClass = (Class) entity;

        int maxLength = getDrawnTitle().getLine().length();
//поля
        drawnFields = new LinkedList<>();
        for(Field field : aClass.getFields()) {
            drawnFields.addFirst(new DrawnLine(field.toUML()));
            if (drawnFields.getFirst().getLine().length() > maxLength) {
                maxLength = drawnFields.getFirst().getLine().length();
            }
            if (field.getKeywords().contains(Keyword.STATIC)) {
                makeFontUnderlined(drawnFields.getFirst());
            }
        }
//методы
        drawnMethods = new LinkedList<>();
        for (Method method : aClass.getMethods()) {
            drawnMethods.addFirst(new DrawnLine(method.toUML()));
            if (drawnMethods.getFirst().getLine().length() > maxLength) {
                maxLength = drawnMethods.getFirst().getLine().length();
            }
            if (method.getKeywords().contains(Keyword.STATIC)) {
               makeFontUnderlined(drawnMethods.getFirst());
            } else if (method.getKeywords().contains(Keyword.ABSTRACT)) {
                drawnMethods.getFirst().setFont(ITALIC_FONT);
            }
        }
//генерики
        if (aClass.getTypeParameters().size() != 0) {
            generics = new TypeParameterDrawer(aClass);
        }

        setFrameWidth(maxLength * SYMBOL_WIDTH + 2 * FRAME_MARGIN);
        setFrameHeight((SYMBOL_HEIGHT + LINE_SPACING) //заголовок
                + SEPARATOR_HEIGHT * 2
                + (drawnFields.size() ==  0 ? 1 : drawnFields.size()) * DRAWN_LINE_HEIGHT
                + (drawnMethods.size() ==  0 ? 1 : drawnMethods.size()) * DRAWN_LINE_HEIGHT
                + 2 * FRAME_MARGIN);
        if (generics != null) {
            setFrameWidth(getFrameWidth() + generics.getFrameWidth() - FRAME_MARGIN * 3);
            setFrameHeight(getFrameHeight() + generics.getFrameHeight() - FRAME_MARGIN);
        }
    }
}
