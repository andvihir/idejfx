package com.ide.menu;

import com.ide.proyectos.MenuContextualDirectorios;
import com.ide.proyectos.TreeDirectorios;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DialogoNuevoProyecto extends Dialog<Pair<String, String>>{

    private final ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
    private final ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final GridPane grid = new GridPane();

    public DialogoNuevoProyecto(){

        //<Pair<String, String>> dialogo = new Dialog<>();

        this.setTitle("Nuevo Proyecto");
        //dialogo.setHeaderText("Nuevo Proyecto");

       // ButtonType aceptarButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
       // ButtonType cancelarButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().addAll(aceptarButtonType, cancelarButtonType);

       // GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre: ");
        TextField ubicacion = new TextField();
        ubicacion.setPromptText("Ubicación: ");
        ubicacion.setText(System.getProperty("user.home"));

        Label destino = new Label("Destino: ");
        Label destinoRuta = new Label();

        Label ubicacionNoExiste = new Label("Esta ubicación no existe");
        ubicacionNoExiste.setVisible(false);
        ubicacionNoExiste.setStyle("-fx-text-fill: red;");

        Label carpetaProyectoYaExiste = new Label("El destino para el proyecto ya existe");
        carpetaProyectoYaExiste.setVisible(false);
        carpetaProyectoYaExiste.setStyle("-fx-text-fill: red;");
        //TODO PROPIEDAD
        // ubicacionNoExiste.visibleProperty().bind(ubicacion.textProperty().isEmpty());
        // ubicacionNoExiste.visibleProperty().bind(ubicacion.textProperty().);


        Button elegirBoton  = new Button("Elegir...");

        elegirBoton.setOnAction( e -> {
            String ruta = getRutaSeleccion(ubicacion.getText());
            ubicacion.setText(ruta);
        });

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombre, 1, 0);
        grid.add(new Label("Ubicacion:"), 0, 1);
        grid.add(ubicacion, 1, 1);
        grid.add(elegirBoton, 2, 1);
        grid.add(destino, 0,2);
        grid.add(destinoRuta, 1, 2);
        grid.add(ubicacionNoExiste, 0, 3);
        grid.add(carpetaProyectoYaExiste, 0, 4);

        Node aceptarBoton = this.getDialogPane().lookupButton(aceptarButtonType);
        aceptarBoton.setDisable(true);

        nombre.textProperty().addListener((observable, oldValue, newValue) -> {
            destinoRuta.setText(ubicacion.getText()+"\\"+newValue);
            carpetaProyectoYaExiste.setVisible(!newValue.trim().isEmpty() && Files.isDirectory(Paths.get(destinoRuta.getText())));
            aceptarBoton.setDisable(newValue.trim().isEmpty() || ubicacionNoExiste.isVisible() || Files.isDirectory(Paths.get(destinoRuta.getText())) );
        });

        ubicacion.textProperty().addListener((observable, oldValue, newValue) -> {
            ubicacionNoExiste.setVisible(!Files.isDirectory(Paths.get(newValue)));
            destinoRuta.setText(newValue+"\\"+nombre.getText());
            aceptarBoton.setDisable(!Files.isDirectory(Paths.get(newValue)) || Files.isDirectory(Paths.get(destinoRuta.getText())));
            carpetaProyectoYaExiste.setVisible(!nombre.getText().trim().isEmpty() && Files.isDirectory(Paths.get(destinoRuta.getText())));
        });
        /*
        ubicacion.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO deshabilitar el boton aceptar cuando no es valido path (no existe)
            aceptarBoton.setDisable(true);

        });
*/
        this.getDialogPane().setContent(grid);
        Platform.runLater(() -> nombre.requestFocus());

        this.setResultConverter(dialogoBoton ->{
            if(dialogoBoton == aceptarButtonType){
                return new Pair<>(nombre.getText(), ubicacion.getText());
            }
            return null;
        });
    }

    private String getRutaSeleccion(String directorioTextField){
        String result=directorioTextField;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Abrir Carpeta");

        //TODO comprobar
        if(!directorioTextField.isEmpty() && Files.isDirectory(Paths.get(directorioTextField)) ){
            directoryChooser.setInitialDirectory(Paths.get(directorioTextField).toFile());
        }else{
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        /*
        if(directorioTextField.isEmpty() || !Files.isDirectory(Paths.get(directorioTextField)) ){
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }else{
            directoryChooser.setInitialDirectory(new File(directorioTextField));
        }
         */
        File archivo = directoryChooser.showDialog(null);
        if (archivo != null) {
            try {
                result = archivo.getAbsolutePath();
            } catch (Exception ignored) {
                System.out.println(ignored);
            }
        }
        return result;
    }

}
