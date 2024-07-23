package com.ide.pestanas;

import com.ide.editor.EditorSimple;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.*;

import java.io.File;

public class PanelPestanya extends TabPane {

    private final HBox panelPestanyas = new HBox();
    private final AnchorPane panelContenido = new AnchorPane();

    private final ObservableList<Tab> pestanyas = FXCollections.observableArrayList();
    private final ObjectProperty<Tab> pestanyaSeleccionada = new SimpleObjectProperty<>();

    public PanelPestanya(Tab... pestanyas) {

        this.pestanyas.addAll(pestanyas);
        this.pestanyaSeleccionada.addListener((propiedad, oldValue, newValue) -> {});



    }

    public void abrirPestana(EditorSimple editorSimple, String nombre){
        Tab pestana = new Tab(nombre);
        pestana.setContent(editorSimple);
        //pestana.setContent();
        this.getTabs().add(pestana);
    }
    public void cerrarPestana(EditorSimple editorSimple, String nombre, Tab tab){
        this.getTabs().remove(tab);

        }


}
