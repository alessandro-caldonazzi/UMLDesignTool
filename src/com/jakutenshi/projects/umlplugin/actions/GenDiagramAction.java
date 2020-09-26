package com.jakutenshi.projects.umlplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.jakutenshi.projects.umlplugin.container.DiagramContainer;
import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;
import com.jakutenshi.projects.umlplugin.parsers.ClassParser;
import com.jakutenshi.projects.umlplugin.parsers.EnumParser;
import com.jakutenshi.projects.umlplugin.parsers.InterfaceParser;
import com.sun.jna.platform.unix.X11;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JAkutenshi on 25.05.2016.
 */
public class GenDiagramAction extends AnAction {
    private static Project project;
    private PsiManager psiManager;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        if (project != null) {
            psiManager = PsiManager.getInstance(project);
            //Date d = new Date();
            //long start = d.getTime();
            parseProject();
            //System.out.println(d.getTime() - start);
        }
    }

    private void parseProject() {
        DiagramContainer container = DiagramContainer.getInstance();
        container.clearContainer();
        ArrayList<UMLEntity> umlEntities = new ArrayList<>();
        parseForJavaFiles(project.getBaseDir().getChildren(), umlEntities);
        for (UMLEntity entity : umlEntities) {
            container.addUMLEntity(entity);
        }
        DiagramContainer.getInstance().notifyObservers();
    }

    private void parseForJavaFiles(VirtualFile[] virtualFiles, ArrayList<UMLEntity> umlEntities) {
        if (virtualFiles == null) return;
        PsiFile psiFile;

        for (VirtualFile virtualFile : virtualFiles) {
            psiFile = psiManager.findFile(virtualFile);
            //Find all Java files
            if (isJavaFile(virtualFile)) {
                assert psiFile != null;
                parseJavaFile(psiFile.getChildren(), umlEntities);
            }
            else {
                parseForJavaFiles(virtualFile.getChildren(), umlEntities);
            }
        }

    }

    private void parseJavaFile(PsiElement[] elements, ArrayList<UMLEntity> umlEntities) {
        if (elements == null || elements.length == 0) {
            return;
        }
        UMLEntity entity;
        EnumParser enumParser = new EnumParser();
        InterfaceParser interfaceParser = new InterfaceParser();
        ClassParser classParser = new ClassParser();

        for (PsiElement element : elements) {
            if ((element instanceof PsiClass)
                    && !(element instanceof PsiAnonymousClass)
                    && !(element instanceof PsiTypeParameter)) {
                PsiClass psiClass = (PsiClass) element;
                // parse the entity
                if(psiClass.isInterface()){
                    entity = interfaceParser.parse(psiClass);
                } else if (psiClass.isEnum()) {
                    entity = enumParser.parse(psiClass);
                } else {
                    entity = classParser.parse(psiClass);
                }
                // looking for internal entities
                PsiClass[] psiInnerEntities = psiClass.getInnerClasses();
                for (PsiClass innerEntity : psiInnerEntities) {
                    entity.addInnerEntities(innerEntity.getQualifiedName());
                }
                // add to container
                umlEntities.add(entity);
            }
            parseJavaFile(element.getChildren(), umlEntities);
        }
    }

    private boolean isJavaFile(VirtualFile file) {
        return psiManager.findFile(file) instanceof PsiJavaFileImpl;
    }

    public static Project getProject() {
        return project;
    }
}
