package com.ide.proyectos;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.Objects;

public class TreeDirectorios extends TreeView<String> {
    private TreeItem<String> parent;
    private File root_file;
    private TreeItem rootItem;

    public TreeDirectorios() {

    }
    public TreeDirectorios(File root_file, String nombreProyecto){
        TreeItem<String> parent = new TreeItem<>(nombreProyecto);
        parent.setExpanded(true);
        //createTree(root_file, parent);
        displayTreeView(root_file.toString());


    }


    public void createTree(File root_file, TreeItem<String> parent) {
        if (root_file.isDirectory()) {
            TreeItem<String> node = new TreeItem<>(root_file.getName());
            parent.getChildren().add(node);
            for (File f: Objects.requireNonNull(root_file.listFiles())) {

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
        rootItem.setValue(s[s.length-1]);
        this.setRoot(rootItem);
    }

    public void refresh(){
        displayTreeView(root_file.toString());
    }

}
