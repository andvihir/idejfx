package com.ide.lenguaje;

import com.ide.Ide;
import com.ide.controladores.ControladorMenu;
import com.ide.lenguaje.config.DialogoSeleccionarClaseMain;
import com.ide.utils.CustomOutputStream;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;


public class JavaMenu extends BorderPane {

    private Ide ide;
    private Button botonCompilarProyecto = new Button("Compilar proyecto");
    private Button botonCompilarClase = new Button("Compilar clase");
    private Button botonEjecutar = new Button("Ejecutar");
    private TextArea textoSalida = new TextArea();
    private VBox contenedorBotones = new VBox(botonCompilarProyecto, botonEjecutar);
    private Class<?> clase;
    private String claseMain = null;
    private String[] args = new String[]{};
    private ClassLoader classLoader;


    public JavaMenu(Ide ide) {
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
        seleccionarTodo.setOnAction(actionEvent -> {
            this.textoSalida.selectAll();
        });
        limpiar.setOnAction(actionEvent -> {
            this.textoSalida.clear();
        });

        this.textoSalida.setPrefHeight(ide.getScrollPaneMenuRodapie().getHeight());
        this.textoSalida.setPrefWidth(ide.getScrollPaneMenuRodapie().getWidth() - this.botonCompilarProyecto.getWidth());

        ide.getScrollPaneMenuRodapie().widthProperty().addListener((obs, oldVal, newVal) -> {
            this.textoSalida.setPrefWidth(newVal.doubleValue() - this.botonCompilarProyecto.getWidth());
        });
        ide.getScrollPaneMenuRodapie().heightProperty().addListener((obs, oldVal, newVal) -> {
            this.textoSalida.setPrefHeight(newVal.doubleValue());
        });


        ide.hayProyectoAbierto().addListener((obs, oldValue, newValue) -> {
            if (newValue) ide.getScrollPaneMenuRodapie().setContent(this);
        });
        ide.getScrollPaneMenuRodapie().setContent(this);
        this.textoSalida.setEditable(false);

        //TODO CONSOLE
        System.setOut(new PrintStream(new CustomOutputStream(this.textoSalida)));
        System.setErr(new PrintStream(new CustomOutputStream(this.textoSalida)));

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

        botonCompilarProyecto.setOnAction(actionEvent -> {
            try {
                Alert dialogo = new Alert(Alert.AlertType.CONFIRMATION);
                dialogo.setTitle("Compilar proyecto");
                if (this.ide.getPanelPestanya().hayCambio()) {
                    dialogo.setContentText("¿Desea guardar cambios y compilar proyecto?");
                } else {
                    dialogo.setContentText("¿Compilar proyecto?");
                }
                Optional<ButtonType> result = dialogo.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (this.ide.getPanelPestanya().hayCambio()) {
                        this.ide.getPanelPestanya().guardarTodasPestanas();
                    }
                    this.classLoader = compilarCodigoTotalDevuelveClaseTest(FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(ide.getTreeDirectorios().getRootFile(), null, true)));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        botonCompilarClase.setOnAction(actionEvent -> {
            try {
                ControladorMenu.guardarArchivoDesdeEditorConDialogoConfirmacion(this.ide.getEditor().getArchivoReferencia(), ide);

                compilarCodigoSeparado(this.ide.getPanelPestanya().getPestanyaSeleccionada().getArchivo());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        botonEjecutar.setOnAction(actionEvent -> {

            try {
                if (this.ide.getPanelPestanya().hayCambio()) {
                    Alert dialogo = new Alert(Alert.AlertType.CONFIRMATION);
                    dialogo.setTitle("Ejecutar");
                    dialogo.setContentText("Hay cambios en el proyecto. ¿Desea compilar el proyecto y luego ejecutarlo?");

                    Optional<ButtonType> result = dialogo.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        this.ide.getPanelPestanya().guardarTodasPestanas();
                        //this.textoSalida.clear();
                        this.classLoader = compilarCodigoTotalDevuelveClaseTest(FileUtils.convertFileCollectionToFileArray(FileUtils.listFiles(ide.getTreeDirectorios().getRootFile(), null, true)));

                        ejecutarClase();
                    }
                }else{
                    //this.textoSalida.clear();
                    ejecutarClase();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void compilarCodigoSeparado(File file) throws Exception {

        File[] r = new File[]{file};

        Compilador.compilarCodigoSeparado(r, this.textoSalida, this.ide.getTreeDirectorios().getRoot_file());

    }

    public void ejecutarClase() throws Exception {
        if(this.claseMain == null){
            DialogoSeleccionarClaseMain dialogo = new DialogoSeleccionarClaseMain(ide);
            Optional<File> result = dialogo.showAndWait();
            result.ifPresent(file -> {
                this.claseMain = generarPaqueteYClaseDesdeString(file.getAbsolutePath());
            });
        }
        if(this.classLoader!=null) {

                Class<?> clazz = this.classLoader.loadClass(this.claseMain);
                Method myMethod = clazz.getMethod("main", String[].class);
                // Method myMethod = this.clase.getMethod("main", String[].class);
                myMethod.setAccessible(true);
                myMethod.invoke(null, (Object) this.args);
        }else{

                Alert dialogo = new Alert(Alert.AlertType.WARNING);
                dialogo.setTitle("No se puede ejecutar el programa");
                dialogo.setHeaderText("Debes compilar el proyecto primero.");
                dialogo.showAndWait();
        }

    }

    public ClassLoader compilarCodigoTotalDevuelveClaseTest(File[] files) throws Exception {

        return Compilador.compilarCodigoTotalDevuelveClaseTest(files, this.textoSalida, this.ide.getTreeDirectorios().getRoot_file());
    }

    public TextArea getTextoSalida() {
        return textoSalida;
    }

    public void setTextoSalida(TextArea textoSalida) {
        this.textoSalida = textoSalida;
    }

    public String generarPaqueteYClaseDesdeString(String name){
        StringBuilder sb = new StringBuilder();
        //System.out.println(name);
        String pathroot = this.ide.getTreeDirectorios().getRoot_file().getAbsolutePath()+"\\";
        String aPartirDeRaiz = name.replace(pathroot, "");
        //System.out.println(aPartirDeRaiz);

        //String carpeta_raizNombre = FilenameUtils.getBaseName(this.ide.getTreeDirectorios().getRoot_file().getName());
        //System.out.println(carpeta_raizNombre);
        //String name1 = name.substring(name.indexOf(carpeta_raizNombre)+carpeta_raizNombre.length());
        //String[] split = name1.split("\\\\");
        String[] split = aPartirDeRaiz.split("\\\\");
        for(String s:split){
            if(!s.contains(".java")){
                sb.append(s);
                sb.append(".");
            }else if (s.contains(".java")){
                sb.append(s.substring(0, s.indexOf(".java")));
            }
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public String getClaseMain() {
        return claseMain;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void setClaseMain(String claseMain) {
        this.claseMain = claseMain;
    }
}
