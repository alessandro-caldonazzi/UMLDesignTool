package com.jakutenshi.projects.umlplugin.ui;

import com.intellij.ide.util.ChooseElementsDialog;
import com.intellij.openapi.project.Project;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.ArrayList;

public class UMLEntityChooser extends ChooseElementsDialog<UMLEntity> {

    public UMLEntityChooser (Project project, ArrayList<UMLEntity> entities) {
        super(project, entities, "Select Elements", "Select the elements of which you want to create an uml schema", true);
        selectElements(entities);
    }
    @Override
    protected String getItemText(UMLEntity s) {
        return s.toString();
    }

    @Nullable
    @Override
    protected Icon getItemIcon(UMLEntity s) {
        return null;
    }
}
