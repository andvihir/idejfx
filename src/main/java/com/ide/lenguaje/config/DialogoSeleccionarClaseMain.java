package com.ide.lenguaje.config;

import com.ide.Ide;
import com.ide.proyectos.TreeDirectorios;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class DialogoSeleccionarClaseMain extends Dialog<File> {


    private final ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final GridPane grid = new GridPane();
    private Ide ide;
    private TreeDirectorios treeDirectorios;

    public DialogoSeleccionarClaseMain(Ide ide) {

        this.ide = ide;
        this.treeDirectorios = new TreeDirectorios(this.ide.getTreeDirectorios().getRoot_file(), ide);

        //<Pair<String, String>> dialogo = new Dialog<>();

        this.setTitle("Seleccionar Clase Para Ejecución");
        this.setContentText("Seleccionar Clase Para Ejecución");

        this.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

        TextField nombreArchivo = new TextField();
        //ubicacion.setText(System.getProperty("user.home"));

        Node aceptarBoton = this.getDialogPane().lookupButton(aceptarButtonType);
        aceptarBoton.setDisable(true);


        treeDirectorios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null && newValue.getValue().isDirectory()){
                aceptarBoton.setDisable(true);
            }else if(newValue!=null && newValue.getValue().isFile() && FilenameUtils.getExtension(newValue.getValue().getAbsolutePath()).equals("java")){
                aceptarBoton.setDisable(false);
            }
        });

        this.getDialogPane().setContent(treeDirectorios);

        this.setResultConverter(dialogoBoton -> {
            if (dialogoBoton == aceptarButtonType) {
                return treeDirectorios.getSelectionModel().getSelectedItem().getValue();
            }
            return null;
        });
    }
}
