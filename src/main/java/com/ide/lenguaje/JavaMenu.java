package com.ide.lenguaje;

import com.ide.Ide;
import com.ide.controladores.ControladorMenu;
import com.ide.utils.ConsolaControl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Text;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;

import static com.ide.utils.MiConsola.leer;


public class JavaMenu extends BorderPane {

    private Ide ide;
    private Button botonCompilarProyecto = new Button("Compilar proyecto");
    private Button botonCompilarClase = new Button("Compilar clase");
    private Button botonEjecutar = new Button("Ejecutar");
    private TextArea textoSalida = new TextArea();
    private VBox contenedorBotones = new VBox(botonCompilarProyecto, botonCompilarClase, botonEjecutar);
    private Class<?> clase;


    public JavaMenu(Ide ide){
        this.ide = ide;

        this.setLeft(contenedorBotones);
        this.setCenter(textoSalida);

        //MENU DE CONTEXTO DEL TEXT AREA
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem seleccionarTodo = new MenuItem("Seleccionar todo");
        MenuItem limpiar = new MenuItem("Limpar");

        this.botonCompilarClase.setMaxWidth(Double.MAX_VALUE);
        this.botonEjecutar.setMaxWidth(Double.MAX_VALUE);

        this.textoSalida.setContextMenu(new ContextMenu(copiar, seleccionarTodo, limpiar));
        copiar.setOnAction(actionEvent -> {
            this.textoSalida.copy();
        });
        seleccionarTodo.setOnAction(actionEvent ->{
            this.textoSalida.selectAll();
        });
        limpiar.setOnAction(actionEvent -> {
            this.textoSalida.clear();
        });

        this.textoSalida.setPrefHeight(ide.getScrollPaneMenuRodapie().getHeight());
        this.textoSalida.setPrefWidth(ide.getScrollPaneMenuRodapie().getWidth()-this.botonCompilarProyecto.getWidth());

        ide.getScrollPaneMenuRodapie().widthProperty().addListener((obs, oldVal, newVal) -> {
            this.textoSalida.setPrefWidth(newVal.doubleValue()-this.botonCompilarProyecto.getWidth());
        });
        ide.getScrollPaneMenuRodapie().heightProperty().addListener((obs, oldVal, newVal) -> {
            this.textoSalida.setPrefHeight(newVal.doubleValue());
        });


        ide.hayProyectoAbierto().addListener((obs, oldValue, newValue) -> {
            if(newValue) ide.getScrollPaneMenuRodapie().setContent(this);
        });
        ide.getScrollPaneMenuRodapie().setContent(this);
        this.textoSalida.setEditable(false);


        botonCompilarProyecto.disableProperty().bind(this.ide.hayProyectoAbierto().not());
        botonCompilarClase.disableProperty().bind(this.ide.hayPestanaAbierta().not());
        //botonEjecutar.disableProperty().bind(this.ide.hayPestanaAbierta().not());

        /*
        botonCompilar.setOnAction(actionEvent ->{
            try{
                ControladorMenu.guardarArchivoDesdeEditorConDialogoConfirmacion(this.ide.getEditor().getArchivoReferencia(), ide);
                compilarCodigoSeparado(ide.getPanelPestanya().getPestanyaSeleccionada().getArchivo());
            }catch (Exception e){
                e.printStackTrace();
            }
        });

         */

        botonCompilarProyecto.setOnAction(actionEvent ->{
            try{

                this.clase = compilarCodigoTotalDevuelveClaseTest(FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(ide.getTreeDirectorios().getRootFile(), null, true)));

            }catch (Exception e){
                e.printStackTrace();
            }

        });

        botonCompilarClase.setOnAction(actionEvent ->{
            try{
                ControladorMenu.guardarArchivoDesdeEditorConDialogoConfirmacion(this.ide.getEditor().getArchivoReferencia(), ide);

                compilarCodigoSeparado(this.ide.getPanelPestanya().getPestanyaSeleccionada().getArchivo());

            }catch (Exception e){
                e.printStackTrace();
            }
        });

        botonEjecutar.setOnAction(actionEvent ->{
                if(this.clase==null) return;
                //ControladorMenu.guardarArchivoDesdeEditorConDialogoConfirmacion(this.ide.getEditor().getArchivoReferencia(), ide);
                //compilarCodigoSeparado(this.ide.getPanelPestanya().getPestanyaSeleccionada().getArchivo());
                StringBuilder stringBuilder = new StringBuilder();
                try{
                    Method myMethod = this.clase.getMethod("main", String[].class);
                    myMethod.setAccessible(true);
                    String[] input = new String[] {};
                    myMethod.invoke(null, (Object) input);

                }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

        });
    }

    public void compilarCodigoSeparado(File file) throws Exception {

        /*
        StringBuilder codigo = new StringBuilder("package test;\n\nclass QuickRunner{\n\t\" + " +
                                "public static void main(String[] args) {");
        for(String linea: ide.getEditor().getText().split("\n")){
            System.out.println("Linea: " +linea);
            codigo.append("\n\t\t").append(linea.startsWith("\n") ? linea.substring(1) : linea);
        }
        codigo.append("\n\t}\n}");
         */
        File[] r = new File[]{file};
        //String result = Compilador.compilar(r, this.textoSalida);
       // this.textoSalida.setText(result);

        Compilador.compilarCodigoSeparado(r, this.textoSalida, this.ide.getTreeDirectorios().getRoot_file());

    }

    public void compilarCodigoTotal(File[] files) throws Exception {

        /*
        StringBuilder codigo = new StringBuilder("package test;\n\nclass QuickRunner{\n\t\" + " +
                                "public static void main(String[] args) {");
        for(String linea: ide.getEditor().getText().split("\n")){
            System.out.println("Linea: " +linea);
            codigo.append("\n\t\t").append(linea.startsWith("\n") ? linea.substring(1) : linea);
        }
        codigo.append("\n\t}\n}");
         */
        //String result = Compilador.compilar(r, this.textoSalida);
        // this.textoSalida.setText(result);

        Compilador.compilarCodigoTotalTest(files, this.textoSalida, this.ide.getTreeDirectorios().getRoot_file());

    }
    public void ejecutarClase(File file) throws Exception {


    }
    public Class<?> compilarCodigoTotalDevuelveClaseTest(File[] files) throws Exception {


        return Compilador.compilarCodigoTotalDevuelveClaseTest(files, this.textoSalida, this.ide.getTreeDirectorios().getRoot_file());
    }

    public TextArea getTextoSalida() {
        return textoSalida;
    }

    public void setTextoSalida(TextArea textoSalida) {
        this.textoSalida = textoSalida;
    }
}
