package trash;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class MenuContextual extends ContextMenu {
    public MenuContextual() {
        MenuItem cortar = new MenuItem("Cortar");
        MenuItem copiar = new MenuItem("Copiar");
        MenuItem pegar = new MenuItem("Pegar");
        MenuItem eliminar = new MenuItem("Eliminar");
        MenuItem seleccionarTodo = new MenuItem("Seleccionar todo");

        this.getItems().addAll(cortar, copiar, pegar, eliminar, seleccionarTodo);


    }
}
