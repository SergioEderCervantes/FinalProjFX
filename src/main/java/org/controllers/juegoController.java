package org.controllers;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.Modules.*;
import javafx.scene.layout.Pane;

import static org.controllers.App.loadFXML;

public class juegoController implements Initializable {
    public static final Color[] ColoresPosibles =
            {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE};

    private Stage stage;
    private int puntaje=0;
    private int multiplicador=1;
    private int cont=0;
    private Scene scene;
    private Song cancionSeleccionada;
    public Timeline timeline;
    private Tecla aux;
    private final ArrayList<Circle> teclasEnPantalla = new ArrayList<>();
    private long t_Nuevo;       //Tiempo medido para iteraciones
    private long t_inicio;      //Tiempo global
    private boolean reproduciendo;
    //Delay necesario para que las teclas aparezcan on time, se modifica en funcion de la velocidad de bajada de las teclas
    private MediaPlayer reproductor;

    //Variables de componentes del FXML
    @FXML
    AnchorPane principal;
    @FXML
    Button RedButton;
    @FXML
    Button BlueButton;
    @FXML
    Button YellowButton;
    @FXML
    Button GreenButton;
    @FXML
    Button OrangeButton;
    @FXML
    Pane PausePane;
    @FXML
    Rectangle HorizontalRect;
    @FXML
    ImageView img;
    @FXML
    Line l1;
    @FXML
    Line l2;
    @FXML
    Line l3;
    @FXML
    Line l4;
    @FXML
    Line l5;
    @FXML
    Label score;
    @FXML
    Label scoref;
    @FXML
    Label multiplo;
    @FXML
    Pane finalPanel;


    /**
     *Metodo de inicializacion para la Ventana juego, en la cual se settean varios parametros importantes para el juego
     * como lo son la timeLine de la animacion, la inicializacion de los eventos de teclado y botones, la
     * inicializacion de la cancion y finalmente se da una pausa de 2 segundos para que el jugador se prepare y despues
     * se llama al metodo play de timeline para iniciar la animacion del juego,
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initTimeline();
        this.initKeyboard();
        this.initButtons();
        this.initReproduction(0);
        this.t_inicio = this.t_Nuevo = System.currentTimeMillis();
        this.timeline.play();
    }


    private void initTimeline() {
        //REPETITION_MILIS dicta la frecuencia de refresco de la animacion,
        // mientas menor sea el numero mayor seran los fps del juego
        //Aproximadamente, 50 dan 20fps, 33 son 30 fps, 16 son 60fps
        int REPETITION_MILIS = 16;

        this.timeline = new Timeline(new KeyFrame(Duration.millis(REPETITION_MILIS), event -> print()));    //Animacion
        this.timeline.setCycleCount(Timeline.INDEFINITE);   //Ciclos que durara la animacion
        this.t_inicio = System.currentTimeMillis();
    }

    private void initReproduction(int SongID){
        //TODO tomar el ID de la cancion seleccionada y cargar toda la cancion para guardarla en un objeto Song, puede ser:
        //this.cancionSeleccionada = loadSongFromSQL(song_ID);
        this.cancionSeleccionada = TestGraph.RealizarTest();
        this.aux = cancionSeleccionada.getTeclas_pulsadas().getVertice(0);

        //Settea reproductor con la cancion seleccionada
        String audioFilePath = cancionSeleccionada.getRuta();
        String mediaUrl = new File(audioFilePath).toURI().toString();
        Media media = new Media(mediaUrl);
        this.reproductor = new MediaPlayer(media);
        this.reproduciendo = false;
    }

    private void print() {

        int xDef = 0;
        Tecla lastTecla = null;
        long t_final = System.currentTimeMillis();
        long dt = t_final - t_Nuevo;     //Esto sera en milisegundos
        final long DELAY = 1647;
        if (t_final - t_inicio > DELAY && !reproduciendo) {
            reproduciendo = true;
            reproductor.play();
        }

        score.setText(String.valueOf(puntaje));

        multiplo.setVisible(multiplicador != 1);
        multiplo.setText("x" + multiplicador);

        //Primero desplaza todas las que ya existen hacia abajo y elimina del ArrayList y el Panel las teclas que ya no se ven
        try{
            for (Circle circulo : teclasEnPantalla) {
                circulo.setCenterY(circulo.getCenterY() + 4);
                if(circulo.getCenterX() < 461 ){
                    circulo.setCenterX(circulo.getCenterX() - 0.6666);
                }
                else if(circulo.getCenterX() > 739){
                    circulo.setCenterX(circulo.getCenterX() + 0.6666);
                }
                else if(circulo.getCenterX() < 600){
                    circulo.setCenterX(circulo.getCenterX() - 0.3333);
                }else if(circulo.getCenterX() >600){
                    circulo.setCenterX(circulo.getCenterX() + 0.3333);
                }

                if (circulo.getCenterY() > 620) {
                    if(circulo.isVisible()){
                        multiplicador=1;
                        cont = 0;
                    }
                    teclasEnPantalla.remove(circulo);
                    principal.getChildren().remove(circulo);
                }
            }
        }catch (Exception e){}
        if(teclasEnPantalla.isEmpty() && aux.getNumAristasAdyacentes() == 0){
            scoref.setText(String.valueOf(puntaje));
            pantallaFinal();
        }
        //Agregamos las nuevas teclas si deben de aparecer
        if (aux.getNumAristasAdyacentes() != 0) {
            if (dt >= aux.getDtSiguiente()) {
                t_Nuevo = t_final;
                for (Arista i : aux.getAristasAdyacentes()) {
                    switch (i.destino().numColor) { //Dependiendo del color se inician en coordenadas en X diferentes
                        case 0:
                            xDef = 460;
                            break;
                        case 1:
                            xDef = 525;
                            break;
                        case 2:
                            xDef = 600;
                            break;
                        case 3:
                            xDef = 675;
                            break;
                        case 4:
                            xDef = 740;
                            break;
                    }
                    Circle circle = new Circle(xDef, 155, 20, ColoresPosibles[i.destino().numColor]);
                    principal.getChildren().add(circle);
                    teclasEnPantalla.add(circle);
                    circle.toBack();
                    lastTecla = i.destino();
                }
                aux = lastTecla;
            }
            this.setPositions();
        }else {
            //TODO cuando acabe la cancion de ejecutarse, salir al menu principal
        }
    }

    /**
     * KeyListener para los eventos del teclado, cuando se presiona una tecla realiza un switch para ver si se
     * Presiono una tecla de interes, en caso de que si, acciona los eventos de la GUI Necesarios, los cuales son
     * darle focus al boton, accionarlo y realizar la animacion de que se pulsó (pulsado y liberado)
     * si se pulsa la tecla ESC
     */
    private void initKeyboard(){
        this.principal.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();

            if(this.timeline.getStatus() == Animation.Status.RUNNING) {
                switch (code) {
                    case Q:
                        this.RedButton.setFocusTraversable(true);
                        this.RedButton.fire();

                    break;
                case W:
                    this.BlueButton.setFocusTraversable(true);
                    this.BlueButton.fire();
                    break;
                case E:
                    this.YellowButton.setFocusTraversable(true);
                    this.YellowButton.fire();

                    break;
                case O:
                    this.GreenButton.setFocusTraversable(true);
                    this.GreenButton.fire();

                    break;
                case P:
                    this.OrangeButton.setFocusTraversable(true);
                    this.OrangeButton.fire();
                    break;
                case ESCAPE:
                    pause();
                    break;
                default:
                    break;
                }
            }
        });
    }

    private void initButtons(){
        this.RedButton.toFront();
        this.BlueButton.toFront();
        this.YellowButton.toFront();
        this.GreenButton.toFront();
        this.OrangeButton.toFront();


    }
    private void pantallaFinal(){
        timeline.pause();
        RedButton.setDisable(true);
        BlueButton.setDisable(true);
        YellowButton.setDisable(true);
        GreenButton.setDisable(true);
        OrangeButton.setDisable(true);
        finalPanel.setVisible(true);
    }

    private void pause(){
        timeline.pause();
        reproductor.pause();
        RedButton.setDisable(true);
        BlueButton.setDisable(true);
        YellowButton.setDisable(true);
        GreenButton.setDisable(true);
        OrangeButton.setDisable(true);
        PausePane.setVisible(true);
        PausePane.toFront();
    }


    @FXML
    public void continuar() {
        PausePane.setVisible(false);
        RedButton.setDisable(false);
        BlueButton.setDisable(false);
        YellowButton.setDisable(false);
        GreenButton.setDisable(false);
        OrangeButton.setDisable(false);
        timeline.play();
        reproductor.play();
    }


    @FXML //TODO Retorno al menu
    public void back(ActionEvent event) throws IOException {
        Pane root = loadFXML("Menu");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reproductor.stop();
        timeline.stop();



    }
    @FXML
    private void btnRActivado(){
        Rectangle rect = this.makeRect(RedButton);
        checkColitions(rect);
    }


    @FXML
    private void btnBActivado(){
        Rectangle rect = this.makeRect(BlueButton);
        checkColitions(rect);

    }


    @FXML
    private void btnYActivado(){
        Rectangle rect = this.makeRect(YellowButton);
        checkColitions(rect);

    }


    @FXML
    private void btnGActivado(){
        Rectangle rect = this.makeRect(GreenButton);
        checkColitions(rect);

    }


    @FXML
    private void btnOActivado(){
        Rectangle rect = this.makeRect(OrangeButton);
        checkColitions(rect);

    }

    private Rectangle makeRect(Button btn){
        return new Rectangle(btn.getLayoutX(), btn.getLayoutY(), btn.getWidth(), btn.getHeight());
    }

    private void setPositions(){
        this.HorizontalRect.toBack();
        this.l1.toBack();
        this.l2.toBack();
        this.l3.toBack();
        this.l4.toBack();
        this.l5.toBack();
        this.img.toBack();
    }

    private void checkColitions(Rectangle rect){

        for (Circle circle : teclasEnPantalla) {


//            System.out.println(band);
            if (rect.contains(circle.getCenterX(), circle.getCenterY()) ||
                    rect.contains(circle.getCenterX(), circle.getCenterY() + circle.getRadius()) ||
                    rect.contains(circle.getCenterX(), circle.getCenterY() - circle.getRadius())){

                puntaje = puntaje+(multiplicador * 25);
                cont++;

                if(cont>=8 && cont<12){
                    multiplicador=2;
                }
                else {
                    if (cont >= 12 && cont < 24) {
                        multiplicador = 4;
                    }
                    else {
                        if (cont >= 24) {
                            multiplicador = 8;
                        }
                    }
                }
                //TODO sprite de explosion de circulo, aumentar el marcador
                circle.setVisible(false);

                    //Esto es mil veces mejor que destruirlo aqui
                return;
            }
            else {



            }
        }
        //TODO Quitar puntos porque se presiono una tecla cuando no habia nada
    }
}

