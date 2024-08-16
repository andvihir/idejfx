package com.ide.proyectos;

import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.ide.controladores.ControladorBarraDirectorios;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.beans.EventHandler;

public class BarraDirectorios extends ScrollPane {
    public BarraDirectorios() {
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        inicio();
      // this.setContextMenu(new MenuContextualDirectorios());
    }
    private void inicio() {

        //PONER TEXTO INICIAL: DE CREAR O ABRIR PROYECTO

        Label labelInicio = new Label("Inicio");
        VBox vBox = new VBox(10);
        Label labelInicio1 = new Label("Inicio2");
        vBox.getChildren().addAll(labelInicio, labelInicio1);

        this.setContent(labelInicio);
        this.setFitToHeight(true);
        this.setFitToHeight(true);

        ContextMenu menuContextoInicial = new ContextMenu();
        MenuItem nuevoProyecto = new MenuItem("Nuevo proyecto");
        MenuItem abrirProyectoExistente = new MenuItem("Abrir proyecto existente");

        menuContextoInicial.getItems().addAll(nuevoProyecto, abrirProyectoExistente);
        this.setContextMenu(menuContextoInicial);

        //TODO HACER METODOS
        nuevoProyecto.setOnAction(e -> {
            ControladorBarraDirectorios.crearNuevoProyecto();
                }
        );
    }
}
