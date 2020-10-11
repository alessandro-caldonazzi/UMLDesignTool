package com.jakutenshi.projects.umlplugin.drawers;

import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.container.entities.attributes.Keyword;
import com.jakutenshi.projects.umlplugin.drawers.relationships.UMLRelationDrawer;
import com.jakutenshi.projects.umlplugin.util.Observable;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by JAkutenshi on 29.05.2016.
 */
public abstract class UMLDrawer implements Observable <UMLRelationDrawer> {
    protected class DrawnLine {
        private Font font = new Font(DEFAULT_LINE_FONT,
                DEFAULT_LINE_FONT_STYLE,
                DEFAULT_LINE_FONT_SIZE);
        String line;

        public DrawnLine(Font font, String line) {
            this.font = font;
            this.line = line;
        }

        public DrawnLine(String line) {
            this.line = line;
        }

        public DrawnLine() {
        }

        public Font getFont() {
            return font;
        }

        public void setFont(Font font) {
            this.font = font;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }
    }

    private int x = 0;
    private int y = 0;
    private int frameWidth = 0;
    private int frameHeight = 0;
    private int offset = 0;
    private int anchorX = 0;
    private int anchorY = 0;
    protected TypeParameterDrawer generics;


    public final static int LINE_SPACING = 10;
    public final static int FRAME_MARGIN = 10;
    public final static int SEPARATOR_HEIGHT = LINE_SPACING * 2 + 1;
    public final static String DEFAULT_LINE_FONT = "Monospaced";
    public final static int DEFAULT_LINE_FONT_STYLE = Font.PLAIN;
    public final static int DEFAULT_LINE_FONT_SIZE = 14;
    public final static int SYMBOL_WIDTH = 9;
    public final static int SYMBOL_HEIGHT = 10;
    protected final static Font ITALIC_FONT = new Font(DEFAULT_LINE_FONT,
                                                          Font.ITALIC,
                                                          DEFAULT_LINE_FONT_SIZE);
    public final static int DRAWN_LINE_HEIGHT = SYMBOL_HEIGHT + LINE_SPACING;

    private String key;
    private DrawnLine drawnTitle;
    private ArrayList<UMLRelationDrawer> observableArrows = new ArrayList<>();

    public abstract void draw(Graphics2D g);
    protected abstract void fillContent(UMLEntity entity);

    public UMLDrawer() {
    }

    public UMLDrawer(UMLEntity entity) {
        key = entity.getPackagePath();
//заголовок
        drawnTitle = new DrawnLine(entity.titleToUML());
        if (entity.getKeywords().contains(Keyword.ABSTRACT)) {
            drawnTitle.setFont(ITALIC_FONT);
        }
        fillContent(entity);
    }

    protected int drawSection(int y, LinkedList<DrawnLine> lines, Graphics2D g) {
        if (lines.size() == 0) {
            y += SYMBOL_HEIGHT;
        }
        for (DrawnLine line : lines) {
            y = drawLine(y, line, g);
        }
        return y - SYMBOL_HEIGHT;
    }

    protected int drawTitle(int y, Graphics2D g) {
        drawLine(y, getDrawnTitle(), g);
        return y + LINE_SPACING;
    }

    protected int drawLine(int y, DrawnLine line, Graphics2D g) {
        g.setFont(line.getFont());
        g.drawString(line.getLine(),
                getOffset(),
                y);
        return y + DRAWN_LINE_HEIGHT;
    }

    protected void drawFrame(Graphics2D g) {
        MaxDrawerPoints.checkForMax(getX() + getFrameWidth(), getY() + getFrameHeight());
        // draw a rectangle
        g.setColor(Color.WHITE);
        g.fillRect(getX(), getY(), getFrameWidth(), getFrameHeight());
        // frame around
        g.setColor(Color.BLACK);
        System.out.println("draw: " + (getY() + getFrameHeight()));
        g.drawRect(getX(), getY(), getFrameWidth(), getFrameHeight());
    }

    protected int drawSeparator(int y, Graphics2D g) {
        g.drawLine(x, y + LINE_SPACING,
                x + frameWidth, y + LINE_SPACING);
        return y + SEPARATOR_HEIGHT + SYMBOL_HEIGHT;
    }

    protected void makeFontUnderlined(DrawnLine line) {
        Map fontAttributes = line.getFont().getAttributes();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        line.setFont(line.getFont().deriveFont(fontAttributes));
    }

    protected DrawnLine getDrawnTitle() {
        return drawnTitle;
    }

    protected void setDrawnTitle(DrawnLine drawnTitle) {
        this.drawnTitle = drawnTitle;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        anchorX = x + frameWidth / 2;
        offset = x + FRAME_MARGIN;
        if (generics != null) {
            generics.setX(getX() + getFrameWidth() - FRAME_MARGIN * 3);
        }
        notifyObservers();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        anchorY = y + frameHeight / 2;
        if (generics != null ) {
            generics.setY(getY() - generics.getFrameHeight() + FRAME_MARGIN);
        }
        notifyObservers();
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return drawnTitle.getLine();
    }

    public int getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(int anchorX) {
        this.anchorX = anchorX;
        setX(anchorX - frameWidth / 2);
        notifyObservers();
    }

    public int getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(int anchorY) {
        this.anchorY = anchorY;
        setY(anchorY - frameHeight / 2);
        notifyObservers();
    }

    @Override
    public void notifyObservers() {

        for (UMLRelationDrawer observableArrow : observableArrows) {
            observableArrow.onChange(key, anchorX, anchorY, generateCoords());
        }
    }

    @Override
    public void addObserver(UMLRelationDrawer observer) {
        if (!observableArrows.contains(observer)) {
            observableArrows.add(observer);
        }
    }

    @Override
    public void removeObserver(UMLRelationDrawer observer) {
        if (observableArrows.contains(observer)) {
            observableArrows.remove(observer);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int[][] generateCoords() {
        int[][] coords =
                {{x, x + frameWidth, x + frameWidth, x},
                        {y, y, y + frameHeight, y + frameHeight}};
        return coords;
    }
}
