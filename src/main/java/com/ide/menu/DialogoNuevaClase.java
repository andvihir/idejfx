package com.ide.menu;

import com.ide.Ide;
import com.ide.proyectos.TreeDirectorios;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DialogoNuevaClase extends Dialog<Pair<String, File>> {


    private final ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final GridPane grid = new GridPane();
    private Ide ide;
    private TreeDirectorios treeDirectorios;

    private File filePathDestino;
    private String nombreArchivo;

    public DialogoNuevaClase(Ide ide) {

        this.ide = ide;
        this.treeDirectorios = new TreeDirectorios(this.ide.getTreeDirectorios().getRoot_file(), ide);

        //<Pair<String, String>> dialogo = new Dialog<>();

        this.setTitle("Nueva Clase");

        this.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre: ");
        TextField nombreArchivo = new TextField();
        //ubicacion.setText(System.getProperty("user.home"));

        Label archivoFinalPathLabel =  new Label("");
        Label archivoYaExiste =  new Label("Nombre ya existe.");
        archivoYaExiste.setVisible(false);
        archivoYaExiste.setStyle("-fx-text-fill: red;");


        grid.add(treeDirectorios, 0,0);
        grid.add(new Label("Archivo:"), 0, 1);
        grid.add(nombreArchivo, 1, 1);
        grid.add(archivoFinalPathLabel, 0, 2);
        grid.add(archivoYaExiste, 0, 3);
        Node aceptarBoton = this.getDialogPane().lookupButton(aceptarButtonType);
        aceptarBoton.setDisable(true);

        nombreArchivo.textProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue.trim().isEmpty()) {
                aceptarBoton.setDisable(true);
                this.nombreArchivo = null;
            }else{
                this.nombreArchivo = newValue+".java";
            }
            if(!newValue.trim().isEmpty() && treeDirectorios.getSelectionModel().getSelectedItem() != null && treeDirectorios.getSelectionModel().getSelectedItem().getValue().isDirectory()) {

                File f = new File(this.filePathDestino.getAbsolutePath(), this.nombreArchivo);
                if(f.exists()) {
                    archivoYaExiste.setVisible(true);
                }else {
                    archivoFinalPathLabel.setText(f.getAbsolutePath());
                    archivoYaExiste.setVisible(false);
                    aceptarBoton.setDisable(false);
                }
            }
        });

        treeDirectorios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null && newValue.getValue().isDirectory()){
                this.filePathDestino = newValue.getValue();
                if(this.nombreArchivo!=null){

                    File f = new File(this.filePathDestino.getAbsolutePath(), this.nombreArchivo);
                    if(f.exists()) {
                        archivoYaExiste.setVisible(true);
                    }else {
                        archivoFinalPathLabel.setText(f.getAbsolutePath());
                        archivoYaExiste.setVisible(false);
                        aceptarBoton.setDisable(false);
                    }
                }
            }else{
                this.filePathDestino = null;
                aceptarBoton.setDisable(true);
            }
        });

        this.getDialogPane().setContent(grid);

        this.setResultConverter(dialogoBoton -> {
            if (dialogoBoton == aceptarButtonType) {
                //return new Pair<>(nombre.getText(), ubicacion.getText());
                return new Pair<>(this.nombreArchivo, treeDirectorios.getSelectionModel().getSelectedItem().getValue());
            }
            return null;
        });
    }
}
