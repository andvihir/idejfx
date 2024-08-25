package com.ide.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsolaControl {

    private static TextArea console;
    private static PrintStream ps;

    public static void initialize() {
        ps = new PrintStream(new Consola(console));
    }

    public static void leer() {
        System.setOut(ps);
        System.setErr(ps);
        System.out.println("Hello World");
    }

    public static class Consola extends OutputStream {
        public static TextArea console = new TextArea();


        public Consola(TextArea console) {
            Consola.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char) b));
        }
    }
}
