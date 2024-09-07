package org.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.Modules.Conexion_UDP;
import org.Modules.FileUtils;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EsperandoSeleccionController {
    private Conexion_UDP connector;

    @FXML
    AnchorPane principal;

    public void setConnector(Conexion_UDP connector) {
        this.connector = connector;
    }
    public void waiting() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            String songID = connector.getLastReceived();
            try {
                if (Integer.parseInt(songID) < 15) {
                    scheduler.shutdown();
                    Platform.runLater(() -> switchToJuegoOnline(songID));
                }
            } catch (NumberFormatException e) {}
        };

        // Programa la tarea para que se ejecute cada 20 milisegundos
        scheduler.scheduleAtFixedRate(task, 1000, 20, TimeUnit.MILLISECONDS);
    }
    private void switchToJuegoOnline(String id){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("juegoOnline.fxml"));
            Pane root = loader.load();
            JuegoOnlineController controller = loader.getController();
            controller.setCancionSeleccionada(FileUtils.loadSong(id));
            connector.sendData("Ready");
            while (!connector.getLastReceived().equals("Ready")){
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
