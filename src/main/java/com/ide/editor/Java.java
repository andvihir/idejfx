package com.ide.editor;

import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Java {

    private CodeArea codeArea;
    private ExecutorService executor;

    private static final String[] PALABRAS_RESERVADAS = new String[] {
        "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const", "continue",
            "default", "do", "double", "else", "enum", "extends",
             "final", "finally", "float", "for", "goto",
            "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "package", "private",
            "protected", "public", "return", "short", "static",
            "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile",
            "while"
    };

    private static final String PALABRAS_RESERVADAS_PATRON = "\\b(" + String.join("|", PALABRAS_RESERVADAS) + ")\\b";
    private static final String PARENTESIS_PATRON = "\\(|\\)";
    private static final String LLAVES_PATRON = "\\{|\\}";
    private static final String CORCHETES_PATRON = "\\[|\\]";
    private static final String PUNTO_Y_COMA_PATRON = "\\;";
    private static final String STRING_PATRON = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMENTARIO_PATRON = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String LLAMADA_A_VARIABLE_PATRON = "\\b([a-z][A-z]+?\\.)\\b";
    private static final String CLASE_PATRON = "\\b([A-Z][A-z]+)\\b";

   // private static final String NUMERO_PATRON = ;

    private static final Pattern PATRON = Pattern.compile(
            "|(?<PALABRASRESERVADAS>" + PALABRAS_RESERVADAS_PATRON + ")" +
            "(?<COMENTARIO>" + COMENTARIO_PATRON + ")" +
            "|(?<STRING>" + STRING_PATRON + ")" +
            "|(?<CLASE>" + CLASE_PATRON + ")" +
            "|(?<LLAMADAVAR>" + LLAMADA_A_VARIABLE_PATRON + ")" +
            "|(?<PARENTESIS>" + PARENTESIS_PATRON + ")" +
            "|(?<CORCHETES>" + CORCHETES_PATRON + ")" +
            "|(?<PUNTOYCOMA>" + PUNTO_Y_COMA_PATRON + ")" +
            "|(?<LLAVES>" + LLAVES_PATRON + ")"
            );




    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATRON.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass = getStyleClass(matcher);
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static String getStyleClass(Matcher matcher) {
        String styleClass =
                matcher.group("PALABRASRESERVADAS") != null ? "keyword" :
                        matcher.group("PARENTESIS") != null ? "paren" :
                                matcher.group("LLAVES") != null ? "brace" :
                                        matcher.group("CORCHETES") != null ? "bracket" :
                                                matcher.group("PUNTOYCOMA") != null ? "semicolon" :
                                                        matcher.group("STRING") != null ? "string" :
                                                                matcher.group("COMENTARIO") != null ? "comment" :
                                                                        null; /* never happens */
        assert styleClass != null;
        return styleClass;
    }


}
