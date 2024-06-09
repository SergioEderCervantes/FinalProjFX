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
import org.Modules.MySqlConn;
import org.Modules.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import static org.controllers.App.loadFXML;
import static org.controllers.multijugadorController.band;

public class selectorController {
    private Stage stage;
    private Scene scene;
    static Pair<Integer,String>[] canciones;
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

        // Crear un rectángulo con bordes redondeados
        Rectangle clip = new Rectangle(uno.getFitWidth(), uno.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        uno.setClip(clip);


        // Crear un rectángulo con bordes redondeados
         clip = new Rectangle(dos.getFitWidth(), dos.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        dos.setClip(clip);


        // Crear un rectángulo con bordes redondeados
         clip = new Rectangle(tres.getFitWidth(), tres.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        tres.setClip(clip);


        // Crear un rectángulo con bordes redondeados
         clip = new Rectangle(cuatro.getFitWidth(), cuatro.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        cuatro.setClip(clip);


        // Crear un rectángulo con bordes redondeados
        clip = new Rectangle(cinco.getFitWidth(), cinco.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        cinco.setClip(clip);



        clip = new Rectangle(seis.getFitWidth(), seis.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        seis.setClip(clip);


        // Crear un rectángulo con bordes redondeados
         clip = new Rectangle(siete.getFitWidth(), siete.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        siete.setClip(clip);


        // Crear un rectángulo con bordes redondeados
         clip = new Rectangle(ocho.getFitWidth(), ocho.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        ocho.setClip(clip);


        // Crear un rectángulo con bordes redondeados
         clip = new Rectangle(nueve.getFitWidth(), nueve.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        nueve.setClip(clip);


        // Crear un rectángulo con bordes redondeados
        clip = new Rectangle(diez.getFitWidth(), diez.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        diez.setClip(clip);


        // Crear un rectángulo con bordes redondeados
        clip = new Rectangle(once.getFitWidth(), once.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        once.setClip(clip);

        clip = new Rectangle(doce.getFitWidth(), doce.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        doce.setClip(clip);

        clip = new Rectangle(trece.getFitWidth(), trece.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        trece.setClip(clip);


        clip = new Rectangle(catorce.getFitWidth(), catorce.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        catorce.setClip(clip);


        clip = new Rectangle(quince.getFitWidth(), quince.getFitHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        quince.setClip(clip);


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
        fondo.stop();
        if( band==0){

            FXMLLoader loader = new FXMLLoader(getClass().getResource("juego.fxml"));


                Pane root = loader.load();
                juegoController controller = loader.getController();
                controller.loadSongFromDB(canciones[show-1].getFirst());
                controller.postInitialize();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();




        }else if(band == 1){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Local.fxml"));

            Pane root = loader.load();
            juegoLocalController controller = loader.getController();
            controller.loadSongFromDB(canciones[show-1].getFirst());
            controller.postInitialize();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else{
            //multi en linea
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
}
