package com.jakutenshi.projects.umlplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.jakutenshi.projects.umlplugin.util.Options;
import org.jetbrains.annotations.NotNull;

public class Erase extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Options.toolInUse = (!Options.toolInUse.equals("eraser"))? "eraser":"default";
    }
}
