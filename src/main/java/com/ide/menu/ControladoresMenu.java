package com.ide.menu;

import com.ide.editor.EditorSimple;
import com.ide.proyectos.VistaDirectorios;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class ControladoresMenu extends BarraMenu{

    public ControladoresMenu(){
    //----- EVENTOS BARRA DE MENU -----
        getMenuItemSalir().setOnAction(e -> System.exit(0));

        getMenuItemAbrirArchivo().setOnAction(e -> {
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
                cargarAchivoAEditor(archivo);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    });
        getMenuItemAbrirCarpeta().setOnAction(e -> {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Abrir Carpeta");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File archivo = directoryChooser.showDialog(null);
        if (archivo != null) {
            try{
                // VistaDirectorios vistaDirectorios = new VistaDirectorios(archivo, archivo.getName());
                vistaDirectorios = new VistaDirectorios(archivo, archivo.getName());
                    /*
                    panelVistaDirectorios.getChildren().add(vistaDirectorios);
                    splitPaneH.getItems().addFirst(panelVistaDirectorios);
                    splitPaneV.getItems().addAll(splitPaneH);
                    */

                scrollPaneDirectorios.setContent(vistaDirectorios);
                scrollPaneDirectorios.autosize();

                // scrollPane.maxHeight()
                //setLeft(scrollPane);
            }catch(Exception ignored){
                System.out.println(ignored);
            }
        }


    });

        getMenuItemGuardar().setOnAction(e -> {
        if (archivoReferencia != null) {
            try {
                guardarArchivo(archivoReferencia);
            } catch(IOException ex){
                Logger.getLogger(getClass().getName()).log(SEVERE, null, ex);
            }
        } else{
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
                    archivoReferencia = archivoGuardar;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    });

        getMenuItemGuardarComo().setOnAction(e -> {

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
                archivoReferencia = archivoGuardar;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    });

    //----- ACELERADORES-----
        getMenuItemAbrirArchivo().setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        getMenuItemGuardar().setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        getMenuItemSalir().setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

}
private File cargarAchivoAEditor(File archivo, EditorSimple editor) throws IOException {
    BufferedReader texto = new BufferedReader(new FileReader(archivo));
    long lineasTotal;
    try (Stream<String> stream = Files.lines(archivo.toPath())) {
        lineasTotal = stream.count();
    }
    String linea;
    StringBuilder archivoTotal = new StringBuilder();
    long lineasCargadas = 0;
    while( (linea = texto.readLine()) != null) {
        archivoTotal.append(linea);
        archivoTotal.append("\n");
        lineasCargadas++;
    }
    editor.replaceText(archivoTotal.toString());
    return archivo;
}
}
