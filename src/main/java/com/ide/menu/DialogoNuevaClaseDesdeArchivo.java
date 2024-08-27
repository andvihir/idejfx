package com.ide.menu;

import com.ide.Ide;
import com.ide.proyectos.TreeDirectorios;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DialogoNuevaClaseDesdeArchivo extends Dialog<Pair<File, File>>{


    private final ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final GridPane grid = new GridPane();
    private Ide ide;
    private TreeDirectorios treeDirectorios;
    private File file;

    public DialogoNuevaClaseDesdeArchivo(Ide ide) {

        this.ide = ide;
        this.treeDirectorios = new TreeDirectorios(this.ide.getTreeDirectorios().getRoot_file(), ide);

        //<Pair<String, String>> dialogo = new Dialog<>();

        this.setTitle("Nueva Clase desde Archivo");

        this.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre: ");
        TextField ubicacionArchivo = new TextField();
        ubicacionArchivo.setEditable(false);
        //ubicacion.setText(System.getProperty("user.home"));

        Button elegirBoton = new Button("Elegir...");

        elegirBoton.setOnAction(e -> {
            this.file = seleccionArchivoJava();
            if(file != null){
                ubicacionArchivo.setText(file.getAbsolutePath());
            }

        });

        grid.add(treeDirectorios, 0,0);
        grid.add(new Label("Archivo:"), 0, 1);
        grid.add(ubicacionArchivo, 1, 1);
        grid.add(elegirBoton, 2, 1);
        Node aceptarBoton = this.getDialogPane().lookupButton(aceptarButtonType);
        aceptarBoton.setDisable(true);

        treeDirectorios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null && newValue.getValue().isDirectory()){
               aceptarBoton.setDisable(false);
           }else{
                aceptarBoton.setDisable(true);
            }
        });

        this.getDialogPane().setContent(grid);

        this.setResultConverter(dialogoBoton -> {
            if (dialogoBoton == aceptarButtonType) {
                //return new Pair<>(nombre.getText(), ubicacion.getText());
                return new Pair<>(file, treeDirectorios.getSelectionModel().getSelectedItem().getValue());
            }
            return null;
        });
    }

    private File seleccionArchivoJava(){
        File res = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Java source", "*.java")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            res=archivo;
        }
        return res;
    }
}
