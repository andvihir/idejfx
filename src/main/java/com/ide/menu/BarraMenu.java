package com.ide.menu;

import com.ide.Ide;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class BarraMenu extends MenuBar {
    private final Menu menuArchivo = new Menu("Archivo");
    private final Menu menuEdicion = new Menu("Edición");
    private final Menu menuVer = new Menu("Ver");
    private final Menu menuAyuda = new Menu("Ayuda");

    private final MenuItem menuItemAbrirArchivo = new MenuItem("Abrir archivo");
    private final MenuItem menuItemNuevoProyecto = new MenuItem("Nuevo proyecto...");
    private final MenuItem menuItemAbrirProyecto = new MenuItem("Abrir proyecto...");
    private final MenuItem menuItemGuardar = new MenuItem("Guardar");
    private final MenuItem menuItemGuardarComo = new MenuItem("Guardar como...");
    private final MenuItem menuItemCerrarProyecto = new MenuItem("Cerrar Proyecto");
    private final MenuItem menuItemSalir = new MenuItem("Salir");

    private final MenuItem menuItemDeshacer = new MenuItem("Deshacer");
    private final MenuItem menuItemCortar = new MenuItem("Cortar");
    private final MenuItem menuItemCopiar = new MenuItem("Copiar");
    private final MenuItem menuItemPegar = new MenuItem("Pegar");
    private final MenuItem menuItemEliminar = new MenuItem("Eliminar");
    private final MenuItem menuItemBuscar = new MenuItem("Buscar...");
    private final MenuItem menuItemSeleccionarTodo = new MenuItem("Seleccionar Todo");

    private final SeparatorMenuItem separator = new SeparatorMenuItem();

/*
    public BarraMenu() {

        menuArchivo.getItems().addAll(menuItemAbrirArchivo, menuItemAbrirCarpeta, menuItemGuardar, menuItemGuardarComo, menuItemSalir);
        menuEdicion.getItems().addAll(menuItemDeshacer, menuItemCortar, menuItemCopiar,menuItemPegar, menuItemEliminar, menuItemBuscar, menuItemSeleccionarTodo);


        this.getMenus().addAll(menuArchivo, menuEdicion, menuVer, menuAyuda);


    }
*/

    public BarraMenu(Ide ide) {
        menuArchivo.getItems().addAll(menuItemAbrirArchivo, menuItemNuevoProyecto, menuItemAbrirProyecto, menuItemGuardar, menuItemGuardarComo, new SeparatorMenuItem(),
                menuItemCerrarProyecto, new SeparatorMenuItem(),
                menuItemSalir);
        menuEdicion.getItems().addAll(menuItemDeshacer, new SeparatorMenuItem(),
                menuItemCortar, menuItemCopiar,menuItemPegar, menuItemEliminar, new SeparatorMenuItem(),
                menuItemBuscar, new SeparatorMenuItem(),
                menuItemSeleccionarTodo);

        //PROPIEDADES INICIALES
        menuItemGuardar.setDisable(true);
        menuItemAbrirArchivo.setVisible(false);
        menuItemGuardarComo.setDisable(true);
        menuItemCerrarProyecto.setDisable(true);
        menuEdicion.setDisable(true);



        this.getMenus().addAll(menuArchivo, menuEdicion, menuVer, menuAyuda);

        ide.hayProyectoAbierto().addListener( (obs, oldVal, newVal) -> {
            //menuEdicion.setDisable(!newVal);
            menuItemSalir.setDisable(!newVal);
            menuItemAbrirArchivo.setVisible(newVal);
            menuItemCerrarProyecto.setDisable(!newVal);
            menuEdicion.setDisable(!newVal);
        });

        ide.hayPestanaAbierta().addListener( (obs, oldVal, newVal) ->{
            menuEdicion.setDisable(!newVal);
        });

       // ControladoresMenu controladoresMenu = new ControladoresMenu(ide);


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

    public MenuItem getMenuItemAbrirProyecto() {
        return menuItemAbrirProyecto;
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

    public MenuItem getMenuItemCortar() {
        return menuItemCortar;
    }

    public MenuItem getMenuItemDeshacer() {
        return menuItemDeshacer;
    }

    public MenuItem getMenuItemCopiar() {
        return menuItemCopiar;
    }

    public MenuItem getMenuItemPegar() {
        return menuItemPegar;
    }

    public MenuItem getMenuItemEliminar() {
        return menuItemEliminar;
    }

    public MenuItem getMenuItemBuscar() {
        return menuItemBuscar;
    }

    public MenuItem getMenuItemSeleccionarTodo() {
        return menuItemSeleccionarTodo;
    }

    public MenuItem getMenuItemNuevoProyecto() {
        return menuItemNuevoProyecto;
    }

    public MenuItem getMenuItemCerrarProyecto() {
        return menuItemCerrarProyecto;
    }

    public MenuBar getBarraMenu() {
        return this;
    }
/*

    private class ControladoresMenu {
        private Ide ide;
        private ControladoresMenu(Ide ide){
        this.ide = ide;

        //----- EVENTOS BARRA DE MENU -----
        getMenuItemSalir().setOnAction(e -> System.exit(0));

        getMenuItemAbrirArchivo().setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt", "*.java")
            );
            //set initial directory somewhere user will recognise
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            //let user select file
            File archivo = fileChooser.showOpenDialog(null);
            //if file has been chosen, load it using asynchronous method (define later)
            if (archivo != null) {
                try {
                    cargarAchivoAEditor(archivo, getExtensionArchivo(archivo.getName()).get().equals("java"));
                    System.out.println("Extension:" + getExtensionArchivo(archivo.getName()).get());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        getMenuItemAbrirCarpeta().setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Abrir Carpeta");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File archivo = directoryChooser.showDialog(null);
            if (archivo != null) {
                try{
                    // VistaDirectorios vistaDirectorios = new VistaDirectorios(archivo, archivo.getName());
                    this.ide.setTreeDirectorios(new TreeDirectorios(archivo, archivo.getName()));

                    this.ide.getBarraDirectorios().setContent(this.ide.getTreeDirectorios());
                    this.ide.getBarraDirectorios().setContextMenu(new MenuContextualDirectorios());
                    //this.getPanelDirectorios().autosize();

                    // scrollPane.maxHeight()
                    //setLeft(scrollPane);
                }catch(Exception ignored){
                    System.out.println(ignored);
                }
            }


        });

        getMenuItemGuardar().setOnAction(e -> {
            if (this.ide.getArchivoReferencia() != null) {
                try {
                    guardarArchivo(this.ide.getArchivoReferencia());
                } catch(IOException ex){
                    Logger.getLogger(getClass().getName()).log(SEVERE, null, ex);
                }
            } else{
                FileChooser fileChooser = new FileChooser();
                //only allow text files to be selected using chooser
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Documentos de texto (*.txt)", "*.txt", "Java (*.java)")
                );
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

                File archivoGuardar = fileChooser.showSaveDialog(null);
                if (archivoGuardar != null) {
                    try {
                        guardarArchivo(archivoGuardar);
                        this.ide.setArchivoReferencia(archivoGuardar);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        getMenuItemGuardarComo().setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            //only allow text files to be selected using chooser
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Documentos de texto (*.txt)", "*.txt", "Java (*.java)")
            );
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File archivoGuardar = fileChooser.showSaveDialog(null);
            if (archivoGuardar != null) {
                try {
                    guardarArchivo(archivoGuardar);
                    this.ide.setArchivoReferencia(archivoGuardar);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //----- ACELERADORES-----
        getMenuItemAbrirArchivo().setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        getMenuItemGuardar().setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        getMenuItemSalir().setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

    }
    private void cargarAchivoAEditor(File archivo, boolean subrayadoJava) throws IOException {
        BufferedReader texto = new BufferedReader(new FileReader(archivo));
        long lineasTotal;
        try (Stream<String> stream = Files.lines(archivo.toPath())) {
            lineasTotal = stream.count();
        }
        String linea;
        StringBuilder archivoTotal = new StringBuilder();
        long lineasCargadas = 0;
        while( (linea = texto.readLine()) != null) {
            archivoTotal.append(linea);
            archivoTotal.append("\n");
            lineasCargadas++;
        }
        this.ide.setArchivoReferencia(archivo);
        this.ide.getEditor().replaceText(archivoTotal.toString());
    }

    private void guardarArchivo(File archivoReferencia) throws IOException {

        FileWriter myWriter = new FileWriter(archivoReferencia);
        myWriter.write(this.ide.getEditor().getText());
        myWriter.close();
        // lastModifiedTime = FileTime.fromMillis(System.currentTimeMillis() + 3000);
        System.out.println("Guardado con éxito.");
    }
        private Optional<String> getExtensionArchivo(String filename) {
            return Optional.ofNullable(filename)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        }
    }
    */
}
