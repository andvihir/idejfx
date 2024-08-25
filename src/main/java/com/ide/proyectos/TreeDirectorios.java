package com.ide.proyectos;

import com.ide.Ide;
import com.ide.controladores.ControladorMenu;
import com.ide.pestanas.Pestanya;
import com.ide.utils.ScrollBarState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;


import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.FileNameMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeDirectorios extends TreeView<File> {
    private TreeItem<String> parent;
    private File root_file;
    private TreeItem<File> rootItem;
    private Ide ide;
    private ScrollBarState scrollBarState;
    private ScrollBar sb;


    public TreeDirectorios() {
        super();
    }
    /*
    public TreeDirectorios(Ide ide) {
        super();
        this.ide = ide;
    }

     */
    public TreeDirectorios(File root_file){

        TreeItem<File> parent = new TreeItem<>(root_file);
        //parent.setExpanded(true);
        //createTree(root_file, parent);
        this.root_file = root_file;
        this.rootItem = displayTreeView(root_file.toString());
    }

    public TreeDirectorios(File root_file, Ide ide) {
        this.ide = ide;
        TreeItem<File> parent = new TreeItem<>(root_file);
        //parent.setExpanded(true);
        //createTree(root_file, parent);
        this.root_file = root_file;
        this.rootItem = displayTreeView(root_file.toString());
        this.scrollBarState = new ScrollBarState(this, Orientation.VERTICAL);


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

        Timeline timelineActualizacion = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                //System.out.println("this is called every 1 seconds");
                                actualizar();

                            }
                        }));
        timelineActualizacion.setCycleCount(Timeline.INDEFINITE);
        this.ide.hayProyectoAbierto().addListener((obs, oldValue, newValue) ->{
            if(newValue) {
                timelineActualizacion.play();
            }else{
                timelineActualizacion.stop();

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
                               // System.out.println(archivo);
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

/*
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
 */
public static void createTree(File file,  TreeItem<File> parent) {
    if (file.isDirectory()) {
        TreeItem<File>  treeItem = new TreeItem<>(file);
        parent.getChildren().add(treeItem);
        for (File f : file.listFiles()) {
            createTree(f, treeItem);
        }
    }else{
        parent.getChildren().add(new TreeItem<File>(file));
    }
}

    public TreeItem<File> displayTreeView(String inputDirectoryLocation) {
        // Creates the root item.
        TreeItem<File> rootItem = new TreeItem<>(Paths.get(inputDirectoryLocation).toFile());

        // Hides the root item of the tree view.
        this.setShowRoot(true);
        //rootItem.setExpanded(true);


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
        /*
        for(TreeItem<?> child:rootItem.getChildren()){
            child.setExpanded(true);
        }
        */
        this.rootItem = rootItem;
        return rootItem;
    }

    public void actualizar() {

        this.scrollBarState.save();
        ScrollBar scrollBar = null;
        for (Node node : this.lookupAll(".scroll-bar")){
            if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation ().equals (Orientation.VERTICAL)) {
                scrollBar = (ScrollBar) node;
            }
        }

        ScrollBar verticalBar = (ScrollBar) this.lookup(".scroll-bar:vertical");
        this.sb = verticalBar;


        TreeItem<File> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();

        //TreeItem<File> raizAntigua = ide.getTreeDirectorios().getRoot();
        TreeItem<File> raizAntigua = ide.getTreeDirectorios().getRoot();

        List<File> listaNodosExpandidos = listaNodosExpandidosRecursion(raizAntigua, new ArrayList<File>());
        //if (raizAntigua.isExpanded()) listaNodosExpandidos.add(raizAntigua.getValue());
        /*
        TreeItem<File> raizAntigua = ide.getTreeDirectorios().getRoot();
        ObservableList<TreeItem<File>> hijosAntiguo = raizAntigua.getChildren();
        List<File> listaNodosExpandidos = new ArrayList<File>();
        TreeItem<File> raizAntigua = ide.getTreeDirectorios().getRoot();
        ObservableList<TreeItem<File>> hijosAntiguo = raizAntigua.getChildren();
        if(raizAntigua.isExpanded()) listaNodosExpandidos.add(raizAntigua.getValue());
        for(TreeItem<File> item:hijosAntiguo){
            if (item.isExpanded()){
                listaNodosExpandidos.add(item.getValue());
            }
        }
         */

        TreeItem<File> root = displayTreeView(root_file.toString());

        treeViewRecursivoMantenerSeleccionYExpanded(root, selectedItem, listaNodosExpandidos);
        if (raizAntigua.isExpanded()) root.setExpanded(true);
        if(selectedItem!=null && selectedItem.equals(raizAntigua)) this.getSelectionModel().select(root);


        //if(selectedItem!=null && selectedItem.equals(raizAntigua)) listaNodosExpandidos.add(selectedItem.getValue());

        //System.out.println(listaNodosExpandidos);
        /*
        if(selectedItem!=null) {
            for(TreeItem<File> child:root.getChildren()){
                if(child.getValue().equals(selectedItem.getValue())){
                    System.out.println("Seleccionado:" + child);
                    ide.getTreeDirectorios().getSelectionModel().select(child);
                }
            }
        }*/
        //ide.getBarraDirectorios().vvalueProperty().setValue(valorScrollAntiguo.getValue());

        this.scrollBarState.restore();

        this.rootItem = root;

        ScrollBar finalScrollBar = scrollBar;
        //System.out.println(scrollBar.getValue());
        //System.out.println(verticalBar.getValue());
        Platform.runLater(() -> {
            ScrollBar verticalBar1 = (ScrollBar) this.lookup(".scroll-bar:vertical");
            assert finalScrollBar != null;
            verticalBar.setValue(this.sb.getValue());
        });

        for (Node node : this.lookupAll(".scroll-bar")){
            if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation ().equals (Orientation.VERTICAL)) {
                assert scrollBar != null;
                node.applyCss();
                ((ScrollBar) node).applyCss();
                ((ScrollBar) node).layout();
                ((ScrollBar) node).setValue(scrollBar.getValue());
                this.ide.getBarraDirectorios().applyCss();
                this.ide.getBarraDirectorios().layout();
                this.ide.getBarraDirectorios().setVvalue(scrollBar.getValue());
            }
        }
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
            //MenuItem actualizar = new MenuItem("Actualizar");

            nuevo.getItems().addAll(nuevaClase, nuevoPaquete);
           // this.getItems().addAll(nuevo, editarNombre, cortar, copiar, pegar, eliminar);
            this.getItems().addAll(nuevo, editarNombre, eliminar);

            nuevo.setDisable(true);

            ide.hayProyectoAbierto().addListener((obs, oldValue, newValue) ->{
                if(newValue){
                    ide.getTreeDirectorios().getSelectionModel().selectedItemProperty().addListener((obs1, oldValue1, newValue1) ->{
                        if(newValue1!=null && newValue1.getValue().isDirectory()){
                            nuevo.setDisable(false);
                        }else if(newValue1!=null && (newValue1.getValue().isFile() || ide.getTreeDirectorios().getSelectionModel().getSelectedItem()==null)){
                            nuevo.setDisable(true);
                        }
                    });
                }
            });

            nuevaClase.setOnAction(actionEvent -> {

                TreeItem<File> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();
                if (selectedItem == null) return;

                TextInputDialog dialogo = new TextInputDialog();

                ButtonType aceptarButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialogo.setTitle("Crear nueva clase");
                dialogo.setHeaderText("Introducir Nombre");
                dialogo.getDialogPane().getButtonTypes().clear();
                dialogo.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

                Node aceptarBoton = dialogo.getDialogPane().lookupButton(aceptarButtonType);
                aceptarBoton.setDisable(true);

                dialogo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

                    Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(newValue);
                    boolean b = m.find();
                    if(b){
                        aceptarBoton.setDisable(true);
                        return;
                    }

                    String path = selectedItem.getValue().getAbsolutePath();
                    File nuevoArchivo= new File(path, newValue+".java");
                    //Path source = Paths.get(path);

                    aceptarBoton.setDisable(nuevoArchivo.exists());
                });

                Optional<String> result = dialogo.showAndWait();
                result.ifPresent(res -> {
                    String path = selectedItem.getValue().getAbsolutePath();
                    File nuevoArchivo = new File(path, res+".java");
                    try {
                        Files.createFile(nuevoArchivo.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
            });

            nuevoPaquete.setOnAction(actionEvent -> {


                TreeItem<File> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();
                if (selectedItem == null) return;

                TextInputDialog dialogo = new TextInputDialog();

                ButtonType aceptarButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialogo.setTitle("Crear nuevo paquete");
                dialogo.setHeaderText("Introducir Nombre");
                dialogo.getDialogPane().getButtonTypes().clear();
                dialogo.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

                Node aceptarBoton = dialogo.getDialogPane().lookupButton(aceptarButtonType);
                aceptarBoton.setDisable(true);

                dialogo.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(newValue);
                    boolean b = m.find();
                    if(b){
                        aceptarBoton.setDisable(true);
                        return;
                    }

                    String path = selectedItem.getValue().getAbsolutePath();
                    File nuevaCarpeta = new File(path, newValue);
                    //Path source = Paths.get(path);
                    //Path nuevoPathYArchivo;

                    // = source.resolveSibling(newValue);

                    //aceptarBoton.setDisable(nuevoPathYArchivo.toFile().exists() && nuevoPathYArchivo.toFile().isDirectory() );
                    aceptarBoton.setDisable(nuevaCarpeta.exists() && !nuevaCarpeta.isFile() );
                });

                Optional<String> result = dialogo.showAndWait();
                result.ifPresent(res -> {
                    String path = selectedItem.getValue().getAbsolutePath();
                    File nuevaCarpeta = new File(path, res);
                    nuevaCarpeta.mkdirs();

                });

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

                    Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(newValue);
                    boolean b = m.find();
                    if(b){
                        aceptarBoton.setDisable(true);
                        return;
                    }

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
                    File archivoPathViejo1 = new File(selectedItem.getValue().getParentFile().getAbsolutePath(), res);
                    File f = new File(selectedItem.getValue(), res);
                    //String path = selectedItem.getValue().getAbsolutePath();
                    Path source = selectedItem.getValue().toPath();
                    try {

                        if(selectedItem.getValue().isDirectory()) {
                            Path nuevoTreeItem= Files.move(source, source.resolveSibling(res));

                            //ObservableList<TreeItem<File>> hijos = selectedItem.getChildren();

                            selectedItem.setValue(nuevoTreeItem.toFile());

                            List<Pestanya> pestanasAbiertas = listaPestanasAbiertasPathAntiguoRecursion(this.ide.getTreeDirectorios().getRoot(), this.ide, source, nuevoTreeItem, new ArrayList<>());

                            for(Pestanya p : pestanasAbiertas){

                                System.out.println(p.getArchivo());
                            }

                            /*
                            for(Pestanya pestana : pestanasAbiertas) {
                                if(pestana.getArchivo().equals(source.toFile())){
                                    pestana.setArchivo(nuevoTreeItem.toFile());
                                    pestana.setText(nuevoTreeItem.toFile().getName());
                                }
                            }
                            */

                            /*
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
                             */
                        }else {
                            Path nuevoTreeItem = Files.move(source, source.resolveSibling(res + "." + ControladorMenu.getExtensionArchivo(source.toString()).get()));
                            selectedItem.setValue(nuevoTreeItem.toFile());
                            ObservableList<Tab> pestanas = this.ide.getPanelPestanya().getTabs();
                            for(Tab pestana : pestanas) {
                                if(((Pestanya)pestana).getArchivo().equals(source.toFile())){
                                    ((Pestanya)pestana).setArchivo(nuevoTreeItem.toFile());
                                    ((Pestanya)pestana).setText(nuevoTreeItem.toFile().getName());
                                }
                            }
                           // this.ide.getPanelPestanya().getPestanyaSeleccionada().setArchivo(nuevoTreeItem.toFile());
                           // this.ide.getPanelPestanya().getPestanyaSeleccionada().setText(nuevoTreeItem.toFile().getName());
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

            //TODO NO SE USA (Borrar)
            /*
            actualizar.setOnAction(actionEvent -> {
                ide.getTreeDirectorios().actualizar();
                if(ide.getTreeDirectorios().getRoot().getChildren().contains("Main2.java")){
                   // ide.getTreeDirectorios().getRoot().getChildren().add(new TreeItem<>("ABC"));
                }else{
                  //  ide.getTreeDirectorios().getRoot().getChildren().add(new TreeItem<>("ZZZ"));
                }
            });
            */
        eliminar.setOnAction(actionEvent -> {

            TreeItem<File> selectedItem = ide.getTreeDirectorios().getSelectionModel().getSelectedItem();
            if (selectedItem == null) return;
            Alert dialogo = new Alert(Alert.AlertType.CONFIRMATION);
            dialogo.setTitle("Eliminar");
            if(selectedItem.getValue().isDirectory()){
                dialogo.setContentText("¿Eliminar este paquete y todos sus archivos?");
            }else {
                dialogo.setContentText("¿Eliminar este archivo?");
            }
            Optional<ButtonType> result = dialogo.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                /*
                if(selectedItem.getValue().isDirectory()){
                    String[]archivos = selectedItem.getValue().list();
                    for(String s: archivos){
                        File archivoActual = new File(selectedItem.getValue().getPath(),s);
                        archivoActual.delete();
                    }
                }
                selectedItem.getValue().delete();*/

                if(selectedItem.getValue().isDirectory()) {
                    try {
                        FileUtils.deleteDirectory(selectedItem.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else if(selectedItem.getValue().isFile()) {
                    selectedItem.getValue().delete();
                }
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

    //TODO VER COMO MANTENER LA RAIZZZ EXPANDED O NO - SE EXPANDE SOLA
    private void treeViewRecursivoMantenerSeleccionYExpanded(TreeItem<File> item, TreeItem<File> selectedItem, List<File> listaNodosExpandidosOg){
        /*
        if(selectedItem!=null && item!=null && !item.isLeaf() &&  item.getValue().equals(selectedItem.getValue())) {
            ide.getTreeDirectorios().getSelectionModel().select(item);
            if(listaNodosExpandidosOg.contains(item.getValue())) {
                item.setExpanded(true);
            }
        }

         */

        if(item != null && !item.isLeaf()){
            for(TreeItem<File> hijo:item.getChildren()){
                if(hijo.getValue()!=null && selectedItem!=null && hijo.getValue().equals(selectedItem.getValue())) {
                    ide.getTreeDirectorios().getSelectionModel().select(hijo);
                }
                if(hijo.getValue()!=null && listaNodosExpandidosOg.contains(hijo.getValue())){
                    hijo.setExpanded(true);
                    //System.out.println(item.getValue());
                }
                treeViewRecursivoMantenerSeleccionYExpanded(hijo, selectedItem, listaNodosExpandidosOg);

            }
        }
    }

    private List<File> listaNodosExpandidosRecursion(TreeItem<File> item, List<File> result){
        //List<File> result = new ArrayList<>();

        ObservableList<TreeItem<File>> hijosAntiguo = item.getChildren();
        //if(item != null && !item.isLeaf() && item.isExpanded()) result.add(item.getValue());
        if(item != null && !item.isLeaf()){
            for(TreeItem<File> hijo:hijosAntiguo) {
                if (hijo.isExpanded() && !result.contains(hijo.getValue())) {
                    result.add(hijo.getValue());
                }
                listaNodosExpandidosRecursion(hijo, result);
            }
        }
        return result;
    }
    private static List<Pestanya> listaPestanasAbiertasPathAntiguoRecursion(TreeItem<File> item, Ide ide, Path source, Path nuevoTreeItem, List<Pestanya> result) throws IOException {

        //ObservableList<TreeItem<File>> hijos = item.getChildren();

        ObservableList<Tab> pestanasAntiguo = ide.getPanelPestanya().getTabs();

        //selectedItem.setValue(nuevoTreeItem.toFile());




        if(item != null && !item.isLeaf()){
                for(TreeItem<File> hijo:item.getChildren()){
                    for(Tab pestanaAntiguo : pestanasAntiguo) {
                    if(hijo.getValue()!=null && hijo.getValue().getAbsolutePath().equals(((Pestanya)pestanaAntiguo).getArchivo().getAbsolutePath())) {

                        //Path nuevoPathArchivo1= Files.move(source, nuevoTreeItem.resolveSibling(pestanaAntiguo.getText()));

/*
                        System.out.println(hijo.getValue().getAbsolutePath());
                        System.out.println(((Pestanya)pestanaAntiguo).getArchivo().getAbsolutePath());

                        System.out.println(source.toAbsolutePath());
                        System.out.println(nuevoTreeItem.toAbsolutePath());

 */


                        //File f = new File(nuevoTreeItem.toAbsolutePath().toString(), hijo.getValue().getName());

                        String carpetasYArchivoHoja = hijo.getValue().getAbsolutePath().replace(source.toAbsolutePath().toString(), "");
                        File f = new File(nuevoTreeItem.toAbsolutePath().toString()+carpetasYArchivoHoja);


                        //System.out.println(f);
                        //((Pestanya)pestanaAntiguo).setArchivo(nuevoTreeItem.toFile());
                        //((Pestanya)pestanaAntiguo).setText(nuevoTreeItem.toFile().getName());
                        ((Pestanya)pestanaAntiguo).setArchivo(f);
                        ((Pestanya)pestanaAntiguo).setText(f.getName());
                        result.add((Pestanya)pestanaAntiguo);


                        /*
                        Path nuevoPathArchivo = Paths.get(nuevoTreeItem.toAbsolutePath()+((Pestanya)pestanaAntiguo).getArchivo().getName());
                        //Path nuevoPathArchivo1= Files.move(source, nuevoTreeItem.resolveSibling(pestanaAntiguo.getText()));
                        ((Pestanya)pestanaAntiguo).setArchivo(nuevoPathArchivo.toFile());
                        ((Pestanya)pestanaAntiguo).setText(nuevoPathArchivo.toFile().getName());
                        result.add((Pestanya)pestanaAntiguo);

                         */
                    }

                }
                    listaPestanasAbiertasPathAntiguoRecursion(hijo, ide, source, nuevoTreeItem, result);
            }
        }
        /*
        ObservableList<Tab> pestanas = this.ide.getPanelPestanya().getTabs();
        for(Tab pestana : pestanas) {
            if(((Pestanya)pestana).getArchivo().equals(source.toFile())){
                ((Pestanya)pestana).setArchivo(nuevoTreeItem.toFile());
                ((Pestanya)pestana).setText(nuevoTreeItem.toFile().getName());
            }
        }

         */
        return result;
    }


    public File getRoot_file() {
        return root_file;
    }

    public TreeItem<File> getRootItem() {
        return rootItem;
    }

    public Ide getIde() {
        return ide;
    }

}