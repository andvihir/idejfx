package com.ide.editor;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import org.fxmisc.richtext.CodeArea;

public class MenuContextualEditor extends ContextMenu{

    public MenuContextualEditor(CodeArea editor) {

        ContextMenu menuContextual = new ContextMenu();
        editor.setContextMenu(menuContextual);

        MenuItem cortar = new MenuItem("Cortar");
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem pegar = new MenuItem("Pegar");
        MenuItem eliminar = new MenuItem("Eliminar");
        MenuItem seleccionarTodo = new MenuItem("Seleccionar todo");

        cortar.setOnAction(actionEvent -> editor.cut());
        copiar.setOnAction(actionEvent -> editor.copy());
        pegar.setOnAction(actionEvent -> editor.paste());
        eliminar.setOnAction(actionEvent -> editor.replaceSelection(""));
        seleccionarTodo.setOnAction(actionEvent -> editor.selectAll());

        menuContextual.getItems().addAll(cortar, copiar, pegar, eliminar, seleccionarTodo);

        cortar.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        copiar.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        pegar.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        seleccionarTodo.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));

    }

}
