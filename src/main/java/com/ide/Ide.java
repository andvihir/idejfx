package com.ide;


import com.ide.editor.EditorSimple;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class Ide extends BorderPane{

    //private final BarraMenu barraMenu = new BarraMenu();
    // private final BorderPane borderPane = new BorderPane();

    private final MenuBar barraMenu = new MenuBar();
    private final EditorSimple editorSimple = new EditorSimple();
    private File archivoReferencia;


    public Ide() {
        //----- BARRA DE MENU -----
        /*
        MenuBar barraMenu = new MenuBar();

        */

        Menu menuArchivo = new Menu("Archivo");
        Menu menuEdicion = new Menu("Edición");
        Menu menuVer = new Menu("Ver");
        Menu menuAyuda = new Menu("Ayuda");

        barraMenu.getMenus().addAll(menuArchivo, menuEdicion, menuVer, menuAyuda);

        MenuItem menuItemAbrir = new MenuItem("Abrir");
        MenuItem menuItemGuardar = new MenuItem("Guardar");
        MenuItem menuItemSalir = new MenuItem("Salir");
        menuArchivo.getItems().addAll(menuItemAbrir, menuItemGuardar, menuItemSalir);

        //----- EVENTOS BARRA DE MENU -----
        menuItemSalir.setOnAction(e -> System.exit(0));

        menuItemAbrir.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().add(
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

        menuItemGuardar.setOnAction(e -> {
            if (archivoReferencia != null) {
                try {
                    guardarArchivo(archivoReferencia);
                } catch(IOException ex){
                Logger.getLogger(getClass().getName()).log(SEVERE, null, ex);
            }
        }
        });


        //----- BARRA DE NAVEGACIÓN/PROYECTOS -----

        //


        // setTop(barraMenu);
        setTop(barraMenu);
        setCenter(new VirtualizedScrollPane<>(editorSimple));
    }

    private void cargarAchivoAEditor(File archivo) throws IOException {
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
        archivoReferencia = archivo;
        editorSimple.replaceText(archivoTotal.toString());
    }

    private void guardarArchivo(File archivoReferencia) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(editorSimple.getText());
        myWriter.close();
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }
    /*
    private MenuBar crearBarraMenu(){

    }*/
    private void a1(){

    }
    //i
}
