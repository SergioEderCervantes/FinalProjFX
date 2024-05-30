/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.Modules.MySqlConn;
import org.Modules.Pair;
import org.Modules.Song;
import org.Modules.Tecla;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;



public class editorController {
    //TODO quitar warnings y en general hacer muchas validaciones
   private Song nuevaCancion;
   private MediaPlayer mediaPlayer;
   private boolean enReproduccion = false;
   private Timeline animacionReproduccion;
   private Pair[] map;
   private int idSong = 100;
   //Variables FXML
    @FXML
    private TextField songName;
    @FXML
    private TextField songPath;
    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;
    @FXML
    private Button AcceptPista;
    @FXML
    private VBox loadSong;
    @FXML
    private AnchorPane sideBar;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Slider sliderRep;
    @FXML
    private TextField timeDisplay;
    @FXML
    private ComboBox <String> comboBox;
    @FXML
    private ListView <Tecla> teclasExistentes;

    /**
     * Event-Handlers para la pre-edicion
     */
    @FXML
    private void openFileChooser(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Music Files", "*.mp3"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();


        File selectedFile = fileChooser.showOpenDialog(stage);
        if(validateFile(selectedFile)){
            songName.setFocusTraversable(false);
            songPath.setFocusTraversable(false);
            songName.setText(selectedFile.getName());
            songPath.setText(selectedFile.getPath());
            AcceptPista.setDisable(false);
        }
        else {
            errorLabel1.setVisible(true);
            errorLabel2.setVisible(true);
        }
    }

    /**
     * Hacemos llamar a la base de datos con una consulta sobre las canciones que existen para poder seleccionar una
     */
    @FXML
    private void openEditMenu(ActionEvent event){
        final String query = "SELECT idSong, name FROM song";
        MySqlConn conn = new MySqlConn();
        conn.consult(query);
        int n = 0;


        this.comboBox.setVisible(true);
        this.comboBox.setDisable(false);

        if (conn.rs != null){

            try {
                conn.rs.last();
                n = conn.rs.getRow();
                conn.rs.first();
            }catch (Exception e){
                e.printStackTrace();
            }
            String []resultados = new String[n];
            this.map = new Pair[n];
            for (int i = 0; i < n; i++) {
                try {
                    resultados[i] = (conn.rs.getString(2));
                    map[i] = new Pair<>(conn.rs.getInt(1),conn.rs.getString(2));
                    conn.rs.next();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            comboBox.getItems().setAll(resultados);
        }
        conn.closeRsStmt();

    }

    @FXML
    private void accetpFile(ActionEvent event){
        this.relocateLabels();
        this.splitPane.setDisable(false);
        //Configurar el slider de reproducccion
        configSlider();
        this.comboBox.setVisible(false);
        this.comboBox.setDisable(true);
        this.animacionReproduccion = new Timeline(new KeyFrame(Duration.millis(1000),event1 -> actualizarSlider(0)));
        animacionReproduccion.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Cuando se selecciona una cancion ya existente, se carga desde la base de datos
     */
    @FXML
    private void editarCancion(ActionEvent event){
        //TODO configurar el media player para poner la cancion, asi como descargar las teclas de la BD, hechale ganitas
        for (int i = 0; i < map.length; i++) {
            if (map[i].getSecond().equals(comboBox.getValue())){
                this.idSong = i;
            }
        }
        //Hacer que si songId == 100 salte error
        this.idSong++;
        final String query = "SELECT * FROM song WHERE idSong = " + this.idSong;
        MySqlConn conn = new MySqlConn();
        conn.consult(query);
        String name = "";
        String path = "";
        if (conn.rs != null){
            try {
                conn.rs.first();
                name = conn.rs.getString(2);
                path = conn.rs.getString(3);
            }catch (SQLException e){}
        }
        this.songName.setText(name);
        this.songPath.setText(path);
        conn.closeRsStmt();


        try{
            String mediaURL = new File(path).toURI().toString();
            Media media = new Media(mediaURL);
            this.mediaPlayer = new MediaPlayer(media);
        }catch (Exception e){
            e.printStackTrace();
        }
        AcceptPista.setDisable(false);

        this.cargarTeclas(conn);
    }


    /**
     * Events Handlers para la edicion
     */
    @FXML
    private void handleButtonEvent(ActionEvent event){
        String btnPressed = ((Button)event.getSource()).getText();
        Duration actual;
        Duration nuevo;
        switch (btnPressed){
            case "⏯":
                if (enReproduccion){
                    mediaPlayer.pause();
                    enReproduccion = false;
                    animacionReproduccion.pause();
                }
                else {
                    mediaPlayer.play();
                    enReproduccion = true;
                    animacionReproduccion.play();
                }
                break;
            case "↺":
                mediaPlayer.stop();
                mediaPlayer.play();
                enReproduccion = true;
                animacionReproduccion.play();
                actualizarSlider(0.1);
                break;
            case "⏵":
                actual = mediaPlayer.getCurrentTime();
                nuevo = actual.add(Duration.millis(333));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏩":
                actual = mediaPlayer.getCurrentTime();
                nuevo = actual.add(Duration.millis(1000));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏭":
                actual = mediaPlayer.getCurrentTime();
                nuevo = actual.add(Duration.seconds(5));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏴":
                actual = mediaPlayer.getCurrentTime();
                nuevo = actual.subtract(Duration.millis(333));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏪":
                actual = mediaPlayer.getCurrentTime();
                nuevo = actual.subtract(Duration.millis(1000));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏮" :
                actual = mediaPlayer.getCurrentTime();
                nuevo = actual.subtract(Duration.seconds(5));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
        }
    }
    @FXML
    private void agregarTecla(ActionEvent event){
        int numColor;
        int xSelected = (int)((Button) event.getSource()).getLayoutX();
        switch (xSelected){
            case 240:
                numColor = 0;
                break;
            case 340:
                numColor = 1;
                break;
            case 440:
                numColor = 2;
                break;
            case 540:
                numColor = 3;
                break;
            case 640:
                numColor = 4;
                break;

        }
    }

    @FXML
    private void sliderDragged(MouseEvent event){
        final Duration valor = Duration.millis(sliderRep.getValue());
        mediaPlayer.seek(valor);

    }

    /**
     * Misc
     */
    private void configSlider(){
        this.sliderRep.setMin(0);
        Duration dur = mediaPlayer.getTotalDuration();
        this.sliderRep.setMax(dur.toMillis());
        this.sliderRep.setShowTickLabels(true);
        this.sliderRep.setMajorTickUnit(60000);
        this.sliderRep.setMinorTickCount(0);
        this.sliderRep.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double timeMillis) {
                long totalSec = Math.round(timeMillis) / 1000;
                long millis = Math.round(timeMillis) % 1000;
                long minutes = totalSec / 60;
                long seconds = totalSec % 60;
                return String.format("%d:%02d:%02d", minutes, seconds,millis);
            }

            @Override
            public Double fromString(String s) {
                String[] parts = s.split(":");
                if (parts.length == 3) {
                    try {
                        int minutes = Integer.parseInt(parts[0]);
                        int seconds = Integer.parseInt(parts[1]);
                        int milis = Integer.parseInt(parts[2]);
                        return (double) (((minutes * 60 + seconds) * 1000) + milis);
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                } else {
                    return 0.0;
                }
            }
        });
        this.timeDisplay.setText("00:00:00");
    }

    /**
     * Funcion que valida el archivo y crea la instancia MediaPlayer de la pista
     * @param file
     * El archivo escogido en el fileChooser
     */
    private boolean validateFile(File file){
        if (file == null || !file.getName().endsWith(".mp3") || !file.exists() || !file.canRead()) {
            return false;
        }
        try {
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void relocateLabels(){
        loadSong.getChildren().remove(songName);
        loadSong.getChildren().remove(songPath);
        loadSong.setVisible(false);
        loadSong.setDisable(true);
        sideBar.getChildren().add(songName);
        sideBar.getChildren().add(songPath);
        songName.setLayoutX(55);
        songName.setLayoutY(48);
        songPath.setLayoutX(55);
        songPath.setLayoutY(83);
    }

    /**
     * Actualiza la posicion del slider de reproduccion, asi como formatea el tiempo para actualizar el label del
     * current time
     * @param newTime el tiempo nuevo que va a ser formateado, si se manda un 0 significa que es el tiempo actual mas
     * 1 segundo
     */
    private void actualizarSlider(double newTime){
        if (newTime == 0)
        {
            newTime = sliderRep.getValue() + 1000;
        }
        final double finalNewTime = newTime;
        javafx.application.Platform.runLater(() -> sliderRep.setValue(finalNewTime));

        long totalSec = Math.round(newTime) / 1000;
        long millis = Math.round(newTime) % 1000;
        long minutes = totalSec / 60;
        long seconds = totalSec % 60;
        String timepo = String.format("%d:%02d:%02d", minutes, seconds,millis);
        this.timeDisplay.setText(timepo);
    }

    private void cargarTeclas(MySqlConn conn){
        final String query = "SELECT numColor,tiempoInicio FROM teclas WHERE idSong = " + idSong;
        conn.consult(query);

        int n = 0;
        if (conn.rs != null){
            try {
                conn.rs.last();
                n = conn.rs.getRow();
                conn.rs.first();
            }catch (SQLException e){}
            ArrayList<Tecla> aux = new ArrayList<>();
            for (int i = 0; i < n; i++){
                try {
                    aux.add(new Tecla(i + 1,conn.rs.getDouble(2),conn.rs.getInt(1)));
                    conn.rs.next();
                } catch (SQLException e){}
            }
            this.configListView(aux);

        }
    }
    private void configListView(ArrayList<Tecla> aux){
        ObservableList <Tecla> observableList= FXCollections.observableArrayList(aux);

        this.teclasExistentes.setItems(observableList);

        this.teclasExistentes.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Tecla tecla, boolean empty) {
                super.updateItem(tecla, empty);
                if (tecla == null || empty) {
                    setText(null);
                }else {
                    String text = tecla.getTecla_ID().toString() + " - " ;
                    switch (tecla.getNumColor()){
                        case 0:
                            text += "Rojo";
                            break;
                        case 1:
                            text += "Azul";
                            break;
                        case 2:
                            text += "Amarillo";
                            break;
                        case 3:
                            text += "Verde";
                            break;
                        case 4:
                            text += "Naranja";
                            break;
                    }
                    text += " - " + tecla.getTiempoInicio();
                    setText(text);
                }
            }
        });
    }
}
