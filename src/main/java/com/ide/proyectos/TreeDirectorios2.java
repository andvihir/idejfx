package com.ide.proyectos;

import com.ide.Ide;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.Objects;

public class TreeDirectorios2 extends TreeView<String> {
    private TreeItem<String> parent;
    private File root_file;
    private TreeItem rootItem;

    public TreeDirectorios2() {

    }
    }
/*
    public TreeDirectorios2(File root_file, String nombreProyecto) {
        TreeItem<String> parent = new TreeItem<>(nombreProyecto);
        parent.setExpanded(true);
        //createTree(root_file, parent);
        this.root_file = root_file;
        displayTreeView(root_file.toString());
        this.toString();


    }


    public void createTree(File root_file, TreeItem<String> parent) {
        if (root_file.isDirectory()) {
            TreeItem<String> node = new TreeItem<>(root_file.getName());
            parent.getChildren().add(node);
            for (File f : Objects.requireNonNull(root_file.listFiles())) {

                TreeItem<String> placeholder = new TreeItem<>(); // Add TreeItem to make parent expandable even it has no child yet.
                node.getChildren().add(placeholder);

                // When parent is expanded continue the recursive
                node.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        createTree(f, node); // Continue the recursive as usual
                        node.getChildren().remove(placeholder); // Remove placeholder
                        node.removeEventHandler(TreeItem.branchExpandedEvent(), this); // Remove event
                    }
                });

            }
        } else {
            parent.getChildren().add(new TreeItem<>(root_file.getName()));

        }
    }

    public void displayTreeView(String inputDirectoryLocation) {
        // Creates the root item.
        TreeItem<String> rootItem = new TreeItem<>(inputDirectoryLocation);

        // Hides the root item of the tree view.
        this.setShowRoot(true);
        rootItem.setExpanded(true);


        // Creates the cell factory.
        //this.setCellFactory(CheckBoxTreeCell.<String>forTreeView());

        // Get a list of files.
        File fileInputDirectoryLocation = new File(inputDirectoryLocation);
        File[] fileList = fileInputDirectoryLocation.listFiles();

        // create tree
        assert fileList != null;
        for (File file : fileList) {
            createTree(file, rootItem);
        }

        String[] s = inputDirectoryLocation.split("\\\\");
        rootItem.setValue(s[s.length - 1]);
        this.setRoot(rootItem);
    }

    public void actualizar() {
        displayTreeView(root_file.toString());
    }

    public File getRootFile() {
        return root_file;
    }

    public static class ControladorTreeDirectorios extends TreeDirectorios2 {

    }

    public static class MenuContextualDirectorios extends ContextMenu {
        private Ide ide;

        public MenuContextualDirectorios() {

            MenuItem nuevoArchivo = new MenuItem("Nuevo Archivo");
            MenuItem cambiarNombre = new MenuItem("Cambiar Nombre");
            MenuItem cortar = new MenuItem("Cortar");
            MenuItem copiar = new MenuItem("Copiar");
            MenuItem pegar = new MenuItem("Pegar");
            MenuItem eliminar = new MenuItem("Eliminar");

            this.getItems().addAll( cortar, copiar, pegar, eliminar);

            cambiarNombre.setOnAction(actionEvent -> {

            });
            nuevoArchivo.setOnAction(actionEvent -> nuevoArchivo());
            cortar.setOnAction(actionEvent -> cut());
            copiar.setOnAction(actionEvent -> copy());
            pegar.setOnAction(actionEvent -> paste());
            eliminar.setOnAction(actionEvent -> delete());

        }
        public MenuContextualDirectorios(Ide ide){
            this.ide = ide;

            MenuItem nuevoArchivo = new MenuItem("Nuevo Archivo");
            MenuItem cambiarNombre = new MenuItem("Cambiar Nombre");
            MenuItem cortar = new MenuItem("Cortar");
            MenuItem copiar = new MenuItem("Copiar");
            MenuItem pegar = new MenuItem("Pegar");
            MenuItem eliminar = new MenuItem("Eliminar");
            MenuItem actualizar = new MenuItem("Actualizar");

            this.getItems().addAll(nuevoArchivo, cambiarNombre, cortar, copiar, pegar, eliminar, actualizar);


            cambiarNombre.setOnAction(actionEvent -> {
                TreeItem<String> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();
                System.out.println(selectedItem);
            });
            nuevoArchivo.setOnAction(actionEvent -> nuevoArchivo());
            cortar.setOnAction(actionEvent -> cut());
            copiar.setOnAction(actionEvent -> copy());
            pegar.setOnAction(actionEvent -> paste());
            eliminar.setOnAction(actionEvent -> delete());
            actualizar.setOnAction(actionEvent -> {
                ide.getTreeDirectorios().actualizar();
                if(ide.getTreeDirectorios().getRoot().getChildren().contains("Main2.java")){
                    ide.getTreeDirectorios().getRoot().getChildren().add(new TreeItem<>("ABC"));
                }else{

                    ide.getTreeDirectorios().getRoot().getChildren().add(new TreeItem<>("ZZZ"));
                }
            });

        }

        private void nuevoArchivo() {
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

}
*/