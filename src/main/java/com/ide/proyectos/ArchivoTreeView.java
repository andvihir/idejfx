package com.ide.proyectos;

import com.ide.Ide;
import com.ide.pestanas.PanelPestanya;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.HashMap;

public class ArchivoTreeView extends TreeView<File> {
    private File archivoRaiz;
    private Ide ide;

    //private HashMap<File>


    public static class ArchivoTreeItem extends TreeItem<File> {

        private PanelPestanya panelPestanya;

        public ArchivoTreeItem() {
            super();
        }
        public ArchivoTreeItem(File archivo, ArchivoTreeView treeView) {
            super(archivo);
            File[] listaArchivos = archivo.listFiles();
            //boolean funcion

            this.getChildren().add(new ArchivoTreeItem());

            this.expandedProperty().addListener((observable, oldValue, newValue) -> {

            });
        }
    }

}
