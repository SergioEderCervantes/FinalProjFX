package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.Modules.FileUtils;
import org.Modules.Pair;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static org.controllers.App.loadFXML;
import static org.controllers.editorController.switchColores;


public class MenuEditorController {

    private Pair<String,String>[] map;
    private boolean editado;
    MediaPlayer pistaSeleccion;
    ArrayList<String> teclasSeleccion;
    String cancionSeleccion;
    String idSongAEliminar;
    final Image closeImg = new Image(Paths.get("src/main/resources/images/cross.png").toUri().toString());

    @FXML
    private VBox loadPanel;
    @FXML
    private VBox editPanel;
    @FXML
    private VBox deletePanel;
    @FXML
    private Button AcceptPista;
    @FXML
    private Button AcceptPista1;
    @FXML
    private Button delButton;
    @FXML
    private ComboBox<String> comboBoxEdit;
    @FXML
    private ComboBox<String> comboBoxDel;
    @FXML
    private Label confirmationLabel;

    /**
     * Switch Back a Menu
     */
    @FXML
    private void back(ActionEvent event) {
        try{

            Pane root = loadFXML("Menu");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Metodo que despliega el panel de agregar nueva cancion
     */
    @FXML
    private void AgregarCancion() {
        this.editado = false;
        this.loadPanel.setVisible(true);
        this.loadPanel.setDisable(false);
        final ImageView closeImgVw = new ImageView(closeImg);

        HBox hBox = (HBox) this.loadPanel.getChildren().get(0);
        Button aux = (Button) hBox.getChildren().get(0);
        aux.setGraphic(closeImgVw);
    }

    /**
     * Metodo que despliega el panel de editar una cancion
     */
    @FXML
    private void editarCancion() {
        this.editado = true;
        this.editPanel.setVisible(true);
        this.editPanel.setDisable(false);
        this.initEditionPanel();
        final ImageView closeImgVw = new ImageView(closeImg);

        HBox hBox = (HBox) this.editPanel.getChildren().get(0);
        Button aux = (Button) hBox.getChildren().get(0);
        aux.setGraphic(closeImgVw);
    }

    /**
     * Metodo que despliega el panel de eliminar una cancion
     */
    @FXML
    private void EliminarCancion() {
        this.editado = false;
        this.deletePanel.setVisible(true);
        this.deletePanel.setDisable(false);
        this.initDeletionPanel();
        final ImageView closeImgVw = new ImageView(closeImg);

        HBox hBox = (HBox) this.deletePanel.getChildren().get(0);
        Button aux = (Button) hBox.getChildren().get(0);
        aux.setGraphic(closeImgVw);
    }

    /**
     * Manejo del fileChooser de agregar nueva cancion
     */
    @FXML
    private void openFileChooser(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Music Files", "*.mp3"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"),"Music"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if(validateFile(selectedFile)){
            Pane pane =(Pane) this.loadPanel.getChildren().get(1);
            TextField songName = new TextField();
            songName.setLayoutX(51);
            songName.setLayoutY(109);
            songName.setPrefWidth(106);
            songName.setPrefHeight(25);
            songName.setFocusTraversable(false);
            songName.setText(selectedFile.getName());
            songName.setEditable(false);
            pane.getChildren().add(songName);

            TextField songPath = new TextField();
            songPath.setLayoutX(227);
            songPath.setLayoutY(109);
            songPath.setPrefWidth(106);
            songPath.setPrefHeight(25);
            songPath.setFocusTraversable(false);
            songPath.setText(selectedFile.getPath());
            songPath.setEditable(false);
            pane.getChildren().add(songPath);

            this.cancionSeleccion = selectedFile.getName();
            AcceptPista.setDisable(false);


            String pathChida = changePath(selectedFile.getName(), selectedFile.getPath());
                File archivoCopiado = new File(pathChida);
            if (!archivoCopiado.exists()) {
                System.out.println("Error: El archivo copiado no existe.");
            } else {
                try {
                    System.out.println(pathChida);
                    System.out.println(archivoCopiado.toURI().toString());
                    // Convertir la ruta del archivo a un URI válido para el MediaPlayer
                    Media media = new Media(archivoCopiado.toURI().toString());
                    this.pistaSeleccion = new MediaPlayer(media);

                    // Eventos de error, listo y fin de la reproducción
                    pistaSeleccion.setOnError(() -> System.out.println("Error: " + pistaSeleccion.getError()));
                    pistaSeleccion.setOnReady(() -> System.out.println("MediaPlayer ready to play."));
                    pistaSeleccion.setOnEndOfMedia(() -> System.out.println("MediaPlayer finished playing."));

                    // Reproducir el archivo
                    pistaSeleccion.play();

                } catch (Exception e) {
                    System.out.println("Error al cargar el archivo de audio: " + e.getMessage());
                }
            }
            this.teclasSeleccion = new ArrayList<>();
        }
        else {
            //todo se crea una ventana de error bonito
        }
    }

    @FXML
    private void getSong() {
        String idSong = this.getIdSong();
        if (editado){
            try {
                this.cancionSeleccion = comboBoxEdit.getValue();
                this.pistaSeleccion = FileUtils.loadMP(idSong);
                this.teclasSeleccion = FileUtils.loadTL(idSong);
                if (pistaSeleccion == null || teclasSeleccion == null){
                    throw new Exception("devolvio un null:");
                }

            } catch (Exception e){
                System.err.println("Error: " + e.getMessage());
            }
            this.AcceptPista1.setDisable(false);
        }
        else {
            this.idSongAEliminar = idSong;
            this.confirmationLabel.setVisible(true);
            this.delButton.setDisable(false);
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Button source = (Button) event.getSource();
        HBox HParent = (HBox) source.getParent();
        VBox VParent = (VBox) HParent.getParent();
        VParent.setVisible(false);
        VParent.setDisable(true);
    }

    /**
     * Metodo que cambia la escena al editor, pasandole todos los valores necesarios para ello
     */
    @FXML
    private void accetpFile(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(App.class.getResource("Editor.fxml"));

        try {
            Pane root = loader.load();
            editorController controller = loader.getController();
            controller.setSongName(this.cancionSeleccion);
            controller.setMediaPlayer(this.pistaSeleccion);
            controller.setTeclas(this.teclasSeleccion);
            controller.setEditado(this.editado);
            controller.Postinitialize();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            System.out.println("IOException: " + e.getMessage());

        }
    }

    /**
     * Hace una llamada a la base de datos para eliminar la cancion que se selecciono, devuelve e imprime la cantidad de registros afectados
     */
    @FXML
    private void deleteSong(){
        FileUtils.deleteSong(this.idSongAEliminar);
    }

    /**
     * @return el idSong de la cancion que fue seleccionada ya sea en el comboBoxEdit o el comboBoxDel
     */
    private String getIdSong() {
        String idSong = "-1";

        if (editado){
            for (Pair<String, String> integerStringPair : map) {
                if (integerStringPair.getSecond().equals(comboBoxEdit.getValue())) {
                    idSong = integerStringPair.getFirst();

                }
            }
        }
        else {
            for (Pair<String, String> integerStringPair : map) {
                if (integerStringPair.getSecond().equals(comboBoxDel.getValue())) {
                    idSong = integerStringPair.getFirst();

                }
            }
        }
        if (Objects.equals(idSong, "-1")){
            //todo error bonito
        }
        return idSong;
    }


    /**
     * Configura el comboBoxEdit
     */
    private void initEditionPanel(){
        this.map = FileUtils.loadSongsNames();
        this.comboBoxEdit.getItems().clear();
        for (Pair<String, String> integerStringPair : map) {
            this.comboBoxEdit.getItems().add(integerStringPair.getSecond());
        }
    }

    /**
     * Configura el comboBoxDel
     */
    private void initDeletionPanel(){
        this.map = FileUtils.loadSongsNames();
        this.comboBoxDel.getItems().clear();
        for (Pair<String, String> integerStringPair : map) {
            this.comboBoxDel.getItems().add(integerStringPair.getSecond());
        }
    }



    //Static methods
    /**
     * Funcion que valida el archivo y crea la instancia MediaPlayer de la pista
     * @param file
     * El archivo escogido en el fileChooser
     */
    private static boolean validateFile(File file){
        if (file == null || !file.getName().endsWith(".mp3") || !file.exists() || !file.canRead()) {
            return false;
        }

        return true;
    }

    /**
     * Metodo que dada la referencia de un archivo, crea una copia del mismo en los archivos locales del proyecto
     * @param name  nombre del archivo sin ruta
     * @param path ruta absoluta donde se encuentra actualmente el archivo
     * @return  String de la ruta relativa al proyecto despues de hacer la copia
     */
    private static String changePath(String name, String path) {
        // Ruta donde se copiará el archivo
        final String target = "src/main/resources/music" + File.separator + name;

        // Usar Paths para manejar la copia del archivo
        Path rutaAntigua = Paths.get(path);
        Path rutaNueva = Paths.get(target);

        try {
            Files.copy(rutaAntigua, rutaNueva, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        return rutaNueva.toString();  // Devolver la ruta absoluta nueva
    }

}
