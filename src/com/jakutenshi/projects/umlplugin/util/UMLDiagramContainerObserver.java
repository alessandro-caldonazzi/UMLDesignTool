package com.jakutenshi.projects.umlplugin.util;

import com.jakutenshi.projects.umlplugin.container.entities.UMLEntity;

import java.util.HashMap;

/**x
 * Created by JAkutenshi on 30.05.2016.
 */
public interface UMLDiagramContainerObserver {

    void onChange(HashMap<String, UMLEntity> entities);

}
