package com.ide.lenguaje.config;

import com.ide.Ide;
import com.ide.lenguaje.JavaMenu;
import com.ide.proyectos.TreeDirectorios;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class EjecucionSettings extends Dialog<Pair<String, String[]>> {
    //Devuelve archivo q va a ser Main, y los args para ejecucion


    private final ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final GridPane grid = new GridPane();
    private Ide ide;

    private String paqueteYClaseParaEjecucion;

    public EjecucionSettings(Ide ide) {

        this.ide = ide;
        JavaMenu javaMenu = ide.getJavaMenu();
        this.paqueteYClaseParaEjecucion = javaMenu.getClaseMain();
        if(paqueteYClaseParaEjecucion==null){
            this.paqueteYClaseParaEjecucion="";
        }
        String[] args = javaMenu.getArgs();
        String strArgs = String.join(" ", args);


        //<Pair<String, String>> dialogo = new Dialog<>();

        this.setTitle("Configuración de Ejecución");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        Label labelClaseParaEjecucion = new Label("Clase Para Ejecución: ");
        TextField textFieldClaseParaEjecucion = new TextField(paqueteYClaseParaEjecucion);
        textFieldClaseParaEjecucion.setEditable(false);
        Button botonCambiarClase = new Button("Elegir otra clase...");

        Label labelArgumentos = new Label("Argumentos: ");
        TextField textFieldArgumentos = new TextField(strArgs);
        //ubicacion.setText(System.getProperty("user.home"));


        grid.add(labelClaseParaEjecucion, 0, 0);
        grid.add(textFieldClaseParaEjecucion, 1, 0);
        grid.add(botonCambiarClase, 2, 0);
        grid.add(labelArgumentos, 0, 1);
        grid.add(textFieldArgumentos, 1, 1);

        this.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

        Node aceptarBoton = this.getDialogPane().lookupButton(aceptarButtonType);

        botonCambiarClase.setOnAction(event ->{
            DialogoSeleccionarClaseMain dialogoSeleccionarClaseMain = new DialogoSeleccionarClaseMain(ide);
            Optional<File> result = dialogoSeleccionarClaseMain.showAndWait();
            result.ifPresent(file -> {
                this.paqueteYClaseParaEjecucion = javaMenu.generarPaqueteYClaseDesdeString(file.getAbsolutePath());
                textFieldClaseParaEjecucion.setText(paqueteYClaseParaEjecucion);
            });
        });

        this.getDialogPane().setContent(grid);


        this.setResultConverter(dialogoBoton -> {
            if (dialogoBoton == aceptarButtonType) {
                String[] argResult =textFieldArgumentos.getText().trim().split(" ");
                return new Pair<>(this.paqueteYClaseParaEjecucion, argResult);
            }
            return null;
        });
    }
}
