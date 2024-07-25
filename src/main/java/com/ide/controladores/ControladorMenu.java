package com.ide.controladores;

import com.ide.Ide;
import com.ide.editor.EditorJava;
import com.ide.editor.EditorSimple;
import com.ide.menu.BarraMenu;
import com.ide.proyectos.MenuContextualDirectorios;
import com.ide.proyectos.TreeDirectorios;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.util.Optional;
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

        barraMenu.getMenuItemAbrirArchivo().setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt", "*.java")
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
        barraMenu.getMenuItemAbrirCarpeta().setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Abrir Carpeta");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File archivo = directoryChooser.showDialog(null);
            if (archivo != null) {
                try {
                    // VistaDirectorios vistaDirectorios = new VistaDirectorios(archivo, archivo.getName());
                    this.ide.setTreeDirectorios(new TreeDirectorios(archivo, archivo.getName()));
                    /*
                    panelVistaDirectorios.getChildren().add(vistaDirectorios);
                    splitPaneH.getItems().addFirst(panelVistaDirectorios);
                    splitPaneV.getItems().addAll(splitPaneH);
                    */

                    this.ide.getBarraDirectorios().setContent(this.ide.getTreeDirectorios());
                    this.ide.getBarraDirectorios().setContextMenu(new MenuContextualDirectorios());
                    //this.getPanelDirectorios().autosize();

                    // scrollPane.maxHeight()
                    //setLeft(scrollPane);
                } catch (Exception ignored) {
                    System.out.println(ignored);
                }
            }


        });

        barraMenu.getMenuItemGuardar().setOnAction(e -> {
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
                        this.ide.getEditor().setArchivoReferencia(archivoGuardar);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        barraMenu.getMenuItemGuardarComo().setOnAction(e -> {

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
                    this.ide.getEditor().setArchivoReferencia(archivoGuardar);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //----- ACELERADORES-----
        barraMenu.getMenuItemAbrirArchivo().setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        barraMenu.getMenuItemGuardar().setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        barraMenu.getMenuItemSalir().setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

    }

    private void cargarArchivoAEditor(File archivo, boolean subrayadoJava) throws IOException {
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
            EditorJava editorJava = new EditorJava();
            editorJava.replaceText(archivoTotal.toString());
            this.ide.getPanelPestanya().abrirPestana(editorJava, archivo.getName());
            editorJava.setArchivoReferencia(archivo);
        }else{
            EditorSimple editorSimple = new EditorSimple();
            editorSimple.replaceText(archivoTotal.toString());
            this.ide.getPanelPestanya().abrirPestana(editorSimple, archivo.getName());
            editorSimple.setArchivoReferencia(archivo);

        }

        //this.ide.getEditor().replaceText(archivoTotal.toString());
    }

    private void guardarArchivo(File archivoReferencia) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(this.ide.getEditor().getText());
        myWriter.close();
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }

    private Optional<String> getExtensionArchivo(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }


}