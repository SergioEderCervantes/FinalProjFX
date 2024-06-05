/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static org.controllers.App.loadFXML;

public class jugadorController {
    private Stage stage;
    private Scene scene;

    public void back(ActionEvent event) throws IOException {
        Pane root = loadFXML("Menu");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    } public void switchToJuego(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("juego.fxml"));
        Pane root = loader.load();
        juegoController controller = loader.getController();
        controller.loadSongFromDB(1);
        controller.postInitialize();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    } public void switchToMulti(ActionEvent event) throws IOException {
        Pane root = loadFXML("Multijugador");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

}
