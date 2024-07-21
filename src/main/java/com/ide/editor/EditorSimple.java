package com.ide.editor;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import org.reactfx.collection.ListModification;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;

public class EditorSimple extends CodeArea {

    private ExecutorService executor;

    public EditorSimple() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        ContextMenu menuContextual = new ContextMenu();
        this.setContextMenu(menuContextual);

        MenuItem cortar = new MenuItem("Cortar");
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem pegar = new MenuItem("Pegar");
        MenuItem eliminar = new MenuItem("Eliminar");
        MenuItem seleccionarTodo = new MenuItem("Seleccionar todo");

        cortar.setOnAction(actionEvent -> cut());
        copiar.setOnAction(actionEvent -> copy());
        pegar.setOnAction(actionEvent -> paste());
        eliminar.setOnAction(actionEvent -> replaceSelection(""));
        seleccionarTodo.setOnAction(actionEvent -> selectAll());

        menuContextual.getItems().addAll(cortar, copiar, pegar, eliminar, seleccionarTodo);


        //this.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
        //this.getStyleClass().add("editorSimple");
        //menuContextual.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");


    }
    }
