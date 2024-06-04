/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;




public class editorController {
    //TODO quitar warnings y en general hacer muchas validaciones
    private MediaPlayer mediaPlayer;
    private boolean enReproduccion = false;
    private Timeline animacionReproduccion;
    private Pair[] map;
    private int idSong = 100;
    private boolean editado = false;
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
    private ListView <String> teclasExistentes;
    @FXML
    private AnchorPane principal;
    @FXML
    private Button qBtn;
    @FXML
    private Button wBtn;
    @FXML
    private Button eBtn;
    @FXML
    private Button oBtn;
    @FXML
    private Button pBtn;

    /**
     * Event-Handlers para la pre-edicion
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
        this.initKeyboard();
    }

    /**
     * Cuando se selecciona una cancion ya existente, se carga desde la base de datos
     */
    @FXML
    private void editarCancion(ActionEvent event){
        for (int i = 0; i < map.length; i++) {
            if (map[i].getSecond().equals(comboBox.getValue())){
                this.idSong = i;
            }
        }
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
        this.editado = true;
    }


    /**
     * Events Handlers para la edicion
     */
    @FXML
    private void handleButtonEvent(ActionEvent event) {
        String btnPressed = ((Button) event.getSource()).getText();
        Duration actual;
        Duration nuevo = new Duration(0);


        actual = new Duration(redondearAlValorMasCercano(mediaPlayer.getCurrentTime().toMillis()));

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
                if (actual.toMillis() % 5 == 0){
                    nuevo = actual.add(Duration.millis(250));
                }
                else {
                    nuevo = actual.add(Duration.millis(333));
                }
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏩":
                nuevo = actual.add(Duration.millis(1000));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏭":
                nuevo = actual.add(Duration.millis(5000));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏴":
                if (actual.toMillis() % 5 == 0){
                    nuevo = actual.subtract(Duration.millis(250));
                }
                else {
                    nuevo = actual.subtract(Duration.millis(333));
                }
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏪":
                nuevo = actual.subtract(Duration.millis(1000));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
            case "⏮" :
                nuevo = actual.subtract(Duration.millis(5000));
                mediaPlayer.seek(nuevo);
                actualizarSlider(nuevo.toMillis());
                break;
        }

    }
    @FXML
    private void agregarTecla(ActionEvent event){
        int numColor = 0;
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
        double time = mediaPlayer.getCurrentTime().toMillis();
        time = redondearAlValorMasCercano(time);

        String aux = switchColores(numColor) + "--" + time;

        //TODO Hacer que la tecla que se agregue en el orden que debe, para que quede ordenada
        this.teclasExistentes.getItems().add(aux);


    }

    @FXML
    private void sliderDragged(MouseEvent event){
        final Duration valor = Duration.millis(sliderRep.getValue());
        mediaPlayer.seek(valor);

    }

    @FXML
    private void regresarFocus(){
        principal.requestFocus();
    }

    @FXML
    private void probarSong(){


    }

    /**
     * Metodo que guarda la nueva cancion en la base de datos
     */
    @FXML
    private void saveSong(){
        MySqlConn conn = new MySqlConn();
        System.out.println(this.teclasExistentes.getItems().size());
        long time = System.currentTimeMillis();
        if (!editado){
            //Cargar la cancion
            this.idSong = loadSong2DB(conn,this.songName.getText(),changePath(this.songName.getText(),
                            this.songPath.getText()),this.mediaPlayer.getTotalDuration().toMillis());
            if (idSong == 0){
                System.out.println("No se puede agregar el song");
            }
            else if  (loadTeclas2DB(this.teclasExistentes.getItems(),conn,this.idSong)){
                    System.out.println("SUCCESS");
                    System.out.println("Dt= " + (System.currentTimeMillis() - time));
                }

        }else {
            //borrar todas las teclas de la cancion que existia en la base de datos y despues volver a cargarla
        }
        conn.closeRsStmt();
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
        Platform.runLater(() -> sliderRep.setValue(finalNewTime));

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
            ArrayList<String> aux = new ArrayList<>();
            for (int i = 0; i < n; i++){
                try {
                    StringBuilder builder = new StringBuilder();
                    builder.append(switchColores(conn.rs.getInt(1)));
                    builder.append("--");
                    builder.append(conn.rs.getString(2));
                    aux.add(builder.toString());
                    conn.rs.next();
                } catch (SQLException e){}
            }

            ObservableList <String> observableList= FXCollections.observableArrayList(aux);

            this.teclasExistentes.setItems(observableList);

        }
    }
    private void initKeyboard(){
        this.principal.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode code = event.getCode();
            switch (code){
                case Q:
                    this.qBtn.setFocusTraversable(true);
                    this.qBtn.fire();
                    break;
                case W:
                    this.wBtn.setFocusTraversable(true);
                    this.wBtn.fire();
                    break;
                case E:
                    this.eBtn.setFocusTraversable(true);
                    this.eBtn.fire();
                    break;
                case O:
                    this.oBtn.setFocusTraversable(true);
                    this.oBtn.fire();
                    break;
                case P:
                    this.pBtn.setFocusTraversable(true);
                    this.pBtn.fire();
                    break;
            }
        });
    }

    /** Metodo que sera llamado cuando se quiera probar la cancion que se esta editando en estos momentos,
     *  construye el objeto Song nuevaCancion con sus teclas pulsadas para usarlas en el juego
     * @return el objeto Song que fue creado
     */
    private Song construirSong(){
        Song nuevaCancion;
        if (!editado){

            nuevaCancion = new Song(this.songName.getText(),changePath(this.songName.getText(),
                    this.songPath.getText()), this.mediaPlayer.getTotalDuration().toMillis());
        }
        else {
            nuevaCancion = new Song(this.songName.getText(),this.songPath.getText(),
                    this.mediaPlayer.getTotalDuration().toMillis());
        }

        nuevaCancion.ConstruirGrafo(getTeclasPulsadas(this.teclasExistentes.getItems()));

        return nuevaCancion;
    }


    //Funciones estaticas

    private static String switchColores(int numColor){
        switch (numColor){
            case 0: return "Rojo";
            case 1: return "Azul";
            case 2: return "Amarillo";
            case 3: return "Verde";
            case 4: return "Naranja";
        }
        return "NONE";
    }

    private static double redondearAlValorMasCercano(double valor) {
        double a = Math.floor(valor / 1000);
        double b = valor % 1000;
        double[] valoresObjetivos = {0, 250, 333, 500, 666,750, 1000};
        double valorMasCercano = valoresObjetivos[0];
        double menorDiferencia = Math.abs(b - valoresObjetivos[0]);

        for (int i = 1; i < valoresObjetivos.length; i++) {
            double diferencia = Math.abs(b - valoresObjetivos[i]);
            if (diferencia < menorDiferencia) {
                menorDiferencia = diferencia;
                valorMasCercano = valoresObjetivos[i];
            }
        }

        return (a * 1000 + valorMasCercano);
    }

    private static ArrayList<Tecla> getTeclasPulsadas(ObservableList<String> items){
        ArrayList<Tecla> teclas = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            String[] partes =  items.get(i).split("--");
            int numColor = 0;
            switch (partes[0]){
                case "Rojo": numColor = 0; break;
                case "Azul": numColor = 1; break;
                case "Amarillo": numColor = 2; break;
                case "Verde": numColor = 3; break;
                case "Naranja": numColor = 4; break;
            }
            try {
                teclas.add(new Tecla(i + 1,Double.parseDouble(partes[1]),numColor));
            }catch (NumberFormatException e){
                System.out.println("Fallo al cargar las teclas de la cancion");
            }

        }
        return teclas;
    }

    /**
     * Metodo que dada la referencia de un archivo, crea una copia del mismo en los archivos locales del proyecto
     * @param name  nombre del archivo sin ruta
     * @param path ruta absoluta donde se encuentra actualmente el archivo
     * @return  String de la ruta relativa al proyecto despues de hacer la copia
     */
    private static String changePath(String name,String path){
        final String target = "src/main/resources/songs" + File.separator + name;
        Path rutaAntigua = Paths.get(path);
        Path rutaNueva = Paths.get(target);
        try {
            Files.copy(rutaAntigua,rutaNueva);
        }catch (IOException e){
            e.printStackTrace();
        }
        return target;
    }

    private static int loadSong2DB(MySqlConn conn,String name, String path, double duracion){
        final String query = "INSERT INTO song (name, sourcePath, duracion) VALUES(\"" + name + "\",\"" + path +
                "\"," + duracion + ");";
        if (conn.uptade(query) > 0) {
            final String query2 = "SELECT idSong FROM song WHERE name='" + name + "' AND sourcePath='" + path + "';";
            conn.consult(query2);
            if (conn.rs != null){
                try {
                    conn.rs.last();
                    if (conn.rs.getRow() != 1) throw new Exception("Excepcion de logica de base de datos:  mas hayde un registro" +
                            "con el mismo nombre o no se encontro el registro");
                    conn.rs.first();
                    return conn.rs.getInt(1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }



    private static boolean loadTeclas2DB(ObservableList<String> list,MySqlConn conn, int idSong) {
        StringBuilder query = new StringBuilder("INSERT INTO teclas (idSong, numColor,tiempoInicio) VALUES ");
        for (int i = 0; i < list.size(); i++) {
            String[] partes = list.get(i).split("--");
            int numColor = 0;
            switch (partes[0]){
                case "Rojo": break;
                case "Azul": numColor = 1; break;
                case "Amarillo": numColor = 2; break;
                case "Verde": numColor = 3; break;
                case "Naranja": numColor = 4; break;
            }
            double tiempoInicio = Double.parseDouble(partes[1]);
            query.append("(").append(idSong).append(",").append(numColor).append(",").append(tiempoInicio).append(")");
            if (i != list.size() - 1) {
                query.append(",");
            }
        }
        query.append(";");
        return conn.uptade(query.toString()) > 0;
    }

    private static boolean del2DB(MySqlConn conn, int idSong) {
        final String query = "DELETE FROM teclas WHERE idSong = " + idSong;
        return conn.uptade(query) > 0;
    }

}
