package org.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;


    @Override
    public void start(Stage stage) throws IOException {

        Pane root = loadFXML("primary");
        scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        PrimaryController controller = new PrimaryController(root);
        stage.show();
        controller.setT_inicio(System.currentTimeMillis());
        controller.timeline.play();



//        Circle circle = new Circle(100,100,10, Color.BLUE);
//        root.getChildren().add(circle);
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.millis(50), event -> {
//                    // Aquí puedes cambiar las coordenadas del círculo
//                    circle.setCenterX(circle.getCenterX() + 10);
//                    circle.setCenterY(circle.getCenterY() + 10);
//                })
//        );
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();



    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Pane loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource( "primary.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}