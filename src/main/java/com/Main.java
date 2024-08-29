package com;
import com.ide.Ide;
import com.ide.editor.EditorJava;
import com.ide.editor.fuentes.Fuentes;
import com.ide.lenguaje.JavaMenu;
import com.ide.utils.ConsolaControl;
import com.ide.utils.CustomOutputStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.io.output.TeeOutputStream;

import java.io.*;
import java.util.Optional;

import static java.lang.System.out;

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
        Scene scene = new Scene(ide, 1024, 768);

        //scene.getStylesheets().add(Java.class.getResource("java-keywords.css").toExternalForm());
        //scene.getStylesheets().add(Java.class.getResource("java-keywords.css").toExternalForm());
        ide.getStylesheets().add(EditorJava.class.getResource("java-keywords.css").toExternalForm());
        Fuentes.cargarFuentes();


        //primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IDEJfx");
        primaryStage.show();
        /*
        TextArea textArea = new TextArea();
        Platform.runLater( () -> {

            System.setOut(new PrintStream(new CustomOutputStream(textArea)));
            ide.getJavaMenu().setTextoSalida(textArea);
        });
*/

        ide.hayProyectoAbierto().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                /*
                PrintStream printStream = new PrintStream(new CustomOutputStream(ide.getJavaMenu().getTextoSalida()));
                TeeOutputStream out = new TeeOutputStream(System.out, printStream);
                try {
                    out.write("HI".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
                //TODO CONSOLE
               // System.setOut(new PrintStream(new CustomOutputStream(ide.getJavaMenu().getTextoSalida())));


            }
        });



        //TODO ARREGLAR EL CIERRE DEL PROGRAMA -> PEDIR PARA GUARDAR ANTES DE SALIR?
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //TODO HACER SOLO SI HAY CAMBIO REALIZADO
                if(ide.getPanelPestanya().getTabs().isEmpty() || !ide.getPanelPestanya().hayCambio()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, null, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Salir");
                    alert.setHeaderText("¿Desea cerrar el programa?");
                    ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Salir");
                    ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
                    Optional<ButtonType> resultado = alert.showAndWait();
                    if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
                        Platform.exit();
                        System.exit(0);
                    } else {
                        event.consume();
                    }
                }else if(ide.getPanelPestanya().hayCambio()){
                Alert alert = new Alert(Alert.AlertType.WARNING,null, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL );
                alert.setTitle("Salir");
                alert.setHeaderText("¿Desea guardar los cambios realizados y cerrar el programa?");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText("Guardar");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText("No guardar");
                ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Cancelar");
                Optional<ButtonType> resultado = alert.showAndWait();
                if(resultado.isPresent() && resultado.get() == ButtonType.YES){
                    try {
                        ide.cerrarYGuardar();
                        Platform.exit();
                        System.exit(0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else if(resultado.isPresent() && resultado.get() == ButtonType.NO){
                    Platform.exit();
                    System.exit(0);
                }else {
                    event.consume();
                }
                }
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }


    /*
    @Override
    public void stop() {
        ide.cerrarIDE();
    }*/

}