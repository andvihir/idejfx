package com.ide.proyectos;

import com.ide.Ide;
import com.ide.controladores.ControladorMenu;
import com.ide.pestanas.Pestanya;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class TreeDirectorios extends TreeView<File> {
    private TreeItem<String> parent;
    private File root_file;
    private TreeItem rootItem;
    private Ide ide;


    public TreeDirectorios() {

    }

    public TreeDirectorios(File root_file, Ide ide) {
        TreeItem<File> parent = new TreeItem<>(root_file);
        parent.setExpanded(true);
        //createTree(root_file, parent);
        this.root_file = root_file;
        displayTreeView(root_file.toString());
        this.toString();

        this.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {

            public TreeCell<File> call(TreeView<File> tv) {
                return new TreeCell<File>() {

                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);

                        setText((empty || item == null) ? "" : item.getName());
                    }

                };
            }
        });

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        TreeItem<File> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();
                        if(selectedItem==null) return;
                        File archivo = selectedItem.getValue();
                        Optional<String> extension =  ide.getControladorMenu().getExtensionArchivo(archivo.getName());
                        if(extension.isPresent()) {
                            try {
                                ide.getControladorMenu().cargarArchivoAEditor(archivo, extension.get().equals("java"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });

    }


    public void createTree(File root_file, TreeItem<File> parent) {
        if (root_file.isDirectory()) {
            TreeItem<File> node = new TreeItem<>(root_file);
            parent.getChildren().add(node);
            for (File f : Objects.requireNonNull(root_file.listFiles())) {

                TreeItem<File> placeholder = new TreeItem<>(); // Add TreeItem to make parent expandable even it has no child yet.
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
            parent.getChildren().add(new TreeItem<>(root_file));

        }
    }

    public void displayTreeView(String inputDirectoryLocation) {
        // Creates the root item.
        TreeItem<File> rootItem = new TreeItem<>(Paths.get(inputDirectoryLocation).toFile());

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

        //String[] s = inputDirectoryLocation.split("\\\\");
        //rootItem.setValue(s[s.length - 1]);
        this.setRoot(rootItem);
    }

    public void actualizar() {
        displayTreeView(root_file.toString());
    }

    public File getRootFile() {
        return root_file;
    }

    public static class ControladorTreeDirectorios extends TreeDirectorios {

    }

    public static class MenuContextualDirectorios extends ContextMenu {
        private Ide ide;
    /*
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
        */

        public MenuContextualDirectorios(Ide ide){
            this.ide = ide;


            Menu nuevo = new Menu("Nuevo");
            MenuItem nuevaClase = new MenuItem("Clase...");
            MenuItem nuevoPaquete = new MenuItem("Paquete...");
            MenuItem editarNombre = new MenuItem("Editar Nombre");
            MenuItem cortar = new MenuItem("Cortar");
            MenuItem copiar = new MenuItem("Copiar");
            MenuItem pegar = new MenuItem("Pegar");
            MenuItem eliminar = new MenuItem("Eliminar");
            MenuItem actualizar = new MenuItem("Actualizar");

            nuevo.getItems().addAll(nuevaClase, nuevoPaquete);
            this.getItems().addAll(nuevo, editarNombre, cortar, copiar, pegar, eliminar, actualizar);

            nuevaClase.setOnAction(actionEvent -> {

            });

            editarNombre.setOnAction(actionEvent -> {
                TreeItem<File> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();
                if(selectedItem==null) return;

                TextInputDialog dialogo = new TextInputDialog();

                ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialogo.setTitle("Cambiar Nombre");
                dialogo.setHeaderText("Introducir Nuevo Nombre");
                dialogo.getDialogPane().getButtonTypes().clear();
                dialogo.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

                Node aceptarBoton = dialogo.getDialogPane().lookupButton(aceptarButtonType);
                aceptarBoton.setDisable(true);


                dialogo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    String path = selectedItem.getValue().getAbsolutePath();
                    Path source = Paths.get(path);
                    Path nuevoPathYArchivo;
                    if(selectedItem.getValue().isDirectory()) { //ES CARPETA
                        nuevoPathYArchivo = source.resolveSibling(newValue);
                    }else{ // ES ARCHIVO

                        Optional<String> extension = ControladorMenu.getExtensionArchivo(path);
                        nuevoPathYArchivo = source.resolveSibling(newValue+"."+extension.get());
                    }
                    //Path source = Paths.get(path);

                    aceptarBoton.setDisable(nuevoPathYArchivo.toFile().exists());
                });
                Optional<String>  result = dialogo.showAndWait();
                result.ifPresent( res->{
                    String path = selectedItem.getValue().getAbsolutePath();
                    Path source = Paths.get(path);
                    try {
                        //TODO ACTUALIZAR EL TREEVIEW DESPUES DE CAMBIAR NOMBRE !!!
                        if(selectedItem.getValue().isDirectory()) {
                            Path nuevoTreeItem= Files.move(source, source.resolveSibling(res));
                            ObservableList<TreeItem<File>> hijos = selectedItem.getChildren();


                            selectedItem.setValue(nuevoTreeItem.toFile());

                            for(TreeItem<File> hijo : hijos){
                                ObservableList<Tab> pestanas = ide.getPanelPestanya().getTabs();
                                for(Tab pestana : pestanas){
                                    File archivoPathViejo = ((Pestanya) pestana).getArchivo();
                                    if(hijo.getValue().equals(archivoPathViejo)){

                                        Path n = Files.move(archivoPathViejo.toPath(), source.resolve(res));
                                        ((Pestanya) pestana).setArchivo(n.toFile());
                                        ((Pestanya) pestana).setText(n.toFile().getName());
                                    }
                                }
                                //ide.getPanelPestanya().getTabs().contains(hijo);
                                ide.getTreeDirectorios().actualizar();
                            }
                        }else {
                            Path nuevoTreeItem = Files.move(source, source.resolveSibling(res + "." + ControladorMenu.getExtensionArchivo(path).get()));
                            selectedItem.setValue(nuevoTreeItem.toFile());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            });
            /*
            nuevoArchivo.setOnAction(actionEvent -> nuevoArchivo());
            cortar.setOnAction(actionEvent -> cut());
            copiar.setOnAction(actionEvent -> copy());
            pegar.setOnAction(actionEvent -> paste());
            eliminar.setOnAction(actionEvent -> delete());
            */

            actualizar.setOnAction(actionEvent -> {
                ide.getTreeDirectorios().actualizar();
                if(ide.getTreeDirectorios().getRoot().getChildren().contains("Main2.java")){
                   // ide.getTreeDirectorios().getRoot().getChildren().add(new TreeItem<>("ABC"));
                }else{
                  //  ide.getTreeDirectorios().getRoot().getChildren().add(new TreeItem<>("ZZZ"));
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