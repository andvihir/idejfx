package com.ide.pestanas;

import com.ide.editor.EditorSimple;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

import java.io.File;

public class Pestanya extends Tab {
    private EditorSimple editor;
    private PanelPestanya panelPestanya;
    private File archivo;

    public Pestanya(){
        super();
    }

    public Pestanya(String s, EditorSimple editor) {
        super(s);
        this.editor = editor;
    }

    public Pestanya(String s) {
        super(s);
    }

    public Pestanya(String s, EditorSimple editor, PanelPestanya panelPadre) {
        super(s);
        this.editor = editor;
        this.panelPestanya = panelPadre;
    }

    public Pestanya(String s, EditorSimple editor, PanelPestanya panelPadre, File archivo) {
        super(s);
        this.editor = editor;
        this.panelPestanya = panelPadre;
        this.archivo = archivo;
    }

    public EditorSimple getEditor() {
        return editor;
    }

    public void setEditor(EditorSimple editor) {
        this.editor = editor;
    }

    public PanelPestanya getPanelPestanya() {
        return panelPestanya;
    }

    public void setPanelPestanya(PanelPestanya panelPestanya) {
        this.panelPestanya = panelPestanya;
    }

    public File getArchivo() {
        return archivo;
    }

    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
}
