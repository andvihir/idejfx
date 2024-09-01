package com.ide.editor;

import com.Main;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import org.reactfx.collection.ListModification;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorSimple extends CodeArea {

    private File archivoReferencia;
    private Boolean modificado = false;

    public EditorSimple() {
        super();
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));


    /*
        this.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed


            }
        });
*/
        //-----**** MENU CONTEXTUAL ****-----
        MenuContextualEditor menuContextualEditor = new MenuContextualEditor(this);

        //this.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
        //this.getStyleClass().add("editorSimple");
        //menuContextual.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");

    }
    public EditorSimple(String text){
        super(text);
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        MenuContextualEditor menuContextualEditor = new MenuContextualEditor(this);

    }
    public File getArchivoReferencia(){
        return this.archivoReferencia;
    }
    public void setArchivoReferencia(File archivoReferencia){
        this.archivoReferencia = archivoReferencia;
    }

    public void buscarTexto(String texto, boolean esSiguiente, IntegerProperty indice){
        if (texto == null || texto.isEmpty()){
            return;
        }
        Pattern pattern = Pattern.compile(texto);
        Matcher matcher = pattern.matcher(getText());
        int indiceNuevo = esSiguiente ? indice.get()+1 : indice.get()-1;
        int contador = 0;
        while (matcher.find()){
            if(contador == indiceNuevo){
                this.selectRange(matcher.start(), matcher.end());
                this.showParagraphAtTop(this.getCurrentParagraph());
                indice.set(indiceNuevo);
                break;
            }
            contador++;
        }
        //ES ULTIMO Y SIGUIENTE:
        if(indiceNuevo == indice.get()+1){
            matcher = pattern.matcher(getText());
            if(matcher.find()){
            this.selectRange(matcher.start(), matcher.end());
                this.showParagraphAtTop(this.getCurrentParagraph());
            indice.set(0);}
        }
    }

    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }
}
