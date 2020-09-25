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
        UMLDiagramPanel panel = UMLToolWindowContentPanel.getDrawingPanel();
        JFileChooser f = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Photo", "png", "png");
        f.setFileFilter(filter);
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);

        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        g.dispose();

        try {
            ImageIO.write(bi, "png", new File(String.valueOf(f.getSelectedFile())+".png"));
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
}
