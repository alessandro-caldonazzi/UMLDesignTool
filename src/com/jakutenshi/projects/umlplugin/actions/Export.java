package com.jakutenshi.projects.umlplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jakutenshi.projects.umlplugin.ui.UMLDiagramPanel;
import com.jakutenshi.projects.umlplugin.ui.UMLToolWindowContentPanel;
import com.jakutenshi.projects.umlplugin.util.Options;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Alessandro Caldonazzi on 22/09/2020.
 */
public class Export extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        UMLDiagramPanel tp = UMLToolWindowContentPanel.getDrawingPanel();
        JFileChooser f = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Photo", "png", "png");
        f.setFileFilter(filter);
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);

        int width = tp.getWidth() * 2 + 40;
        int height = tp.getHeight() * 2 + 40;

        tp.setSize(width, height);
        tp.doLayout();
        tp.zoomIn();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        tp.print(g2d);
        g2d.dispose();

        tp.setSize((width-40)/2, (height-40)/2);
        tp.doLayout();
        tp.zoomOut();
        try {
            ImageIO.write(img, "png", new File(String.valueOf(f.getSelectedFile())+".png"));
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}
