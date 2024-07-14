package com.ide.editor;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class EditorSimple extends CodeArea {
    public EditorSimple() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
    }
}
