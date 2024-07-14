package com;
import com.ide.Ide;
import com.ide.editor.Java;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.function.IntFunction;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainold.fxml"));
        Parent root = loader.load();
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("img/EdenCodingIcon.png")));
        primaryStage.setTitle("Simple EdenCoding JavaFX File Editor");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
         */

       // CodeArea codeArea = new CodeArea();
       // codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

       // codeArea.setContextMenu(new DefaultContextMenu());
       // Scene scene = new Scene(codeArea);

        //Scene scene = new Scene(new StackPane(new VirtualizedScrollPane<>(codeArea)), 600, 400);
       // scene.getStylesheets().add(Main.class.getResource("java-keywords.css").toExternalForm());

       // StackPane root = new StackPane(new VirtualizedScrollPane<>(codeArea));
       // TabPane tabPane = new TabPane();

        //Tab tab1 = new Tab("Tab 1");
       // tab1.setText("Tab 1");
       // tab1.setContent(new StackPane(new VirtualizedScrollPane<>(codeArea)));

       // tabPane.getTabs().add(tab1);
       // root.getChildren().add(tabPane);

        Ide ide = new Ide();
        Scene scene = new Scene(ide, 1200, 960);
        scene.getStylesheets().add(Java.class.getResource("java-keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java Demo");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}