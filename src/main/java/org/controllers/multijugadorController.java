package org.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.Modules.GAME_MODE;

import java.io.IOException;

import static org.controllers.App.loadFXML;

public class multijugadorController {
    private Stage stage;
    private GAME_MODE gm;
    private Scene scene;

    public void back(ActionEvent event) throws IOException {
        Pane root = loadFXML("Jugador");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    public void Local(ActionEvent event) throws IOException {
        this.gm = GAME_MODE.LOCAL_MULTIPLAYER;
        Pane root = loadFXML("Local");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    public void Linea(ActionEvent event) throws IOException {
        Pane root = loadFXML("MenuLinea");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
}
