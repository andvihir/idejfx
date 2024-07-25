package com.ide.pestanas;

import com.ide.editor.EditorSimple;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

public class Pestanya extends Tab {
    private EditorSimple editor;
    private PanelPestanya panelPestanya;

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
}
