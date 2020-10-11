package com.jakutenshi.projects.umlplugin.drawers;

import com.jakutenshi.projects.umlplugin.container.entities.Class;
import com.jakutenshi.projects.umlplugin.container.entities.Interface;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.TypeParameter;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by JAkutenshi on 03.06.2016.
 */
public class TypeParameterDrawer extends UMLDrawer {
    private LinkedList<DrawnLine> drawnTypeParameters = new LinkedList<>();

    public TypeParameterDrawer(UMLEntity entity) {
        fillContent(entity);
    }

    @Override
    public void draw(Graphics2D g) {
        MaxDrawerPoints.checkForMax(getX() + getFrameWidth(), getY() + getFrameHeight());
        g.setColor(Color.WHITE);
        g.fillRoundRect(getX(), getY(), getFrameWidth(), getFrameHeight(), 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(getX(), getY(), getFrameWidth(), getFrameHeight(), 10, 10);
        int currentY = getY() + FRAME_MARGIN + SYMBOL_HEIGHT;
        drawSection(currentY, drawnTypeParameters, g);
    }

    @Override
    protected void fillContent(UMLEntity entity) {
        Class aClass;
        Interface anInterface;
        ArrayList<TypeParameter> parameters;
        if (entity instanceof Class) {
            aClass = (Class) entity;
            parameters = aClass.getTypeParameters();
        } else {
            anInterface = (Interface) entity;
            parameters = anInterface.getTypeParameters();
        }
        fillTypes(parameters);
    }

    private void fillTypes(ArrayList<TypeParameter> parameters) {
        int width = 0;
        for (TypeParameter parameter : parameters) {
            drawnTypeParameters.addFirst(new DrawnLine(parameter.toUML()));
            if (drawnTypeParameters.getFirst().getLine().length() > width) {
                width = drawnTypeParameters.getFirst().getLine().length();
            }
        }

        setFrameHeight(FRAME_MARGIN * 2
                + drawnTypeParameters.size() * DRAWN_LINE_HEIGHT);
        setFrameWidth(FRAME_MARGIN * 2 + SYMBOL_WIDTH * width);
    }

    public LinkedList<DrawnLine> getDrawnTypeParameters() {
        return drawnTypeParameters;
    }
}
