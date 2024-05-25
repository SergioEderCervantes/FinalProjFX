/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.Modules.Song;

import java.io.File;

/**
 *
 * @author zzzup
 */
public class editorController {

   Song nuevaCancion;
   MediaPlayer mediaPlayer;

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
    private void openFileChooser(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Music Files", "*.mp3"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();


        File selectedFile = fileChooser.showOpenDialog(stage);
        //Todo hacer validaciones chidas para la cancion

        if(validateFile(selectedFile)){
            songName.setText(selectedFile.getName());
            songPath.setText(selectedFile.getPath());
            AcceptPista.setDisable(false);
        }
        else {
            errorLabel1.setVisible(true);
            errorLabel2.setVisible(true);
        }
    }

    @FXML
    private void accetpFile(ActionEvent event){
        loadSong.setVisible(false);

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


    
}
