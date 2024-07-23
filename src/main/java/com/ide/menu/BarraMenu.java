package com.ide.menu;

import com.ide.editor.EditorSimple;
import com.ide.proyectos.VistaDirectorios;
import javafx.concurrent.Task;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.logging.Level.SEVERE;

public class BarraMenu extends Pane {
    private final Menu menuArchivo = new Menu("Archivo");
    private final Menu menuEdicion = new Menu("Edición");
    private final Menu menuVer = new Menu("Ver");
    private final Menu menuAyuda = new Menu("Ayuda");

    private final MenuItem menuItemAbrirArchivo = new MenuItem("Abrir archivo");
    private final MenuItem menuItemAbrirCarpeta = new MenuItem("Abrir carpeta");
    private final MenuItem menuItemGuardar = new MenuItem("Guardar");
    private final MenuItem menuItemGuardarComo = new MenuItem("Guardar como...");
    private final MenuItem menuItemSalir = new MenuItem("Salir");

    private final MenuBar barraMenu = new MenuBar();

    public BarraMenu() {

        barraMenu.getMenus().addAll(menuArchivo, menuEdicion, menuVer, menuAyuda);

        menuArchivo.getItems().addAll(menuItemAbrirArchivo, menuItemAbrirCarpeta, menuItemGuardar, menuItemGuardarComo, menuItemSalir);
    }

    public Menu getMenuArchivo() {
        return menuArchivo;
    }

    public Menu getMenuEdicion() {
        return menuEdicion;
    }

    public Menu getMenuVer() {
        return menuVer;
    }

    public Menu getMenuAyuda() {
        return menuAyuda;
    }

    public MenuItem getMenuItemAbrirArchivo() {
        return menuItemAbrirArchivo;
    }

    public MenuItem getMenuItemAbrirCarpeta() {
        return menuItemAbrirCarpeta;
    }

    public MenuItem getMenuItemGuardar() {
        return menuItemGuardar;
    }

    public MenuItem getMenuItemGuardarComo() {
        return menuItemGuardarComo;
    }

    public MenuItem getMenuItemSalir() {
        return menuItemSalir;
    }

    public MenuBar getBarraMenu() {
        return barraMenu;
    }
}
