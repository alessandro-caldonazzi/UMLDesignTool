package com.jakutenshi.projects.umlplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jakutenshi.projects.umlplugin.ui.UMLDiagramPanel;
import com.jakutenshi.projects.umlplugin.ui.UMLToolWindowContentPanel;
import com.jakutenshi.projects.umlplugin.util.Options;

/**
 * Created by Alessandro Caldonazzi on 22/09/2020.
 */
public class DisableFieldPath extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Options.disableFieldPath = !Options.disableFieldPath;
    }
}
