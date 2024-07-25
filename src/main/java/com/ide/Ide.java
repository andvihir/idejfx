package com.ide;


import com.ide.controladores.ControladorMenu;
import com.ide.editor.EditorSimple;
import com.ide.editor.EditorJava;
import com.ide.menu.BarraMenu;
import com.ide.pestanas.PanelPestanya;
import com.ide.proyectos.BarraDirectorios;
import com.ide.proyectos.TreeDirectorios;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.*;

public class Ide extends BorderPane{

    //private final BarraMenu barraMenu = new BarraMenu();
    // private final BorderPane borderPane = new BorderPane();

    private final BarraMenu barraMenu = new BarraMenu(this);
    //private final ControladoresMenu controladoresMenu = new ControladoresMenu(this);
    private final EditorSimple editorSimple = new EditorSimple();
    private final EditorJava editorJava = new EditorJava();
    private EditorSimple editor = new EditorSimple();
    private VirtualizedScrollPane<CodeArea> editorView = new VirtualizedScrollPane<>(editor);
    private final VBox vBox = new VBox();
    private final SplitPane splitPaneH = new SplitPane();
    private final SplitPane splitPaneV = new SplitPane();
    private final ScrollPane scrollPaneMenuRodapie = new ScrollPane();
    private final PanelPestanya panelPestanya = new PanelPestanya();

    private final BarraDirectorios barraDirectorios = new BarraDirectorios();
    private TreeDirectorios treeDirectorios;
    private ControladorMenu controladorMenu = new ControladorMenu(this);


    public Ide() {

        //----- ESPACIO DE LAS PESTAÑAS -----


        //----- BARRA DE NAVEGACIÓN/PROYECTOS -----

        //


        // setTop(barraMenu);
        //vBox.setPrefSize(600,600);
        VirtualizedScrollPane<CodeArea> editorView = new VirtualizedScrollPane<>(editorJava);
        //vsp.setPrefSize(800, 800);
       // Tab tab1 = new Tab("AAAA");
       // tab1.setContent(editorView);
       // panelPestanya.getTabs().add(tab1);
        //vBox.getChildren().addAll(panelPestanya);
       // scrollPaneDirectorios.setPrefSize(120,120);
       // setLeft(scrollPaneDirectorios);
        splitPaneH.setDividerPositions(0.21);
        splitPaneH.getItems().addAll(barraDirectorios, panelPestanya);


        splitPaneV.setOrientation(Orientation.VERTICAL);
        splitPaneV.setDividerPositions(0.76);
        splitPaneV.getItems().addAll(splitPaneH, scrollPaneMenuRodapie);
       // this.getChildren().addAll(barraMenu,splitPaneV);
        setTop(barraMenu);
        setCenter(splitPaneV);
        //setTop(barraMenu);
        //setCenter(splitPaneV);
       //setCenter(new VirtualizedScrollPane<>(javaEditor));
    }


    /*
    private MenuBar crearBarraMenu(){

    }*/
    public void stopI() {
        editorJava.stopJ();
    }
    public EditorSimple getEditor(){
        return this.panelPestanya.getPestanyaSeleccionada().getEditor();
    }
/*
    public void cargarEditorSimple(){
        this.editor = this.editorSimple;
        editorView = new VirtualizedScrollPane<>(editor);
    }
    public void cargarEditorJava(){
        this.editor = this.editorJava;
    }
 */

    public TreeDirectorios getTreeDirectorios(){
        return this.treeDirectorios;
    }
    public void setTreeDirectorios(TreeDirectorios v){
        this.treeDirectorios = v;
    }
    public BarraDirectorios getBarraDirectorios() {
        return this.barraDirectorios;
    }
    public BarraMenu getBarraMenu(){
        return this.barraMenu;
    }
    public PanelPestanya getPanelPestanya(){
        return this.panelPestanya;
    }
}
