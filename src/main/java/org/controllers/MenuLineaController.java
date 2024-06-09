package org.controllers;


import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.Modules.Conexion_UDP;
import org.Modules.ConnectionUtils;
import org.Modules.GAME_MODE;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static org.controllers.App.loadFXML;


public class MenuLineaController implements Initializable {
    private boolean creando;
    private Conexion_UDP conexionUdp;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean conexionLista;

    @FXML
    private AnchorPane principal;
    @FXML
    private Button exitBtn;
    @FXML
    private Pane crearPane;
    @FXML
    private Pane unirsePane;
    @FXML
    private Label ipServer;
    @FXML
    private Label ipPropio;
    @FXML
    private TextField ipCliente;
    @FXML
    private Label ipCliente1;
    @FXML
    private TextField ipServer1;
    @FXML
    private Label ipPropio1;
    @FXML
    private Button crearBtn;
    @FXML
    private Button unirseBtn;
    @FXML
    private Rectangle animationRectangle;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.creando = true;
        this.conexionLista = false;
        if (ConnectionUtils.devIp != null) {
            ipServer.setText(ConnectionUtils.devIp);
            ipPropio.setText(ConnectionUtils.devIp);
            ipPropio1.setText(ConnectionUtils.devIp);
            ipCliente1.setText(ConnectionUtils.devIp);
        } else {
            ipServer.setText("ERROR EN IP");
            ipPropio.setText("ERROR EN IP");
        }
        Image icon = new Image(Paths.get("src/main/resources/images/home.png").toUri().toString());
        Image iconHover = new Image(Paths.get("src/main/resources/images/homeHover.png").toUri().toString());
        ImageView imgVw = new ImageView(icon);
        imgVw.setFitHeight(28);
        imgVw.setFitWidth(28);
        exitBtn.setGraphic(imgVw);
        exitBtn.setOnMouseEntered(e -> imgVw.setImage(iconHover));
        exitBtn.setOnMouseExited(e -> imgVw.setImage(icon));
    }

    @FXML
    private void back(ActionEvent event) {
        try {

            Pane root = loadFXML("Multijugador");
            Stage stage = (Stage) crearPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void switchCrear() {
        if (!creando) {
            this.crearBtn.getStyleClass().remove("redText");
            this.crearBtn.getStyleClass().add("whiteText");
            this.unirseBtn.getStyleClass().remove("whiteText");
            this.unirseBtn.getStyleClass().add("redText");
            RectTransition(this.animationRectangle, 0);
            animateGradientToLeft(principal);
        }
        this.creando = true;
        this.conexionLista = false;
        this.unirsePane.setVisible(false);
        this.unirsePane.setDisable(true);
        this.crearPane.setVisible(true);
        this.crearPane.setDisable(false);

    }

    @FXML
    private void switchUnirse() {
        if (creando) {
            this.crearBtn.getStyleClass().remove("whiteText");
            this.crearBtn.getStyleClass().add("redText");
            this.unirseBtn.getStyleClass().remove("redText");
            this.unirseBtn.getStyleClass().add("whiteText");
            RectTransition(this.animationRectangle, 136);
            animateGradientToRight(principal);

        }
        this.creando = false;
        this.conexionLista = false;
        this.crearPane.setVisible(false);
        this.crearPane.setDisable(true);
        this.unirsePane.setVisible(true);
        this.unirsePane.setDisable(false);
    }

    @FXML
    private void testConection(ActionEvent event) {
        try {
            this.testConn();
        } catch (Exception e) {
            //Todo poner mensaje grafico de error
            System.out.println("ERROR EN CONEXION: " + e.getMessage());
        }
    }

    @FXML
    private void initGame(ActionEvent event) {
        if (conexionLista){
            if (creando){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Selector.fxml"));
                    Pane root = loader.load();
                    selectorController controller = loader.getController();
                    controller.setConnector(conexionUdp);
                    controller.setGameMode(GAME_MODE.ONLINE_MULTIPLAYER);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("esperandoSeleccion.fxml"));
                    Pane root = loader.load();
                    EsperandoSeleccionController controller = loader.getController();
                    controller.setConnector(conexionUdp);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {}
            }

        }else {
            Button btn = (Button) event.getSource();
            Label label = new Label("Prueba la conexion primero !!!");
            label.setStyle("-fx-text-fill: red");
            label.setLayoutX(btn.getLayoutX());
            label.setLayoutY(btn.getLayoutY() - 20);
            principal.getChildren().add(label);
        }

    }

    @FXML
    private void customIp(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Pane dialogPane = new Pane();
        dialogPane.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
        dialogPane.setPrefSize(200, 150);

        Label label = new Label("Enter something:");
        TextField textField = new TextField();
        Button confirmButton = new Button("Confirm");
        Button closeButton = new Button("X");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        label.setLayoutX(10);
        label.setLayoutY(10);
        textField.setLayoutX(10);
        textField.setLayoutY(40);
        confirmButton.setLayoutX(10);
        confirmButton.setLayoutY(80);
        closeButton.setLayoutX(dialogPane.getPrefWidth() - 25);
        closeButton.setLayoutY(5);

        confirmButton.setOnAction(e -> {
            String input = textField.getText();
            this.ipPropio.setText(input);
            this.ipServer.setText(input);
            this.ipCliente1.setText(input);
            this.ipPropio1.setText(input);
            if (creando)this.crearPane.getChildren().remove(dialogPane);
            else this.unirsePane.getChildren().remove(dialogPane);

        });
        closeButton.setOnAction(e -> {
            if (creando)this.crearPane.getChildren().remove(dialogPane);
            else this.unirsePane.getChildren().remove(dialogPane);
        });

        dialogPane.getChildren().addAll(label, textField, confirmButton, closeButton);
        dialogPane.setLayoutX((currentStage.getWidth() - dialogPane.getPrefWidth()) / 2);
        dialogPane.setLayoutY((currentStage.getHeight() - dialogPane.getPrefHeight()) / 2);

        addDragListeners(dialogPane);
        if (creando){
            this.crearPane.getChildren().add(dialogPane);

        }else {
            this.unirsePane.getChildren().add(dialogPane);
        }
    }

    @FXML
    private void hoverIn(MouseEvent event) {
        try {

            Button btn = (Button) event.getSource();
            btn.getStyleClass().remove("insideButton");
            btn.getStyleClass().add("customHover");
        } catch (Exception e) {
        }
    }

    @FXML
    private void hoverOut(MouseEvent event) {
        try {

            Button btn = (Button) event.getSource();
            btn.getStyleClass().remove("customHover");
            btn.getStyleClass().add("insideButton");
        } catch (Exception e) {
        }
    }


    private void addDragListeners(Pane dialogPane) {

        dialogPane.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX() - dialogPane.getLayoutX();
            yOffset = event.getSceneY() - dialogPane.getLayoutY();
        });

        dialogPane.setOnMouseDragged((MouseEvent event) -> {
            dialogPane.setLayoutX(event.getSceneX() - xOffset);
            dialogPane.setLayoutY(event.getSceneY() - yOffset);
        });
    }


    private void testConn() throws Exception {
        String response = "";
        boolean makeIt = false;
        long timeConnectionInit = System.currentTimeMillis();
        if (creando) {
            conexionUdp = new Conexion_UDP("ConnServThread", this.ipServer.getText(),
                    this.ipCliente.getText(), true);
            conexionUdp.resumeConnection();
            conexionUdp.sendData("Hello");
            while (!response.equals("Response") || (System.currentTimeMillis() - timeConnectionInit < 10000)) {
                response = conexionUdp.getLastRecieved();
                Thread.sleep(50);
            }
            if (response.equals("Response")) {
                makeIt = true;
                conexionUdp.sendData("Connection stablished");
            }


        } else {
            conexionUdp = new Conexion_UDP("ConnClientThread", this.ipCliente.getText(),
                    this.ipServer.getText(), false);
            while (!response.equals("Response") || (System.currentTimeMillis() - timeConnectionInit < 10000)) {
                response = conexionUdp.getLastRecieved();
                Thread.sleep(50);
            }
            if (response.equals("Response")) {
                makeIt = true;
            }
        }
        if (makeIt) {
            //Todo poner conexion exitosa
            this.conexionLista = true;
        } else {
            throw new Exception("Waiting timeout");
        }

    }

    private static void RectTransition(Rectangle rect, final int toX) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(rect);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToX(toX);
        transition.play();
    }

    private static void animateGradientToRight(AnchorPane root) {
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.2));
            }

            @Override
            protected void interpolate(double frac) {
                double percent1 = 100 - (100 * frac);
                double percent2 = 100 * frac;
                root.setStyle("-fx-background-color: linear-gradient(from " + percent1 + "% 0% to " + percent2 + "% 100%," +
                        " #2f2f2f, #2f2f2f 70%, #a140fd);");
            }
        };

        transition.play();


    }
    private static void animateGradientToLeft(AnchorPane root) {
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.seconds(0.2));
            }

            @Override
            protected void interpolate(double frac) {
                double percent1 = 100 * frac;
                double percent2 = 100 - (100 * frac);
                root.setStyle("-fx-background-color: linear-gradient(from " + percent1 + "% 0% to " + percent2 +
                        "% 100%, #2f2f2f, #2f2f2f 70%, #a140fd);");
            }
        };

        transition.play();
    }
}
