/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.Modules.*;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static org.controllers.App.loadFXML;


public class editorController {
    //TODO quitar warnings y en general hacer muchas validaciones
    private MediaPlayer mediaPlayer;
    private boolean enReproduccion = false;
    private Timeline animacionReproduccion;
    private Pair[] map;
    private int idSong = 100;
    private boolean editado = false;
    String songPath;
    Sprite sprite;
    CustomRunnable<Button,Color> effect;

    //Variables FXML
    @FXML
    private TextField songName;
    @FXML
    private Slider sliderRep;
    @FXML
    private TextField timeDisplay;
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
    @FXML
    private Button exitBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private ImageView Animacion;
    private final HashMap<KeyCode, Double> PressedKeys = new HashMap<>();


    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.songPath = getRelativePath(mediaPlayer.getMedia());
    }

    public void setTeclas(ArrayList<String> teclas) {
        ObservableList <String> observableList= FXCollections.observableArrayList(teclas);
        this.teclasExistentes.setItems(observableList);
    }

    public void setSongName (String songName) {
        this.songName.setText(songName);
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
    }


    public void Postinitialize(){
        //Configurar el slider de reproducccion
        initButtons();
        configSlider();
        Image img = new Image(Paths.get("src/main/resources/images/barritas.png").toUri().toString());
        Animacion.setImage(img);
        this.effect = (Button btn,Color color) -> {
            Circle aux = new Circle();
            aux.setRadius(0);
            aux.setFill(color);
            aux.setCenterX(btn.getLayoutX() + btn.getWidth()/ 2);
            aux.setCenterY(btn.getLayoutY() + btn.getHeight() / 2);

            this.principal.getChildren().add(aux);

            //Animacion del circulo
            Timeline timeline = new Timeline();
            KeyValue kvRadius = new KeyValue(aux.radiusProperty(),btn.getHeight() * 1.5);
            KeyValue kvOpacity = new KeyValue(aux.opacityProperty(),0);
            KeyFrame kf = new KeyFrame(Duration.millis(250),kvRadius,kvOpacity);

            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(e -> principal.getChildren().remove(aux));
            this.principal.requestFocus();
            timeline.play();


        };

        this.sprite = new Sprite(Animacion,16,4,240,136,1500);
        this.sprite.setCycleCount(Transition.INDEFINITE);
        this.sprite.resetAnimation();

        this.animacionReproduccion = new Timeline(new KeyFrame(Duration.millis(1000),event1 -> actualizarSlider(0)));
        animacionReproduccion.setCycleCount(Timeline.INDEFINITE);
        this.initKeyboard();
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
                    sprite.pause();
                }
                else {
                    System.out.println(mediaPlayer);
                    mediaPlayer.play();
                    enReproduccion = true;
                    animacionReproduccion.play();
                    sprite.play();
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
            case "b":
                this.back(event);
                break;
            case "s":
                try{
                    FileUtils.saveSong(this.editado,this.songName.getText(), this.songPath, this.teclasExistentes.getItems(),
                            this.mediaPlayer.getTotalDuration().toMillis());
                } catch (IOException e){
                    System.out.println(e.getMessage());
                }
                break;
        }

    }

    @FXML
    private void Reset(ActionEvent event){
        this.teclasExistentes.getItems().clear();
        mediaPlayer.stop();
        enReproduccion = false;
        animacionReproduccion.stop();
        actualizarSlider(0.1);
    }

    private void handleKeyPressed(KeyEvent keyEvent){
        KeyCode code = keyEvent.getCode();
        if (!PressedKeys.containsKey(code)){
            double timeInit = this.mediaPlayer.getCurrentTime().toMillis();
            PressedKeys.put(code,timeInit);
        }
        switch (code){
            case Q:
                effect.run(this.qBtn,Color.RED);
                break;
            case W:
                effect.run(this.wBtn,Color.BLUE);
                break;
            case E:
                effect.run(this.eBtn,Color.YELLOW);
                break;
            case O:
                effect.run(this.oBtn,Color.GREEN);
                break;
            case P:
                effect.run(this.pBtn,Color.ORANGE);
                break;
        }

    }

    private void handleKeyReleased(KeyEvent keyEvent){
        double timeFin = this.mediaPlayer.getCurrentTime().toMillis();
        KeyCode code = keyEvent.getCode();
        int numColor = 0;
        switch (code){
            case Q:
                break;
            case W:
                numColor = 1;
                break;
            case E:
                numColor = 2;
                break;
            case O:
                numColor = 3;
                break;
            case P:
                numColor = 4;
                break;
            default:
                PressedKeys.remove(code);
                return;
        }

        agregarTecla(PressedKeys.get(code),timeFin, numColor);
        PressedKeys.remove(code);
    }

    private void agregarTecla(double timeInicio, double timeFin, int numColor){
        if (timeFin - timeInicio < 500){
            timeFin = timeInicio;
        }
        String aux = switchColores(numColor) + "-" + timeInicio + "-" + timeFin;
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
    private void probarSong(ActionEvent event) throws IOException{
        FileUtils.saveSong(this.editado,this.songName.getText(), this.songPath, this.teclasExistentes.getItems(),
                this.mediaPlayer.getTotalDuration().toMillis());
        Song nuevaCancion = construirSong();
        //Switch de escena con el nuevo argumento
        FXMLLoader loader = new FXMLLoader(App.class.getResource("juego.fxml"));
        Pane root = loader.load();
        juegoController controller = loader.getController();
        controller.setCancionSeleccionada(nuevaCancion);
        controller.postInitialize();


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch Back a Menu
     */
    private void back(ActionEvent event) {
        try{
            mediaPlayer.stop();
            Pane root = loadFXML("Menu");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            System.exit(1);
        }
    }



    //Misc

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

    private void initButtons(){
        configButton(exitBtn,"src/main/resources/images/home.png",28,false,
                "src/main/resources/images/homeHover.png");
        configButton(saveBtn,"src/main/resources/images/disk.png",28,false,
                "src/main/resources/images/diskHover.png");

        Button bigBack = new Button("⏮");
        bigBack.setLayoutX(34);
        bigBack.setLayoutY(417);
        configButton(bigBack,"src/main/resources/images/forward-fast.png",50,true,
                "src/main/resources/images/forward-fastPressed.png");
        principal.getChildren().add(bigBack);
        Button back = new Button("⏪");
        back.setLayoutX(134);
        back.setLayoutY(417);
        configButton(back,"src/main/resources/images/forward.png",50,true,
                "src/main/resources/images/forwardPressed.png");
        principal.getChildren().add(back);
        Button miniBack = new Button("⏴");
        miniBack.setLayoutX(234);
        miniBack.setLayoutY(410);
        configButton(miniBack,"src/main/resources/images/miniFw.png",64,true,
                "src/main/resources/images/miniFwPressed.png");
        principal.getChildren().add(miniBack);
        Button reload = new Button("↺");
        reload.setLayoutX(378);
        reload.setLayoutY(417);
        configButton(reload,"src/main/resources/images/reload.png",50,false,
                "src/main/resources/images/reloadPressed.png");
        principal.getChildren().add(reload);
        Button play = new Button("⏯");
        play.setLayoutX(478);
        play.setLayoutY(417);
        configButton(play,"src/main/resources/images/play-pause.png",50,false,
                "src/main/resources/images/play-pausePressed.png");
        principal.getChildren().add(play);
        Button miniFor = new Button("⏵");
        miniFor.setLayoutX(638);
        miniFor.setLayoutY(410);
        configButton(miniFor,"src/main/resources/images/miniFw.png",64,false,
                "src/main/resources/images/miniFwPressed.png");
        principal.getChildren().add(miniFor);
        Button For = new Button("⏩");
        For.setLayoutX(722);
        For.setLayoutY(417);
        configButton(For,"src/main/resources/images/forward.png",50,false,
                "src/main/resources/images/forwardPressed.png");
        principal.getChildren().add(For);
        Button bigFor = new Button("⏭");
        bigFor.setLayoutX(806);
        bigFor.setLayoutY(417);
        configButton(bigFor,"src/main/resources/images/forward-fast.png",50,false,
                "src/main/resources/images/forward-fastPressed.png");
        principal.getChildren().add(bigFor);
    }
    private void initKeyboard(){
        this.principal.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        this.principal.addEventHandler(KeyEvent.KEY_RELEASED, this::handleKeyReleased);
    }

    /** Metodo que sera llamado cuando se quiera probar la cancion que se esta editando en estos momentos,
     *  construye el objeto Song nuevaCancion con sus teclas pulsadas para usarlas en el juego
     * @return el objeto Song que fue creado
     */
    private Song construirSong(){
        Song nuevaCancion;
        nuevaCancion = new Song(this.songName.getText(),this.songPath,
                this.mediaPlayer.getTotalDuration().toMillis());

        nuevaCancion.ConstruirGrafo(getTeclasPulsadas(this.teclasExistentes.getItems()));

        return nuevaCancion;
    }


    //Funciones estaticas

    public static String switchColores(int numColor){
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
                case "Rojo": break;
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

    private static String getRelativePath(Media media){
        // Obtener la URI del Media
        URI mediaUri = URI.create(media.getSource());

        // Convertir la URI a un Path
        Path mediaPath = Paths.get(mediaUri);

        // Obtener la ruta relativa del archivo
        Path projectPath = Paths.get(System.getProperty("user.dir"));
        Path relativePath = projectPath.relativize(mediaPath);

        return relativePath.toString();
    }

    public void configButton(Button btn, String imgSource, int tam, boolean reverse, String imgReverseSource){
        Image icon = new Image(Paths.get(imgSource).toUri().toString());
        Image iconHover = new Image(Paths.get(imgReverseSource).toUri().toString());
        ImageView imgVw = new ImageView(icon);
        imgVw.setFitHeight(tam);
        imgVw.setFitWidth(tam);
        if (reverse){
            imgVw.setRotate(180);
        }
        btn.setGraphic(imgVw);
        btn.setOnMouseEntered(e -> imgVw.setImage(iconHover));
        btn.setOnMouseExited(e -> imgVw.setImage(icon));
        btn.setOnAction(this::handleButtonEvent);
        btn.getStyleClass().add("icon-button");
    }
    @FXML
    private void editTecla() {
        String selectedItem = teclasExistentes.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No selection", "Please select an item from the list.");
            return;
        }

        DialogPane dialogPane = new DialogPane();
        dialogPane.setHeaderText("Edit Item");

        Label label = new Label("Selected Item:");
        TextField textField = new TextField(selectedItem);

        HBox buttonBox = gethBox(dialogPane, selectedItem, textField);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox dialogContent = new VBox(10, label, textField, buttonBox);
        dialogContent.setPadding(new Insets(10));

        dialogPane.setContent(dialogContent);


        principal.getChildren().add(dialogPane);
    }

    private HBox gethBox(DialogPane dialogPane, String selectedItem, TextField textField) {
        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(event -> principal.getChildren().remove(dialogPane));

        Button deleteButton = new Button("Eliminar");
        deleteButton.setOnAction(event -> {
            teclasExistentes.getItems().remove(selectedItem);
            principal.getChildren().remove(dialogPane);
        });

        Button confirmButton = new Button("Confirmar");
        confirmButton.setOnAction(event -> {
            int selectedIndex = teclasExistentes.getSelectionModel().getSelectedIndex();
            teclasExistentes.getItems().set(selectedIndex, textField.getText());
            principal.getChildren().remove(dialogPane);
        });

        HBox buttonBox = new HBox(10, cancelButton, deleteButton, confirmButton);
        return buttonBox;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
