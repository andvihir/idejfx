package com.ide.utils;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class CustomOutputStream extends OutputStream {
    private TextArea textArea;

    public CustomOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.appendText(String.valueOf((char)b));
    }
}