package org.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.Modules.Conexion_UDP;
import org.Modules.GAME_MODE;
import org.Modules.MySqlConn;
import org.Modules.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import static org.controllers.App.loadFXML;

public class selectorController {
    private Stage stage;
    private Scene scene;
    static Pair<Integer,String>[] canciones;
    private GAME_MODE gameMode;
    Conexion_UDP connector;
    @FXML
    private ImageView uno;
    @FXML
    private ImageView dos;
    @FXML
    private ImageView tres;
    @FXML
    private ImageView cuatro;
    @FXML
    private ImageView cinco;
    @FXML
    private ImageView seis;
    @FXML
    private ImageView siete;
    @FXML
    private ImageView ocho;
    @FXML
    private ImageView nueve;
    @FXML
    private ImageView diez;
    @FXML
    private ImageView once;
    @FXML
    private ImageView doce;
    @FXML
    private ImageView trece;
    @FXML
    private ImageView catorce;
    @FXML
    private ImageView quince;
    @FXML
    private ImageView note;
    @FXML
    private MediaView backgSelect;
    int show=1;
    @FXML
    private Label num;
    @FXML
    private Label name;
    @FXML
    private Label err;
    @FXML
    private MediaPlayer fondo;

    public void setGameMode(GAME_MODE gameMode) {
        this.gameMode = gameMode;
    }
    public void setConnector(Conexion_UDP connector) {
        this.connector = connector;
    }
    private static void search(){
        final String query = "SELECT idSong, name FROM song";
        MySqlConn conn = new MySqlConn();
        conn.consult(query);
        canciones = null;

        int n = 0;
        if (conn.rs != null){

            try {
                conn.rs.last();
                n = conn.rs.getRow();
                conn.rs.first();
            }catch (Exception e){
                System.out.println("Exception:" + e.getMessage());
            }
            canciones = new Pair[n];

            for (int i = 0; i < n; i++) {
                try {
                    canciones[i] = new Pair<>(conn.rs.getInt(1),conn.rs.getString(2));
                    conn.rs.next();
                }catch (SQLException e){
                    System.out.println("SQLException:" + e.getMessage());
                }
            }

        }
        conn.closeRsStmt();

    }

    public void translateAnimation(double duration, Node node, double width){
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(duration),node);
        translateTransition.setByX(width);
        translateTransition.play();
    }
    public void initialize() {
        search();
        URL videoUrl = getClass().getResource("/images/cc.mp4");
        if (videoUrl != null) {
            Media media = new Media(videoUrl.toExternalForm());
            fondo = new MediaPlayer(media);
            fondo.setCycleCount(MediaPlayer.INDEFINITE); // Hacer que el video se repita indefinidamente
            backgSelect.setMediaPlayer(fondo);
            fondo.play(); // Reproducir el video
            backgSelect.toBack();
        }

        uno.setFitWidth(255);
        uno.setFitHeight(270);

        configRect(uno);
        configRect(dos);
        configRect(tres);
        configRect(cuatro);
        configRect(cinco);
        configRect(seis);
        configRect(siete);
        configRect(ocho);
        configRect(nueve);
        configRect(diez);
        configRect(once);
        configRect(doce);
        configRect(trece);
        configRect(catorce);
        configRect(quince);


        URL overlayImageUrl = getClass().getResource("/images/carr.png");
        if (overlayImageUrl != null) {
            Image overlayImage = new Image(overlayImageUrl.toExternalForm());
            note.setImage(overlayImage);
        }
        note.setFitWidth(517);
        note.setFitHeight(551);
        name.setText(canciones[show-1].getSecond());
        translateAnimation(0.5,dos,1200);
        translateAnimation(0.5,tres,1200);
        translateAnimation(0.5,cuatro,1200);
        translateAnimation(0.5,cinco,1200);
        translateAnimation(0.5,seis,1200);
        translateAnimation(0.5,siete,1200);
        translateAnimation(0.5,ocho,1200);
        translateAnimation(0.5,nueve,1200);
        translateAnimation(0.5,diez,1200);
        translateAnimation(0.5,once,1200);
        translateAnimation(0.5,doce,1200);
        translateAnimation(0.5,trece,1200);
        translateAnimation(0.5,catorce,1200);
        translateAnimation(0.5,quince,1200);
    }
    public void switchTojuego(ActionEvent event) throws IOException {
        if( canciones[show-1].getSecond() instanceof String){
            fondo.stop();
            switch (gameMode){
                case SINGLE_PLAYER:
                    loadSinglePlayer(event);
                    break;
                case LOCAL_MULTIPLAYER:
                    loadLocalMultiplayer(event);
                    break;
                case ONLINE_MULTIPLAYER:
                    try{
                        loadOnlineMultiPlayer(event);
                    }catch (Exception e){}
                    break;
            }
        }
    }
    public void back(ActionEvent event) throws IOException {
        fondo.stop();
        Pane root = loadFXML("Jugador");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void atras(ActionEvent event){

        switch (show){

            case 1:
                translateAnimation(0.5,dos,1200);

                num.setText("1/15");
                break;
            case 2:
                translateAnimation(0.5,tres,1200);
                show--;
                num.setText("2/15");
                break;
            case 3:
                translateAnimation(0.5,cuatro,1200);
                show--;
                num.setText("3/15");
                break;
            case 4:
                translateAnimation(0.5,cinco,1200);
                show--;
                num.setText("4/15");
                break;
            case 5:
                translateAnimation(0.5,seis,1200);
                show--;
                num.setText("5/15");
                break;
            case 6:
                translateAnimation(0.5,siete,1200);
                show--;
                num.setText("6/15");
                break;
            case 7:
                translateAnimation(0.5,ocho,1200);
                show--;
                num.setText("7/15");
                break;
            case 8:
                translateAnimation(0.5,nueve,1200);
                show--;
                num.setText("8/15");
                break;
            case 9:
                translateAnimation(0.5,diez,1200);
                show--;
                num.setText("9/15");
                break;
            case 10:
                translateAnimation(0.5,once,1200);
                show--;
                num.setText("10/15");
                break;
            case 11:
                translateAnimation(0.5,doce,1200);
                show--;
                num.setText("11/15");
                break;
            case 12:
                translateAnimation(0.5,trece,1200);
                show--;
                num.setText("12/15");
                break;
            case 13:
                translateAnimation(0.5,catorce,1200);
                show--;
                num.setText("13/15");
                break;
            case 14:
                translateAnimation(0.5,quince,1200);
                show--;
                num.setText("14/15");
                break;
            default:

        }
        name.setText(canciones[show-1].getSecond());
    }

    @FXML
    void adelante(ActionEvent event){
        switch (show){

            case 1:
                translateAnimation(0.5,dos,-1200);
                show++;
                num.setText("2/15");
                break;
            case 2:
                translateAnimation(0.5,tres,-1200);
                show++;
                num.setText("3/15");
                break;
            case 3:
                translateAnimation(0.5,cuatro,-1200);
                show++;
                num.setText("4/15");
                break;
            case 4:
                translateAnimation(0.5,cinco,-1200);
                show++;
                num.setText("5/15");
                break;
            case 5:
                translateAnimation(0.5,seis,-1200);
                show++;
                num.setText("6/15");
                break;
            case 6:
                translateAnimation(0.5,siete,-1200);
                show++;
                num.setText("7/15");
                break;
            case 7:
                translateAnimation(0.5,ocho,-1200);
                show++;
                num.setText("8/15");
                break;
            case 8:
                translateAnimation(0.5,nueve,-1200);
                show++;
                num.setText("9/15");
                break;
            case 9:
                translateAnimation(0.5,diez,-1200);
                show++;
                num.setText("10/15");
                break;
            case 10:
                translateAnimation(0.5,once,-1200);
                show++;
                num.setText("11/15");
                break;
            case 11:
                translateAnimation(0.5,doce,-1200);
                show++;
                num.setText("12/15");
                break;
            case 12:
                translateAnimation(0.5,trece,-1200);
                show++;
                num.setText("13/15");
                break;
            case 13:
                translateAnimation(0.5,catorce,-1200);
                show++;
                num.setText("14/15");
                break;
            case 14:
                translateAnimation(0.5,quince,-1200);

                num.setText("15/15");
                break;
            default:
        }
        name.setText(canciones[show-1].getSecond());
    }

    private void loadSinglePlayer(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("juego.fxml"));
        Pane root = loader.load();
        juegoController controller = loader.getController();
        controller.loadSongFromDB(canciones[show-1].getFirst());
        controller.postInitialize();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadLocalMultiplayer(ActionEvent event) throws IOException{
        //Logica para el multi local
    }

    private void loadOnlineMultiPlayer(ActionEvent event) throws IOException, InterruptedException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("juegoOnline.fxml"));
        Pane root = loader.load();
        JuegoOnlineController controller = loader.getController();
        controller.loadSongFromDB(canciones[show-1].getFirst());
        connector.sendData("Ready");
        while (!connector.getLastRecieved().equals("Ready")){
            Thread.sleep(20);
        }
        controller.setConnector(connector);
        controller.postInitialize();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private static void configRect(ImageView target){
        Rectangle clip = new Rectangle(target.getFitWidth(), target.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        target.setClip(clip);
    }
}
