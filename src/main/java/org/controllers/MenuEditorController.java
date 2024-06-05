package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.Modules.MySqlConn;
import org.Modules.Pair;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.controllers.App.loadFXML;
import static org.controllers.editorController.switchColores;


public class MenuEditorController {

    private Pair<Integer,String>[] map;
    private boolean editado;
    MediaPlayer pistaSeleccion;
    ArrayList<String> teclasSeleccion;
    int idSongAEliminar;

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

    /**
     * Switch Back a Menu
     * @param event
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
            e.printStackTrace();
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


        File selectedFile = fileChooser.showOpenDialog(stage);
        if(validateFile(selectedFile)){
            TextField songName = new TextField();
            songName.setLayoutX(51);
            songName.setLayoutY(134);
            songName.setFocusTraversable(false);
            songName.setText(selectedFile.getName());
            this.loadPanel.getChildren().add(songName);

            TextField songPath = new TextField();
            songPath.setLayoutX(226);
            songPath.setLayoutY(134);
            songPath.setFocusTraversable(false);
            songPath.setText(selectedFile.getPath());
            this.loadPanel.getChildren().add(songPath);
            AcceptPista.setDisable(false);
        }
        else {
            //todo se crea una ventana de error bonito
        }
    }

    //todo crear el manejo del boton AcceptPista bien
    @FXML
    private void accetpFile(ActionEvent event) {
        System.out.println("Aceptao");
    }
    @FXML
    private void deleteSong(){
        System.out.println("DELETEAO");
    }

    @FXML
    private void getSong() {
        int idSong = this.getIdSong();
        if (editado){
            try {
                this.pistaSeleccion = loadMP(idSong);
                this.teclasSeleccion = loadTl(idSong);
                if (pistaSeleccion == null || teclasSeleccion == null){
                    throw new Exception("devolvio un null:");
                }

            } catch (SQLException sqlException){
                System.err.println("Error SQL: " + sqlException.getMessage());
            } catch (Exception e){
                System.err.println("Error: " + e.getMessage());
            }
            this.AcceptPista1.setDisable(false);
        }
        else {
            this.idSongAEliminar = idSong;
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

    private int getIdSong() {
        int idSong = -1;

        if (editado){
            for (Pair<Integer, String> integerStringPair : map) {
                if (integerStringPair.getSecond().equals(comboBoxEdit.getValue())) {
                    idSong = integerStringPair.getFirst();
                    idSong++;
                }
            }
        }
        else {
            for (Pair<Integer, String> integerStringPair : map) {
                if (integerStringPair.getSecond().equals(comboBoxDel.getValue())) {
                    idSong = integerStringPair.getFirst();
                    idSong++;
                }
            }
        }
        if (idSong == -1){
            //todo error bonito
        }
        return idSong;
    }


    private void initEditionPanel(){
        this.map = cargarSongs();
        for (Pair<Integer, String> integerStringPair : map) {
            this.comboBoxEdit.getItems().add(integerStringPair.getSecond());
        }
    }

    private void initDeletionPanel(){
        this.map = cargarSongs();
        for (Pair<Integer, String> integerStringPair : map) {
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
        try {
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Metodo que hace una llamada a la base de datos para hacer una consulta de los nombres y id de las canciones existentes
     * @return un arreglo de Pair que contiene todos los id's de las canciones asi como sus nombres
     */
    private static Pair <Integer,String>[] cargarSongs(){
        final String query = "SELECT idSong, name FROM song";
        MySqlConn conn = new MySqlConn();
        conn.consult(query);
        Pair <Integer,String>[] map = null;

        int n = 0;
        if (conn.rs != null){

            try {
                conn.rs.last();
                n = conn.rs.getRow();
                conn.rs.first();
            }catch (Exception e){
                e.printStackTrace();
            }
            map = new Pair[n];

            for (int i = 0; i < n; i++) {
                try {
                    map[i] = new Pair<>(conn.rs.getInt(1),conn.rs.getString(2));
                    conn.rs.next();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
        conn.closeRsStmt();
        return map;
    }

    /**
     * Metodo que carga un MediaPlayer desde la base de datos con el id de la cancion
     * @param idSong id de la cancion seleccionada
     * @return MediaPlayer funcional para su uso
     */
    private static MediaPlayer loadMP(int idSong) throws SQLException{
        final String query = "SELECT sourcePath FROM song WHERE idSong = " + idSong;
        MySqlConn conn = new MySqlConn();
        conn.consult(query);

        String path = "";
        MediaPlayer target = null;
        if (conn.rs != null){
            conn.rs.first();
            path = conn.rs.getString(1);
            Media media = new Media(new File(path).toURI().toString());
            target = new MediaPlayer(media);

        }
        return target;
    }

    /**
     * Metodo para cargar todas las teclas relacionadas con el idsong que se pasa
     * @param idSong el id de la cancion seleccionada
     * @return ArrayList listo para usarse
     */
    private static ArrayList<String> loadTl(int idSong) throws SQLException {
        final String query = "SELECT numColor,tiempoInicio FROM teclas WHERE idSong = " + idSong;
        MySqlConn conn = new MySqlConn();
        conn.consult(query);

        int n = 0;
        if (conn.rs != null) {
            conn.rs.last();
            n = conn.rs.getRow();
            conn.rs.first();

            ArrayList<String> aux = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                String builder = switchColores(conn.rs.getInt(1)) +
                        "--" +
                        conn.rs.getString(2);
                aux.add(builder);
                conn.rs.next();
            }
            return aux;
        }else return null;
    }

}
