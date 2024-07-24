package com.ide.proyectos;

import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class VistaProyectos extends VBox {

    private final TreeView<Object> treeView = new TreeView<>();
    //private ObjectProperty<TreeView> treeView = new SimpleObjectProperty<>();
/*
    private void createTreeView(String dirPath) {
        TreeItem<Object> tree = new TreeItem<>(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1), new ImageView(icon));
        List<TreeItem<Object>> dirs = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dirPath));
            for (Path path : directoryStream) {
                if (Files.isDirectory(path)) {
                    String pathString = path.toString();
                    TreeItem<Object> subDirectory = new TreeItem<>(pathString.substring(pathString.lastIndexOf(File.separator) + 1), new ImageView(icon));
                    getSubLeafs(path, subDirectory);
                    dirs.add(subDirectory);
                }
            }

            tree.getChildren().addAll(dirs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            imageNumber = 1;
            StringBuilder pathBuilder = new StringBuilder();
            for (TreeItem<String> item = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem(); item != null; item = item.getParent()) {

                pathBuilder.insert(0, item.getValue());
                pathBuilder.insert(0, "/");
            }
            String path = pathBuilder.toString();
            populateImageView(path.substring(1) + "/");
        });

        tree.setExpanded(true);
        treeView.setRoot(tree);
        treeView.setShowRoot(true);
    }


    private void getSubLeafs(Path subPath, TreeItem<Object> parent) {
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(subPath.toString()));
            for (Path subDir : directoryStream) {
                if (Files.isDirectory(subDir)) {
                    String subTree = subDir.toString();
                    TreeItem<Object> subLeafs = new TreeItem<>(subTree, new ImageView(icon));
                    subLeafs.setValue(subTree.substring(subTree.lastIndexOf(File.separator) + 1));
                    getSubLeafs(subDir, subLeafs);
                    parent.getChildren().add(subLeafs);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
