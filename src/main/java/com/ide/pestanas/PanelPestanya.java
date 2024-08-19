package com.ide.pestanas;

import com.ide.controladores.ControladorMenu;
import com.ide.editor.EditorJava;
import com.ide.editor.EditorSimple;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class PanelPestanya extends TabPane {
    /*
    private final HBox panelPestanyas = new HBox();
    private final AnchorPane panelContenido = new AnchorPane();

    private final ObservableList<Tab> pestanyas = FXCollections.observableArrayList();
    private final ObjectProperty<Tab> pestanyaSeleccionada = new SimpleObjectProperty<>();

    public PanelPestanya(Tab... pestanyas) {

        this.pestanyas.addAll(pestanyas);
        this.pestanyaSeleccionada.addListener((propiedad, oldValue, newValue) -> {});
    }
    */
    private SimpleBooleanProperty hayPestanaAbierta = new SimpleBooleanProperty(false);

    private final AnchorPane panelContenido = new AnchorPane();
    private final ObservableList<Pestanya> pestanyas = FXCollections.observableArrayList();
   // private final ObjectProperty<Tab> pestanyaSeleccionada = new SimpleObjectProperty<>();
    private Pestanya pestanyaSeleccionada;


    public PanelPestanya(){
        this.setSide(Side.TOP);



    }

    private void seleccionar(Pestanya newValue, Pestanya oldValue) {
        panelContenido.getChildren().setAll(newValue.getContent());
    }

    public void abrirPestana(EditorSimple editor, String nombre){
        Pestanya pestana = new Pestanya(nombre, editor, this);
        pestana.setContent(editor);
        //pestana.setContent();
        this.getTabs().add(pestana);
        this.setPestanyaSeleccionada(pestana);
        this.getSelectionModel().select(pestana);

        controlPestanya(pestana);
        hayPestanaAbierta.set(true);
    }
    public void abrirPestana(EditorSimple editor, String nombre, File archivo){
        Pestanya pestana = new Pestanya(nombre, editor, this, archivo);

        VirtualizedScrollPane<CodeArea> editorView = new VirtualizedScrollPane<>(editor);

        pestana.setContent(editorView);

        this.getTabs().add(pestana);
        this.setPestanyaSeleccionada(pestana);
        this.getSelectionModel().select(pestana);

        controlPestanya(pestana);
        hayPestanaAbierta.set(true);

    }


    public void cerrarPestana(EditorSimple editor, Tab tab) {
        this.getTabs().remove(tab);
        if(editor instanceof EditorJava){
            ((EditorJava) editor).cerrarEditorJava();
        }
        if(this.getTabs().isEmpty()) hayPestanaAbierta.set(false);
    }

    public void cerrarYGuardarTodasPestanas() throws IOException {
        for(Tab t : this.getTabs()){
            if(t instanceof Pestanya){
                if(((Pestanya) t).getEditor().getModificado()) {
                    guardarArchivo(((Pestanya) t).getEditor().getArchivoReferencia(), (Pestanya) t);
                }
                /*
                //EditorSimple editor = ((Pestanya) t).getEditor();

                if(editor instanceof EditorJava){
                    ((EditorJava) editor).cerrarEditorJava();
                }*/
            }
        }
        this.getTabs().removeAll();
        hayPestanaAbierta.set(false);
    }

    public Pestanya getPestanyaSeleccionada(){
        if(this.pestanyaSeleccionada == null){
            return null;
        }else return this.pestanyaSeleccionada;
    }
    private void setPestanyaSeleccionada(Pestanya tab){
        this.pestanyaSeleccionada = tab;
    }

    private static void controlPestanya(Pestanya pestana){
        pestana.setOnSelectionChanged(new EventHandler<>() {

            @Override
            public void handle(Event event) {
                if(pestana.isSelected()){
                    pestana.getPanelPestanya().setPestanyaSeleccionada(pestana);
                    System.out.println(pestana.getText() + " seleccionada.");
                }
            }
        });

        pestana.setOnCloseRequest((Event event) -> {
            //TODO HACER SOLO SI HAY CAMBIO REALIZADO
            if(!pestana.getEditor().getModificado()) {
                pestana.getPanelPestanya().cerrarPestana(pestana.getEditor(), pestana);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.WARNING,null, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL );
            alert.setTitle("Cerrar pestaña");
            alert.setHeaderText("¿Desea guardar los cambios efectuados en '"+pestana.getText()+"'?");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Guardar");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText("No guardar");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
            Optional<ButtonType> resultado = alert.showAndWait();
            if(resultado.isPresent() && resultado.get() == ButtonType.YES){
                try {
                    guardarArchivo(pestana.getEditor().getArchivoReferencia(), pestana);
                    pestana.getPanelPestanya().cerrarPestana(pestana.getEditor(), pestana);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if(resultado.isPresent() && resultado.get() == ButtonType.NO){
                pestana.getPanelPestanya().cerrarPestana(pestana.getEditor(), pestana);
            }else {
                event.consume();
            }

        });

    }

    private void handleCloseRequest(Event event) {


    }

    private static void guardarArchivo(File archivoReferencia, Pestanya pestana) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(pestana.getEditor().getText());
        myWriter.close();
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }

    public boolean estaArchivoYaAbierto(File archivo){
        for(Tab p : this.getTabs()){
            if(p instanceof Pestanya){
                if(((Pestanya) p).getArchivo().getAbsolutePath().equals(archivo.getAbsolutePath())){

                    this.setPestanyaSeleccionada((Pestanya)  p);
                    this.getSelectionModel().select((Pestanya)  p);

                    controlPestanya((Pestanya)  p);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hayCambio() {
        boolean hayCambio = false;
        for (Tab t : this.getTabs()) {
            if (t instanceof Pestanya) {
                if (((Pestanya) t).getEditor().getModificado()){
                    hayCambio = true;
                    return hayCambio;
            }
        }
    }
        return hayCambio;
    }

    public SimpleBooleanProperty hayPestanaAbierta(){
        return hayPestanaAbierta;
    }
}
