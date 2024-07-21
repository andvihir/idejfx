package com.ide.pestanas;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.*;

public class PanelPestanya extends AnchorPane {

    private final HBox panelPestanyas = new HBox();
    private final AnchorPane panelContenido = new AnchorPane();

    private final ObservableList<Tab> pestanyas = FXCollections.observableArrayList();
    private final ObjectProperty<Tab> pestanyaSeleccionada = new SimpleObjectProperty<>();

    public PanelPestanya(Tab... pestanyas) {

        this.pestanyas.addAll(pestanyas);
        this.pestanyaSeleccionada.addListener((propiedad, oldValue, newValue) -> {});
        


    }

}
