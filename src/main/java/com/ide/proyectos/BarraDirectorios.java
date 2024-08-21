package com.ide.proyectos;

import com.dlsc.formsfx.model.validators.CustomValidator;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.ide.Ide;
import com.ide.controladores.ControladorBarraDirectorios;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.beans.EventHandler;

public class BarraDirectorios extends ScrollPane {
    private TreeDirectorios.MenuContextualDirectorios menuContextoDirectorios;

    public BarraDirectorios(Ide ide) {
        this.setFitToHeight(true);
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.menuContextoDirectorios = new TreeDirectorios.MenuContextualDirectorios(ide);
        this.setContextMenu(this.menuContextoDirectorios);

        //inicio();
      // this.setContextMenu(new MenuContextualDirectorios());
    }
    /*
    private void inicio() {

        //PONER TEXTO INICIAL: DE CREAR O ABRIR PROYECTO
    //
        Label labelInicio = new Label("Crear o abrir un Proyecto");
       // labelInicio.layoutXProperty().bind(this.widthProperty().subtract(labelInicio.widthProperty()).divide(2));
       // labelInicio.layoutYProperty().bind(this.widthProperty().subtract(labelInicio.widthProperty()).divide(2));
        this.setContent(labelInicio);
        //this.setFitToHeight(true);
        //labelInicio.setOpaqueInsets(new Insets(200,100,20,10));
        //labelInicio.setAlignment(Pos.CENTER);

        ContextMenu menuContextoInicial = new ContextMenu();
        MenuItem nuevoProyecto = new MenuItem("Nuevo proyecto");
        MenuItem abrirProyectoExistente = new MenuItem("Abrir proyecto existente");

        menuContextoInicial.getItems().addAll(nuevoProyecto, abrirProyectoExistente);
        this.setContextMenu(menuContextoInicial);

        //
        nuevoProyecto.setOnAction(e -> {
            ControladorBarraDirectorios.crearNuevoProyecto();
                }
        );
    }
    */

}
