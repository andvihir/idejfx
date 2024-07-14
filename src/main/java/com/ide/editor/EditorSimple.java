package com.ide.editor;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class EditorSimple extends CodeArea {
    public EditorSimple() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        ContextMenu menuContextual = new ContextMenu();
        this.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
        menuContextual.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");

        this.setContextMenu(menuContextual);

        MenuItem cortar = new MenuItem("Cortar");
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem pegar = new MenuItem("Pegar");
        MenuItem eliminar = new MenuItem("Eliminar");
        MenuItem seleccionarTodo = new MenuItem("Seleccionar todo");

        cortar.setOnAction(actionEvent -> cut());
        copiar.setOnAction(actionEvent -> copy());
        pegar.setOnAction(actionEvent -> paste());

        menuContextual.getItems().addAll(cortar, copiar, pegar, eliminar, seleccionarTodo);


    }
}
