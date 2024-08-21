package com.ide.controladores;

import com.ide.Ide;
import com.ide.editor.EditorJava;
import com.ide.editor.EditorSimple;
import com.ide.menu.BarraMenu;
import com.ide.menu.DialogoNuevoProyecto;
import com.ide.proyectos.TreeDirectorios;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class ControladorMenu {
    private final Ide ide;
    private final BarraMenu barraMenu;


    public ControladorMenu(Ide ide) {
        this.ide = ide;
        this.barraMenu = ide.getBarraMenu();


        //----- EVENTOS BARRA DE MENU -----
        barraMenu.getMenuItemSalir().setOnAction(e -> System.exit(0));

        barraMenu.getMenuItemNuevaClaseDesdeArchivo().setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().addAll(
                   // new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt", "*.java")
                    new FileChooser.ExtensionFilter("*.java")
            );
            //set initial directory somewhere user will recognise
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            //let user select file
            File archivo = fileChooser.showOpenDialog(null);
            //if file has been chosen, load it using asynchronous method (define later)
            if (archivo != null) {
                try {
                    cargarArchivoAEditor(archivo, getExtensionArchivo(archivo.getName()).get().equals("java"));
                    //System.out.println("Extension:" + getExtensionArchivo(archivo.getName()).get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        barraMenu.getMenuItemNuevoPaquete().setOnAction(e ->{
            Stage nuevaVentana = new Stage();
            Scene nuevaEscena = new Scene(new VBox(), 1024, 768);
            nuevaVentana.setScene(nuevaEscena);
            nuevaVentana.show();
        });

        barraMenu.getMenuItemNuevoProyecto().setOnAction(e -> {
            //TODO nuevo proyecto : crear carpeta proyecto y demas -> llamar metodo nuevoproeycto
            handlerCerrarProyecto(e);
            if(this.ide.hayProyectoAbierto().getValue()) return;
            Boolean creado = crearNuevoProyecto(this.ide);
            if (creado) this.ide.setVistaTotal();

        });

        barraMenu.getMenuItemAbrirProyecto().setOnAction(e -> {

            handlerCerrarProyecto(e);
            if(!ide.hayProyectoAbierto().getValue()){
                abrirProyectoExistente(this.ide);
                this.ide.setVistaTotal();
            }
        });

        barraMenu.getMenuItemGuardar().setOnAction(e -> {
            if (this.ide.getEditor() == null) return;

            if (this.ide.getEditor().getArchivoReferencia() != null) {
                try {
                    guardarArchivo(this.ide.getEditor().getArchivoReferencia());
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(SEVERE, null, ex);
                }
            } else {
                FileChooser fileChooser = new FileChooser();
                //only allow text files to be selected using chooser
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Documentos de texto (*.txt)", "*.txt", "Java (*.java)")
                );
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

                File archivoGuardar = fileChooser.showSaveDialog(null);
                if (archivoGuardar != null) {
                    try {
                        guardarArchivo(archivoGuardar);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        barraMenu.getMenuItemGuardarComo().setOnAction(e -> {
            if (this.ide.getEditor() == null) return;

            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Documentos de texto (*.txt)", "*.txt", "Java (*.java)")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File archivoGuardar = fileChooser.showSaveDialog(null);
            if (archivoGuardar != null) {
                try {

                    //TODO hacer con que cambie el nombre de la pestaña cuando guarda como nuevo archivo y cambiar el editor

                    guardarArchivo(archivoGuardar);
                    this.ide.getPanelPestanya().getPestanyaSeleccionada().setArchivo(archivoGuardar);
                    this.ide.getPanelPestanya().getPestanyaSeleccionada().setText(archivoGuardar.getName());

                } catch (IOException ex) {

                    throw new RuntimeException(ex);

                }
            }
        });

        barraMenu.getMenuItemCerrarProyecto().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handlerCerrarProyecto(event);
            }
        });

        barraMenu.getMenuItemDeshacer().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            this.ide.getEditor().undo();
        });
        barraMenu.getMenuItemCortar().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            this.ide.getEditor().cut();
        });
        barraMenu.getMenuItemCopiar().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            this.ide.getEditor().copy();
        });
        barraMenu.getMenuItemPegar().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            this.ide.getEditor().paste();
        });
        barraMenu.getMenuItemEliminar().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            this.ide.getEditor().replaceSelection("");
        });
        barraMenu.getMenuItemBuscar().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            buscarTextoMenu();
        });
        barraMenu.getMenuItemSeleccionarTodo().setOnAction(e ->{
            if(this.ide.getEditor()==null) return;
            this.ide.getEditor().selectAll();
        });

        //----- ACELERADORES-----
        //barraMenu.getMenuItemNuevaClaseDesdeArchivo().setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        barraMenu.getMenuItemGuardar().setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        barraMenu.getMenuItemSalir().setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        barraMenu.getMenuItemDeshacer().setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
        barraMenu.getMenuItemCortar().setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        barraMenu.getMenuItemCopiar().setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        barraMenu.getMenuItemPegar().setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        barraMenu.getMenuItemSeleccionarTodo().setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        barraMenu.getMenuItemBuscar().setAccelerator(KeyCombination.keyCombination("Ctrl+F"));

    }

    public void cargarArchivoAEditor(File archivo, boolean subrayadoJava) throws IOException {
        if(this.ide.getPanelPestanya().estaArchivoYaAbierto(archivo)) return;

        BufferedReader texto = new BufferedReader(new FileReader(archivo));
        long lineasTotal;
        try (Stream<String> stream = Files.lines(archivo.toPath())) {
            lineasTotal = stream.count();
        }
        String linea;
        StringBuilder archivoTotal = new StringBuilder();
        long lineasCargadas = 0;
        while ((linea = texto.readLine()) != null) {
            archivoTotal.append(linea);
            archivoTotal.append("\n");
            lineasCargadas++;
        }
        /*
        if(subrayadoJava){
            this.ide.cargarEditorJava();
        }else{
            this.ide.cargarEditorSimple();
        }*/
        if (subrayadoJava) {
            EditorJava editorJava = new EditorJava(archivoTotal.toString());
            //editorJava.replaceText(archivoTotal.toString());
            this.ide.getPanelPestanya().abrirPestana(editorJava, archivo.getName(), archivo);
            editorJava.setArchivoReferencia(archivo);
            subscribe(editorJava);
        }else{
            EditorSimple editorSimple = new EditorSimple(archivoTotal.toString());
           // editorSimple.replaceText(archivoTotal.toString());
            this.ide.getPanelPestanya().abrirPestana(editorSimple, archivo.getName(), archivo);
            editorSimple.setArchivoReferencia(archivo);
            subscribe(editorSimple);

        }
        //this.ide.getEditor().replaceText(archivoTotal.toString());
    }

    public void guardarArchivo(File archivoReferencia) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(this.ide.getEditor().getText());
        myWriter.close();

        //this.ide.getEditor().setArchivoReferencia(archivoGuardar);
        this.ide.getEditor().setModificado(false);
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }

    public static Optional<String> getExtensionArchivo(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private void buscarTextoMenu(){
        IntegerProperty indice = new SimpleIntegerProperty(-1);
        TextField texto = new TextField();
        texto.textProperty().addListener(p -> indice.set(-1));
        HBox pane = new HBox(10, new Label("Buscar: "), texto);

        Button siguiente = new Button("Siguiente");
        Button anterior = new Button("Anterior");
        siguiente.setOnAction(ev -> this.ide.getEditor().buscarTexto(texto.getText(), true, indice) );
        anterior.setOnAction(ev -> this.ide.getEditor().buscarTexto(texto.getText(), false, indice) );
        texto.setOnKeyPressed(ev -> {
                if(ev.getCode() == KeyCode.ENTER){
                    siguiente.fire();
                }
        });

        HBox paneBoton = new HBox(10, siguiente, anterior);
        paneBoton.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(20, pane, paneBoton);
        raiz.setPadding(new Insets(20));

        Stage dialogo = new Stage();
        dialogo.initStyle(StageStyle.UTILITY);
        dialogo.setTitle("Buscar");
        dialogo.setScene(new Scene(raiz, 300, 120));
        dialogo.show();
    }

    public void subscribe(EditorSimple editorSimple)
    {
        editorSimple.plainTextChanges().subscribe(ptc ->
        {
            editorSimple.setModificado(true);
        });
    }

    public static boolean crearNuevoProyecto(Ide ide){
        AtomicReference<Boolean> r = new AtomicReference<>(false);
        DialogoNuevoProyecto dialogoNuevoProyecto = new DialogoNuevoProyecto();
        Optional<Pair<String, String>>  result = dialogoNuevoProyecto.showAndWait();
        result.ifPresent( nombreYRuta ->{
            r.set(true);
            System.out.println("nombreProyecto= "+nombreYRuta.getKey() + ", ruta = "+nombreYRuta.getValue());
            String nombreProyecto = nombreYRuta.getKey();
            String ruta = nombreYRuta.getValue();
            String rutaFinal = ruta+"\\"+nombreProyecto;
            File archivo = new File(rutaFinal);
            boolean creado = archivo.mkdirs();
            if(creado) {
                ide.setTreeDirectorios(new TreeDirectorios(archivo, ide));
                ide.getBarraDirectorios().setContent(ide.getTreeDirectorios());
                ide.hayProyectoAbierto().set(true);
            }
        });
        return r.get();
    }

    public static boolean abrirProyectoExistente(Ide ide){
        boolean r = false;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Abrir Carpeta");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File archivo = directoryChooser.showDialog(null);
        if (archivo != null) {
            try {
                // VistaDirectorios vistaDirectorios = new VistaDirectorios(archivo, archivo.getName());
                ide.setTreeDirectorios(new TreeDirectorios(archivo, ide));
                    /*
                    panelVistaDirectorios.getChildren().add(vistaDirectorios);
                    splitPaneH.getItems().addFirst(panelVistaDirectorios);
                    splitPaneV.getItems().addAll(splitPaneH);
                    */

                ide.getBarraDirectorios().setContent(ide.getTreeDirectorios());
                //this.getPanelDirectorios().autosize();
                r = true;
                ide.hayProyectoAbierto().set(true);
                // scrollPane.maxHeight()
                //setLeft(scrollPane);
            } catch (Exception ignored) {
                System.out.println(ignored);
            }
        }
        return r;
    }

    private void handlerCerrarProyecto(ActionEvent event){
        if(ide.hayProyectoAbierto().getValue()) { //HAY PROYECTO ABIERTO
            if (ide.getPanelPestanya().getTabs().isEmpty() || !ide.getPanelPestanya().hayCambio()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.CANCEL);
                alert.setTitle("Cerrar");
                alert.setHeaderText("¿Desea cerrar el proyecto?");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Cerrar");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
                Optional<ButtonType> resultado = alert.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
                    ide.cerrarProyecto();
                } else {
                    event.consume();
                }
            } else if (ide.getPanelPestanya().hayCambio()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.setTitle("Salir");
                alert.setHeaderText("¿Desea guardar los cambios realizados y cerrar el proyecto?");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Guardar");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText("No guardar");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
                Optional<ButtonType> resultado = alert.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
                    try {
                        ide.cerrarYGuardar();
                        ide.cerrarProyecto();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (resultado.isPresent() && resultado.get() == ButtonType.NO) {
                    ide.cerrarProyecto();
                } else {
                    event.consume();
                }
            }
        }else{
            event.consume();
        }

    }

}