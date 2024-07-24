package com.ide.editor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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

public class EditorSimple extends CodeArea {

    private File archivoReferencia;

    public EditorSimple() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));

        //-----**** MENU CONTEXTUAL ****-----
        MenuContextualEditor menuContextualEditor = new MenuContextualEditor(this);

        //this.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
        //this.getStyleClass().add("editorSimple");
        //menuContextual.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");


    }
    public File getArchivoReferencia(){
        return this.archivoReferencia;
    }
    public void setArchivoReferencia(File archivoReferencia){
        this.archivoReferencia = archivoReferencia;
    }

    }
