package org.controllers;



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
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
    private Song prueba;
    public Timeline timeline;
    private Tecla aux;
    private final ArrayList<Circle> teclasEnPantalla = new ArrayList<>();
    private long t_inicio;

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
    Label multiplo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Dicta la frecuencia de refresco de la animacion, mientas menor sea el numero mayor seran los fps del juego
        //Aproximadamente, 50 dan 20fps, 33 son 30 fps, 16 son 60fps
        int REPETITION_MILIS = 16;
        this.prueba = TestGraph.RealizarTest();
        this.timeline = new Timeline(new KeyFrame(Duration.millis(REPETITION_MILIS), event -> print()));    //Animacion
        this.aux = prueba.getTeclas_pulsadas().getVertice(0);
        this.timeline.setCycleCount(Timeline.INDEFINITE);   //Ciclos que durara la animacion
        this.t_inicio = System.currentTimeMillis();
        this.timeline.play();
        this.iniciaTeclado();
    }

    private void print() {

        int xDef = 0;
        Tecla lastTecla = null;
        long t_final = System.currentTimeMillis();
        long dt = t_final - t_inicio;     //Esto sera en milisegundos

        score.setText(String.valueOf(puntaje));
        if(multiplicador != 1){
            multiplo.setVisible(true);
        }
        else{
            multiplo.setVisible(false);
        }

        multiplo.setText("x"+String.valueOf(multiplicador));
        //Primero desplaza todas las que ya existen hacia abajo y elimina del ArrayList y el Panel las teclas que ya no se ven
        try{
            for (Circle circulo : teclasEnPantalla) {
                circulo.setCenterY(circulo.getCenterY() + 5);
                if (circulo.getCenterY() > 620) {

                    teclasEnPantalla.remove(circulo);
                    principal.getChildren().remove(circulo);
                }
            }
        }catch (Exception e){}

        //Agregamos las nuevas teclas si deben de aparecer
        if (aux.getNumAristasAdyacentes() != 0) {
            if (dt >= aux.getDtSiguiente()) {
                t_inicio = t_final;
                for (Arista i : aux.getAristasAdyacentes()) {
                    switch (i.destino().numColor) { //Dependiendo del color se inician en coordenadas en X diferentes
                        case 0:
                            xDef = 400;
                            break;
                        case 1:
                            xDef = 500;
                            break;
                        case 2:
                            xDef = 600;
                            break;
                        case 3:
                            xDef = 700;
                            break;
                        case 4:
                            xDef = 800;
                            break;
                    }
                    Circle circle = new Circle(xDef, 30, 20, ColoresPosibles[i.destino().numColor]);
                    principal.getChildren().add(circle);
                    teclasEnPantalla.add(circle);
                    circle.toBack();
                     lastTecla = i.destino();
                }
                aux = lastTecla;
            }
            this.setPositions();

        }
    }

    /**
     * KeyListener para los eventos del teclado, cuando se presiona una tecla realiza un switch para ver si se
     * Presiono una tecla de interes, en caso de que si, acciona los eventos de la GUI Necesarios, los cuales son
     * darle focus al boton, accionarlo y realizar la animacion de que se pulsó (pulsado y liberado)
     * si se pulsa la tecla ESC
     */
    private void iniciaTeclado(){
        this.RedButton.toFront();
        this.BlueButton.toFront();
        this.YellowButton.toFront();
        this.GreenButton.toFront();
        this.OrangeButton.toFront();
        this.principal.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();

            if(this.timeline.getStatus() == Animation.Status.RUNNING) {
                switch (code) {
                    case Q:
                        this.RedButton.setFocusTraversable(true);
                        this.RedButton.fire();
                        this.RedButton.arm();
                        this.releaseButton(RedButton);

                    break;
                case W:
                    this.BlueButton.setFocusTraversable(true);
                    this.BlueButton.fire();
                    this.BlueButton.arm();
                    this.releaseButton(BlueButton);
                    break;
                case E:
                    this.YellowButton.setFocusTraversable(true);
                    this.YellowButton.fire();
                    this.YellowButton.arm();
                    this.releaseButton(YellowButton);
                    break;
                case R:
                    this.GreenButton.setFocusTraversable(true);
                    this.GreenButton.fire();
                    this.GreenButton.arm();
                    this.releaseButton(GreenButton);
                    break;
                case T:
                    this.OrangeButton.setFocusTraversable(true);
                    this.OrangeButton.fire();
                    this.OrangeButton.arm();
                    this.releaseButton(OrangeButton);
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

    private void pause(){
        timeline.pause();
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
    }
    @FXML //TODO Retorno al menu
    public void back(ActionEvent event) throws IOException {
        Pane root = loadFXML("Menu");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


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

    private void releaseButton(Button button){
        PauseTransition pause = new PauseTransition(Duration.millis(48));
        pause.setOnFinished(actionEvent -> button.disarm());
        pause.play();
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
    }

    private void checkColitions(Rectangle rect){

        for (Circle circle : teclasEnPantalla) {
            if (rect.contains(circle.getCenterX(), circle.getCenterY()) ||
                    rect.contains(circle.getCenterX(), circle.getCenterY() + circle.getRadius()) ||
                        rect.contains(circle.getCenterX(), circle.getCenterY() - circle.getRadius())){
                puntaje = puntaje+(multiplicador * 25);
                cont++;
                if(cont>=4 && cont<8){
                    multiplicador=2;
                }
                else {
                    if (cont >= 8 && cont < 12) {
                        multiplicador = 4;
                    }
                    else {
                        if (cont >= 12) {
                            multiplicador = 8;
                        }
                    }
                }

                //TODO sprite de explosion de circulo, aumentar el marcador
                circle.setVisible(false);   //Esto es mil veces mejor que destruirlo aqui
                System.out.println("Score: "+puntaje);
                return;
            }
            else {
                multiplicador=1;
                cont=0;
            }
        }
        //TODO Quitar puntos porque se presiono una tecla cuando no habia nada
    }
}

