package com.jakutenshi.projects.umlplugin.drawers;

public class MaxDrawerPoints {
    public static int maxX;
    public static int maxY;

    public static void checkForMax(int x, int y){
        maxX = Math.max(x, maxX);
        maxY = Math.max(y, maxY);
    }
}
