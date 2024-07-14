package com.ide.editor;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.CodeArea;

public class MenuContextual extends ContextMenu {
    public MenuContextual() {
        MenuItem cortar = new MenuItem("Cortar");
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem pegar = new MenuItem("Pegar");
        MenuItem eliminar = new MenuItem("Eliminar");
        MenuItem seleccionarTodo = new MenuItem("Seleccionar todo");

        this.getItems().addAll(cortar, copiar, pegar, eliminar, seleccionarTodo);
       // copiar.setOnAction(actionEvent -> paste());

    }
}
