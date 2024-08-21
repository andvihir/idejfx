package com.ide;


import com.ide.controladores.ControladorBarraDirectorios;
import com.ide.controladores.ControladorMenu;
import com.ide.editor.EditorSimple;
import com.ide.editor.EditorJava;
import com.ide.menu.BarraMenu;
import com.ide.pestanas.PanelPestanya;
import com.ide.proyectos.BarraDirectorios;
import com.ide.proyectos.TreeDirectorios;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.*;

public class Ide extends BorderPane{

    //private final BarraMenu barraMenu = new BarraMenu();
    // private final BorderPane borderPane = new BorderPane();
    private SimpleBooleanProperty hayProyectoAbierto = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty hayPestanaAbierta = new SimpleBooleanProperty(false);

    private final BarraMenu barraMenu = new BarraMenu(this);
    //private final ControladoresMenu controladoresMenu = new ControladoresMenu(this);
    /*
    private final EditorSimple editorSimple = new EditorSimple();
    private final EditorJava editorJava = new EditorJava();
    private EditorSimple editor = new EditorSimple();
    private VirtualizedScrollPane<CodeArea> editorView = new VirtualizedScrollPane<>(editor);
    */
    private final VBox vBox = new VBox();
    private final SplitPane splitPaneH = new SplitPane();
    private final SplitPane splitPaneV = new SplitPane();
    private ScrollPane scrollPaneMenuRodapie = new ScrollPane();
    private PanelPestanya panelPestanya = new PanelPestanya();


    private BarraDirectorios barraDirectorios = new BarraDirectorios(this);
    private TreeDirectorios treeDirectorios;
    private ControladorMenu controladorMenu = new ControladorMenu(this);


    public Ide() {

        //----- ESPACIO DE LAS PESTAÑAS -----


        //----- BARRA DE NAVEGACIÓN/PROYECTOS -----

        //


        // setTop(barraMenu);
        //vBox.setPrefSize(600,600);
        //VirtualizedScrollPane<CodeArea> editorView = new VirtualizedScrollPane<>(editorJava);
        //vsp.setPrefSize(800, 800);
       // Tab tab1 = new Tab("AAAA");
       // tab1.setContent(editorView);
       // panelPestanya.getTabs().add(tab1);
        //vBox.getChildren().addAll(panelPestanya);
       // scrollPaneDirectorios.setPrefSize(120,120);
       // setLeft(scrollPaneDirectorios);

        hayPestanaAbierta().bind(panelPestanya.hayPestanaAbierta());

        setTop(barraMenu);
        //splitPaneH.getItems().addAll(barraDirectorios, panelPestanya);
        splitPaneV.setOrientation(Orientation.VERTICAL);
        //splitPaneV.getItems().addAll(splitPaneH, scrollPaneMenuRodapie);
        splitPaneV.getItems().addAll(splitPaneH);
        setVistaInicio();

        /*
        splitPaneH.getItems().addAll(barraDirectorios, panelPestanya);
        splitPaneH.setDividerPositions(0.20);

        splitPaneV.setOrientation(Orientation.VERTICAL);
        //splitPaneV.getItems().addAll(splitPaneH, scrollPaneMenuRodapie);

        splitPaneV.getItems().addAll(splitPaneH);
        splitPaneV.setDividerPositions(0.75);

        setCenter(splitPaneV);
        */

       // this.getChildren().addAll(barraMenu,splitPaneV);
        //setTop(barraMenu);
        //setCenter(splitPaneV);
       //setCenter(new VirtualizedScrollPane<>(javaEditor));
    }


    /*
    private MenuBar crearBarraMenu(){

    }*/
    /*
    public void stopI() {
        editorJava.cerrarEditorJava();
    }*/
    /*
    public void cerrarIDE(){
        if(this.panelPestanya.getTabs()!=null) this.panelPestanya.cerrarTodasPestanas();
    }*/
    public EditorSimple getEditor(){
        if(this.panelPestanya.getPestanyaSeleccionada()==null){
            return null;
        }else return this.panelPestanya.getPestanyaSeleccionada().getEditor();
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

    public SplitPane getSplitPaneV() {
        return splitPaneV;
    }

    public SplitPane getSplitPaneH() {
        return splitPaneH;
    }

    public SimpleBooleanProperty hayProyectoAbierto() {
        return hayProyectoAbierto;
    }
    public SimpleBooleanProperty hayPestanaAbierta(){
        return hayPestanaAbierta;
    }

    /*
    public ReadOnlyBooleanProperty hayArchivoAbierto(Ide ide){
        final SimpleBooleanProperty propiedad = new SimpleBooleanProperty(false);
        if(this.getEditor()!=null){
            propiedad.set(true);
        }

    }*/

    public void cerrarYGuardar() throws IOException {
        this.panelPestanya.cerrarYGuardarTodasPestanas();
    }

    private void setVistaInicio(){
        /*
        splitPaneH.setOrientation(Orientation.HORIZONTAL);
        splitPaneV.setOrientation(Orientation.VERTICAL);
        splitPaneH.getItems().addAll(barraDirectorios);
        splitPaneV.getItems().addAll(splitPaneH);
       // setCenter(splitPaneV);

         */
        StackPane stackPane = new StackPane();
        ScrollPane scrollPane = new ScrollPane();
        setCenter(stackPane);

        Label labelInicio = new Label("Crear o abrir un Proyecto...");
        Button buttonInicio = new Button("Iniciar Proyecto");

        ContextMenu menuContextoInicial = new ContextMenu();
        MenuItem nuevoProyecto = new MenuItem("Nuevo proyecto");
        MenuItem abrirProyectoExistente = new MenuItem("Abrir proyecto existente");

        menuContextoInicial.getItems().addAll(nuevoProyecto, abrirProyectoExistente);
        labelInicio.setContextMenu(menuContextoInicial);
        buttonInicio.setContextMenu(menuContextoInicial);
        scrollPane.setContextMenu(menuContextoInicial);

       // this.setContextMenu(menuContextoInicial);
        labelInicio.setStyle("-fx-font-size: 26");
        stackPane.getChildren().addAll(scrollPane, labelInicio);

        //TODO HACER METODOS
        nuevoProyecto.setOnAction(e -> {
            boolean r = ControladorMenu.crearNuevoProyecto(this);
            if(r) setVistaTotal();

        });

        abrirProyectoExistente.setOnAction(e->{

            boolean r = ControladorMenu.abrirProyectoExistente(this);
            if(r) setVistaTotal();

        });

    }

    public void setVistaTotal(){
        setCenter(splitPaneV);
        setBarraDirectorios(true);
        setPanelPestanya(true);
        setScrollPaneMenuRodapie(true);
    }

    public void cerrarProyecto(){

        this.scrollPaneMenuRodapie.setContent(null);
        this.panelPestanya.getTabs().clear();
        this.barraDirectorios.setContent(null);
        this.treeDirectorios.getRoot().getChildren().clear();
        this.treeDirectorios.setRoot(null);
        setVistaInicio();
        hayProyectoAbierto.set(false);
    }

    public void setBarraDirectorios(boolean b){
        if(b && !splitPaneH.getItems().contains(barraDirectorios)){
            splitPaneH.getItems().addFirst(barraDirectorios);
            splitPaneH.setDividerPositions(0.20);
        }else if(!b && splitPaneH.getItems().contains(barraDirectorios)){
            splitPaneH.getItems().removeAll(barraDirectorios);
        }
    }
    public void setScrollPaneMenuRodapie(boolean b){
        if(b && !splitPaneV.getItems().contains(scrollPaneMenuRodapie)){
            splitPaneV.getItems().addLast(scrollPaneMenuRodapie);
            splitPaneV.setDividerPositions(0.8);
        }else if(!b && splitPaneV.getItems().contains(scrollPaneMenuRodapie)){
            splitPaneV.getItems().removeAll(scrollPaneMenuRodapie);
        }
    }
    public void setPanelPestanya(boolean b){
        if(b && !splitPaneH.getItems().contains(panelPestanya)){
            splitPaneH.getItems().addLast(panelPestanya);
            splitPaneH.setDividerPositions(0.20);
        }else if(!b && splitPaneH.getItems().contains(panelPestanya)){
            splitPaneH.getItems().removeAll(panelPestanya);
        }
    }

    public ControladorMenu getControladorMenu() {
        return controladorMenu;
    }
}
