package org.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.Modules.Conexion_UDP;

import java.io.IOException;

public class EsperandoSeleccionController {
    private Conexion_UDP connector;

    @FXML
    AnchorPane principal;

    public void setConnector(Conexion_UDP connector) {
        this.connector = connector;
    }
    public void waiting(){
        boolean ready = false;
        int id = 10000;
        while(!ready){
            String songID = connector.getLastRecieved();
            try {
                id = Integer.parseInt(songID);
                if (id < 15){
                    ready = true;
                }
            } catch (NumberFormatException e) {}
        }
        switchToJuegoOnline(id);

    }
    private void switchToJuegoOnline(int id){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("juegoOnline.fxml"));
            Pane root = loader.load();
            JuegoOnlineController controller = loader.getController();
            controller.loadSongFromDB(id);
            connector.sendData("Ready");
            while (!connector.getLastRecieved().equals("Ready")){
                Thread.sleep(20);
            }
            controller.setConnector(connector);
            controller.postInitialize();
            Stage stage = (Stage) principal.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){}
        catch (InterruptedException e){}
    }

}
