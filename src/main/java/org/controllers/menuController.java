/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static org.controllers.App.loadFXML;

public class menuController {
    private Stage stage;
    private Scene scene;

    @FXML
    private Rectangle bar1;
    @FXML
    private Rectangle bar2;
    @FXML
    private Rectangle bar3;
    @FXML
    private Rectangle bar4;
    @FXML
    private Rectangle bar5;
    @FXML
    private Rectangle bar6;
    @FXML
    private Rectangle bar7;
    @FXML
    private Rectangle bar8;
    @FXML
    private Rectangle bar9;
    @FXML
    private Rectangle bar10;
    @FXML
    private Rectangle bar11;

    public void initialize() {
        // Iniciar la animación de las barras
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> updateBars()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateBars() {
        // Simular la animación de barras

        bar1.setY((Math.random() * 150)+170);
        bar1.setY(bar1.getY()+75);
        bar1.setY(bar1.getY()-100);
        bar2.setY((Math.random() * 150)+170);
        bar2.setY(bar2.getY()+80);
        bar2.setY(bar2.getY()-90);
        bar3.setY((Math.random() * 150)+170);
        bar3.setY(bar3.getY()+75);
        bar3.setY(bar3.getY()-100);
        bar4.setY((Math.random() * 150)+170);
        bar4.setY(bar4.getY()+80);
        bar4.setY(bar4.getY()-90);
        bar5.setY((Math.random() * 150)+170);
        bar5.setY(bar5.getY()+75);
        bar5.setY(bar5.getY()-100);
        bar6.setY((Math.random() * 150)+170);
        bar6.setY(bar6.getY()+80);
        bar6.setY(bar6.getY()-90);
        bar7.setY((Math.random() * 150)+170);
        bar7.setY(bar7.getY()+75);
        bar7.setY(bar7.getY()-100);
        bar8.setY((Math.random() * 150)+170);
        bar8.setY(bar8.getY()+80);
        bar8.setY(bar8.getY()-90);
        bar9.setY((Math.random() * 150)+170);
        bar9.setY(bar9.getY()+75);
        bar9.setY(bar9.getY()-100);
        bar10.setY((Math.random() * 150)+170);
        bar10.setY(bar10.getY()+80);
        bar10.setY(bar10.getY()-90);
        bar11.setY((Math.random() * 150)+170);
        bar11.setY(bar11.getY()+75);
        bar11.setY(bar11.getY()-100);


    }

    public void switchToMenu(ActionEvent event) throws IOException {
        Pane root = loadFXML("Menu");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToJugador(ActionEvent event) throws IOException {
        Pane root = loadFXML("Jugador");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEditor(ActionEvent event) throws IOException {
        Pane root = loadFXML("Editor");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSalida(ActionEvent event) throws IOException {
        System.exit(0);
    }
}