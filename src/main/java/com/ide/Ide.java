package com.ide;


import com.ide.editor.EditorSimple;
import com.ide.editor.Java;
import com.ide.pestanas.PanelPestanya;
import com.ide.proyectos.VistaDirectorios;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class Ide extends AnchorPane{

    //private final BarraMenu barraMenu = new BarraMenu();
    // private final BorderPane borderPane = new BorderPane();

    private final MenuBar barraMenu = new MenuBar();
    private final EditorSimple editorSimple = new EditorSimple();
    private File archivoReferencia;
    private final Java javaEditor = new Java();
    private final VBox vBox = new VBox();
    private final TabPane tabPane = new TabPane();
    private final SplitPane splitPaneH = new SplitPane();
    private final SplitPane splitPaneV = new SplitPane();
    private final StackPane panelVistaDirectorios = new StackPane();
    private final ScrollPane scrollPaneDirectorios = new ScrollPane();
    private final ScrollPane scrollPaneMenuRodapie = new ScrollPane();
    private final PanelPestanya panelPestanya = new PanelPestanya();

    private VistaDirectorios vistaDirectorios;


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

        MenuItem menuItemAbrirArchivo = new MenuItem("Abrir archivo");
        MenuItem menuItemAbrirCarpeta = new MenuItem("Abrir carpeta");
        MenuItem menuItemGuardar = new MenuItem("Guardar");
        MenuItem menuItemGuardarComo = new MenuItem("Guardar como...");
        MenuItem menuItemSalir = new MenuItem("Salir");
        menuArchivo.getItems().addAll(menuItemAbrirArchivo, menuItemAbrirCarpeta, menuItemGuardar, menuItemGuardarComo, menuItemSalir);

        //----- EVENTOS BARRA DE MENU -----
        menuItemSalir.setOnAction(e -> System.exit(0));

        menuItemAbrirArchivo.setOnAction(e -> {
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
        menuItemAbrirCarpeta.setOnAction(e -> {
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

        menuItemGuardar.setOnAction(e -> {
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

        menuItemGuardarComo.setOnAction(e -> {

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
        menuItemAbrirArchivo.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        menuItemGuardar.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        menuItemSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        //----- ESPACIO DE LAS PESTAÑAS -----


        //----- BARRA DE NAVEGACIÓN/PROYECTOS -----

        //


        // setTop(barraMenu);
        vBox.setPrefSize(600,600);
        VirtualizedScrollPane<Java> vsp = new VirtualizedScrollPane<>(javaEditor);
        vsp.setPrefSize(800, 800);
        vBox.getChildren().addAll(tabPane, vsp);
       // scrollPaneDirectorios.setPrefSize(120,120);
       // setLeft(scrollPaneDirectorios);
        splitPaneH.getItems().addAll(scrollPaneDirectorios, vsp);

        scrollPaneDirectorios.autosize();

        splitPaneV.setOrientation(Orientation.VERTICAL);
        splitPaneV.getItems().addAll(splitPaneH, scrollPaneMenuRodapie);
        this.getChildren().addAll(barraMenu,splitPaneV);
        //setTop(barraMenu);
        //setCenter(splitPaneV);
       //setCenter(new VirtualizedScrollPane<>(javaEditor));
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
        javaEditor.replaceText(archivoTotal.toString());
    }

    private void guardarArchivo(File archivoReferencia) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(javaEditor.getText());
        myWriter.close();
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }
    /*
    private MenuBar crearBarraMenu(){

    }*/
    public void stopI() {
        javaEditor.stopJ();
    }
}
