package com.ide.proyectos;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MenuContextualDirectorios2 extends ContextMenu{
    public MenuContextualDirectorios2() {


        MenuItem cortar = new MenuItem("Cortar");
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem pegar = new MenuItem("Pegar");
        MenuItem eliminar = new MenuItem("Eliminar");

        this.getItems().addAll(cortar, copiar, pegar, eliminar);

        cortar.setOnAction(actionEvent -> cut());
        copiar.setOnAction(actionEvent -> copy());
        pegar.setOnAction(actionEvent -> paste());
        eliminar.setOnAction(actionEvent -> delete());

    }
    private void cut() {

    }
    private void copy() {

    }
    private void paste() {

    }
    private void delete() {

    }



}
