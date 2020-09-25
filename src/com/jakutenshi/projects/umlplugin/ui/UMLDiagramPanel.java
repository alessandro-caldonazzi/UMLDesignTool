package com.jakutenshi.projects.umlplugin.ui;

import com.jakutenshi.projects.umlplugin.container.DiagramContainer;
import com.jakutenshi.projects.umlplugin.container.entities.Class;
import com.jakutenshi.projects.umlplugin.container.entities.Interface;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Field;
import com.jakutenshi.projects.umlplugin.drawers.*;
import com.jakutenshi.projects.umlplugin.drawers.relationships.*;
import com.jakutenshi.projects.umlplugin.util.Options;
import com.jakutenshi.projects.umlplugin.util.UMLDiagramContainerObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JAkutenshi on 26.05.2016.
 */
public class UMLDiagramPanel extends JPanel implements UMLDiagramContainerObserver {
    private UMLDrawer draggedDrawer;
    private int startDragX;
    private int startDragY;
    private HashMap<String, UMLDrawer> drawers = new HashMap<>();
    private ArrayList<UMLRelationDrawer> arrows = new ArrayList<>();
    HashMap<String, UMLEntity> entities;
    private int maxDrawnEntityHeight;
    private int maxDrawnEntityWidth;
    private int currentX;
    private int currentY;
    private double scale = 1;

    private final int SPACE = 10;

    public UMLDiagramPanel() {
        setPreferredSize(new Dimension(5000, 5000));
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedDrawer != null) {
                    if (0 <= e.getX() && e.getX() + draggedDrawer.getFrameWidth() / 2 <= getSize().width
                            && 0 <= e.getY() && e.getY() + draggedDrawer.getFrameHeight() / 2 <= getSize().height) {
                        draggedDrawer.setAnchorX((int) (e.getX() / scale));
                        draggedDrawer.setAnchorY((int) (e.getY() / scale));
                        repaint();
                    }
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                getIt((int) (e.getX() / scale), (int) (e.getY() / scale), drawers);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedDrawer = null;
            }
        });

        DiagramContainer.getInstance().addObserver(this);
    }

    public void returnScaleToDefault() {
        scale = 1;
        applyZoom();
    }

    public void zoomIn() {
        scale *= 2;
        applyZoom();
    }

    public void zoomOut() {
        scale /= 2;
        applyZoom();
    }

    private void applyZoom() {
        setPreferredSize(new Dimension((int) scale * getWidth(), (int) scale * getHeight()));
        validate();
        repaint();
    }

    private void zoom(Graphics2D g) {
        g.scale(scale, scale);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        clearPane(g);
        zoom(g);

        if (drawers.size() == 0) {
            return;
        }

        for (String key : drawers.keySet()) {
            drawers.get(key).draw(g);
        }
        for (UMLRelationDrawer arrow : arrows) {
            arrow.drawArrow(g);
        }

    }

    @Override
    public void onChange(HashMap<String, UMLEntity> entities) {
        this.entities = entities;
        fillDrawnEntities();
        repaint();
    }

    private void fillDrawnEntities() {
        UMLEntity entity;
        UMLDrawer drawer;
        drawers = new HashMap<>();

        for (String key : entities.keySet()) {
            entity = entities.get(key);
            if (entity instanceof Class) {
                drawer = new ClassDrawer(entity);
            } else if (entity instanceof Interface) {
                drawer = new InterfaceDrawer(entity);
            } else {
                drawer = new EnumDrawer(entity);
            }
            drawer.setKey(entity.getPackagePath());
            drawers.put(drawer.getKey(), drawer);
            if (drawer.getFrameHeight() > maxDrawnEntityHeight) {
                maxDrawnEntityHeight = drawer.getFrameHeight();
            }
            if (drawer.getFrameWidth() > maxDrawnEntityWidth) {
                maxDrawnEntityWidth = drawer.getFrameWidth();
            }
        }

        int maxWidth = currentX * 2 + (maxDrawnEntityWidth + SPACE * 2) * drawers.size();
        int maxHeight = currentY * 2 + (maxDrawnEntityHeight + SPACE * 2) * drawers.size();

        setPreferredSize(new Dimension(maxWidth, maxHeight));

        createsRelations();
        fillCoordinates();

    }

    private void fillCoordinates() {
        UMLDrawer drawer;
        currentX = SPACE;
        currentY = SPACE;
        int rows = 1 + (int) Math.sqrt(drawers.size());
        Object[] keys = drawers.keySet().toArray();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if (i * rows + j < drawers.size()) {
                    drawer = drawers.get(keys[i * rows + j]);
                    drawer.setX(currentX);
                    drawer.setY(currentY);
                    currentX += maxDrawnEntityWidth + SPACE;
                } else {
                    break;
                }
            }
            currentY += maxDrawnEntityHeight + SPACE;
            currentX = 15;
        }
    }

    private void createsRelations() {

        UMLEntity entity;
        Class aClass;
        Interface anInterface;
        arrows = new ArrayList<>();

        for (String key : entities.keySet()) {
            entity = entities.get(key);
            if (entity instanceof Class) {
                aClass = (Class) entity;
//обобщение
                if (aClass.getExtendsClass() != null){
                    addRelarioship(aClass, aClass.getExtendsClass(), new Generalisation());
                }
//реализация
                if (aClass.getImplementInterfaces() != null && aClass.getImplementInterfaces().size() != 0) {
                    for (String anImplInterface : aClass.getImplementInterfaces()) {
                        addRelarioship(aClass, anImplInterface, new Realisation());
                    }
                }
//композиция
                if (!aClass.isUtility()) {
                    for (Field field : aClass.getFields()) {
                        //учитываем экземпляры самого класса
                        if (!Options.disableFieldPath && !field.getTypePath().equals(aClass.getPackagePath())) {
                            addRelarioship(aClass, field.getTypePath(), new Composition());
                        }
                    }
                }
            } else if (entity instanceof Interface) {
//обобщение
                anInterface = (Interface) entity;
                if (anInterface.getExtendedInterface() != null){
                    addRelarioship(anInterface, anInterface.getExtendedInterface(), new Generalisation());
                }
            }
//внутренние сущности
            if (entity.getInnerEntities() != null && entity.getInnerEntities().size() != 0) {
                for (String innerEntity : entity.getInnerEntities()) {
                    addRelarioship(entity, innerEntity, new Inclusition());
                }
            }
        }
    }

    private void addRelarioship(UMLEntity entity, String withEntity, UMLRelationDrawer type) {
        if (entities.containsKey(withEntity)) {
            addArrow(type, entity, entities.get(withEntity));
        } else {
            if (!drawers.containsKey(withEntity)) {
                BlackBox blackBox = new BlackBox(withEntity);
                drawers.put(blackBox.getKey(), blackBox);
            }
            addArrow(type, drawers.get(entity.getPackagePath()), drawers.get(withEntity));
        }
    }

    private void addArrow(UMLRelationDrawer arrow, UMLDrawer fromDrawer, UMLDrawer toDrawer) {
        arrow.setStartDrawerCoordinates(fromDrawer.generateCoords());
        arrow.setEndDrawerCoordinates(toDrawer.generateCoords());
        arrow.setStart(fromDrawer.getAnchorX(), fromDrawer.getAnchorY());
        arrow.setEnd(toDrawer.getAnchorX(), toDrawer.getAnchorY());
        arrow.setStartKey(fromDrawer.getKey());
        arrow.setEndKey(toDrawer.getKey());
        fromDrawer.addObserver(arrow);
        toDrawer.addObserver(arrow);
        arrows.add(arrow);
    }

    private void addArrow(UMLRelationDrawer arrow, UMLEntity from, UMLEntity to) {
        addArrow(arrow, drawers.get(from.getPackagePath()), drawers.get(to.getPackagePath()));
    }

    private boolean getIt(int x, int y, HashMap<String, UMLDrawer> drawers) {
        UMLDrawer drawer;
        for (String key : drawers.keySet()) {
            drawer = drawers.get(key);
            if (isInFrame(x, y, drawer)) {
                draggedDrawer = drawer;
                startDragX = drawer.getX();
                startDragY = drawer.getY();
                return true;
            }
        }
        return false;
    }

    private boolean isInFrame(int x, int y, UMLDrawer drawer) {
        if (drawer.getX() <= x && x <= drawer.getX() + drawer.getFrameWidth()
                && drawer.getY() <= y && y <= drawer.getY() + drawer.getFrameHeight()) {
            return true;
        }

        return false;
    }

    private void clearPane(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
    }
}
