/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.Modules.Sprite;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Paths;

import static org.controllers.App.loadFXML;

public class menuController {
    private Stage stage;
    private Scene scene;
    public MediaPlayer mediaPlayer;


    @FXML
    private Label title;

    @FXML
    private Rectangle bar1;
    @FXML
    private Rectangle bar2;
    @FXML
    private Rectangle bar3;
    @FXML
    private Rectangle bar4;
    @FXML
    ImageView memo;
    Sprite memor;
    @FXML
    private Rectangle bar8;
    @FXML
    private Rectangle bar9;
    @FXML
    private Rectangle bar10;
    @FXML
    private Rectangle bar11;

    public void initialize() {
        music();
        // Aplicar estilo CSS al título
        title.getStyleClass().add("title");
        Image img = new Image(Paths.get("src/main/resources/images/memo.png").toUri().toString());
        memo.setImage(img);
        this.memor = new Sprite(memo,2,2,(int)69.5,36,1200);
        this.memor.setCycleCount(Transition.INDEFINITE);
        this.memor.resetAnimation();
        this.memor.setAutoReverse(true);
        memor.play();        // Iniciar la animación de las barras
        Timeline barTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> updateBars()));
        barTimeline.setCycleCount(Timeline.INDEFINITE);
        barTimeline.play();

        // Iniciar la animación rítmica del título
        Timeline titleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> title.setLayoutY(50)),
                new KeyFrame(Duration.seconds(0.5), e -> title.setLayoutY(55)),
                new KeyFrame(Duration.seconds(1), e -> title.setLayoutY(50))
        );
        titleTimeline.setCycleCount(Timeline.INDEFINITE);
        titleTimeline.play();
    }

    public void music(){
        String s = "src/main/resources/music/menu.mp3";
        Media h = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();

    }

    private void updateBars() {
        // Simular la animación de barras
        bar1.setY((Math.random() * 150) + 170);
        bar1.setY(bar1.getY() + 75);
        bar1.setY(bar1.getY() - 100);
        bar2.setY((Math.random() * 150) + 170);
        bar2.setY(bar2.getY() + 80);
        bar2.setY(bar2.getY() - 90);
        bar3.setY((Math.random() * 150) + 170);
        bar3.setY(bar3.getY() + 75);
        bar3.setY(bar3.getY() - 100);
        bar4.setY((Math.random() * 150) + 170);
        bar4.setY(bar4.getY() + 80);
        bar4.setY(bar4.getY() - 90);

        bar8.setY((Math.random() * 150) + 170);
        bar8.setY(bar8.getY() + 80);
        bar8.setY(bar8.getY() - 90);
        bar9.setY((Math.random() * 150) + 170);
        bar9.setY(bar9.getY() + 75);
        bar9.setY(bar9.getY() - 100);
        bar10.setY((Math.random() * 150) + 170);
        bar10.setY(bar10.getY() + 80);
        bar10.setY(bar10.getY() - 90);
        bar11.setY((Math.random() * 150) + 170);
        bar11.setY(bar11.getY() + 75);
        bar11.setY(bar11.getY() - 100);
    }

    public void switchToJugador(ActionEvent event) throws IOException {
        mediaPlayer.stop();
        Pane root = loadFXML("Jugador");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEditor(ActionEvent event) throws IOException {
        mediaPlayer.stop();
        Pane root = loadFXML("menuEditor");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSalida(ActionEvent event) throws IOException {
        mediaPlayer.stop();
        System.exit(0);
    }
}
