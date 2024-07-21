package com.ide.editor.fuentes;

import com.Main;
import javafx.scene.text.Font;

public class Fuentes {

    public static void cargarFuentes(){

        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-Regular.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-Bold.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-BoldItalic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-ExtraBold.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-ExtraBoldItalic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-ExtraLight.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-ExtraLightItalic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-Italic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-LightItalic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-Medium.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-MediumItalic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-SemiBold.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-SemiBoldItalic.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-Thin.ttf").toExternalForm(), 14);
        Font.loadFont(Fuentes.class.getResource("JetBrainsMono-ThinItalic.ttf").toExternalForm(), 14);
    }
}
