package com;
import com.ide.Ide;
import com.ide.editor.EditorJava;
import com.ide.editor.fuentes.Fuentes;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private final Ide ide = new Ide();

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

        ide.getStyleClass().add("ide");
        Scene scene = new Scene(ide, 1200, 960);
        //scene.getStylesheets().add(Java.class.getResource("java-keywords.css").toExternalForm());
        //scene.getStylesheets().add(Java.class.getResource("java-keywords.css").toExternalForm());
        ide.getStylesheets().add(EditorJava.class.getResource("java-keywords.css").toExternalForm());
        Fuentes.cargarFuentes();

        primaryStage.setScene(scene);
        primaryStage.setTitle("IDE Java UNED");
        primaryStage.show();
        
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        ide.stopI();
    }

}