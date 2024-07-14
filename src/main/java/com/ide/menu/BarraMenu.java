package com.ide.menu;

import javafx.concurrent.Task;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class BarraMenu extends MenuBar {
    private final Menu menuArchivo = new Menu("Archivo");
    private final Menu menuEdicion = new Menu("Edición");
    private final Menu menuVer = new Menu("Ver");
    private final Menu menuAyuda = new Menu("Ayuda");

    private final MenuItem menuItemAbrir = new MenuItem("Abrir");
    private final MenuItem menuItemGuardar = new MenuItem("Guardar");
    private final MenuItem menuItemSalir = new MenuItem("Salir");

    public BarraMenu(){
        /*
        Menu menuArchivo = new Menu("Archivo");
        Menu menuEdicion = new Menu("Edición");
        Menu menuVer = new Menu("Ver");
        Menu menuAyuda = new Menu("Ayuda");
        */
        /*
        MenuItem menuItemAbrir = new MenuItem("Abrir");
        MenuItem menuItemGuardar = new MenuItem("Guardar");
        MenuItem menuItemSalir = new MenuItem("Salir");
        */
        menuArchivo.getItems().addAll(menuItemAbrir, menuItemGuardar, menuItemSalir);

        menuItemSalir.setOnAction(e -> System.exit(0));

        menuItemAbrir.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")
            );
            //set initial directory somewhere user will recognise
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            //let user select file
            File archivo = fileChooser.showOpenDialog(null);
            //if file has been chosen, load it using asynchronous method (define later)
            if (archivo != null) {
                cargarAchivoAEditor(archivo);
            }
        });
        getMenus().addAll(menuArchivo, menuEdicion, menuVer, menuAyuda);
    }

    private void cargarAchivoAEditor(File archivo) {
        if(archivo !=null && archivo.exists()){
            if(archivo.isDirectory()){

            }
        }
    }

    public void salir(){
    }
}
