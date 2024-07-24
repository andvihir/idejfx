package trash;

import com.ide.Ide;
import com.ide.menu.BarraMenu;
import com.ide.proyectos.VistaDirectorios;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class ControladoresMenu extends BarraMenu {
    private Ide ide;

    public ControladoresMenu(Ide ide){
        this.ide = ide;
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
                this.ide.setVistaDirectorios(new VistaDirectorios(archivo, archivo.getName()));
                    /*
                    panelVistaDirectorios.getChildren().add(vistaDirectorios);
                    splitPaneH.getItems().addFirst(panelVistaDirectorios);
                    splitPaneV.getItems().addAll(splitPaneH);
                    */

                this.ide.getPanelDirectorios().setContent(this.ide.getVistaDirectorios());
                //this.getPanelDirectorios().autosize();

                // scrollPane.maxHeight()
                //setLeft(scrollPane);
            }catch(Exception ignored){
                System.out.println(ignored);
            }
        }


    });

        getMenuItemGuardar().setOnAction(e -> {
        if (this.ide.getArchivoReferencia() != null) {
            try {
                guardarArchivo(this.ide.getArchivoReferencia());
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
                    this.ide.setArchivoReferencia(archivoGuardar);
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
                this.ide.setArchivoReferencia(archivoGuardar);
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
    this.ide.setArchivoReferencia(archivo);
    this.ide.getEditor().replaceText(archivoTotal.toString());
}

    private void guardarArchivo(File archivoReferencia) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(this.ide.getEditor().getText());
        myWriter.close();
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }
}
