package org.controllers;



import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.Modules.*;

public class juegoController implements Initializable {
    public static final Color[] ColoresPosibles =
            {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE};


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
        


        //Primero desplaza todas las que ya existen hacia abajo y elimina del ArrayList y el Panel las teclas que ya no se ven
        try{
            for (Circle circulo : teclasEnPantalla) {
                circulo.setCenterY(circulo.getCenterY() + 3);
                if (circulo.getCenterY() > 600) {

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
                    default:
                        break;
                }
            }
        });
    }
    @FXML
    private void btnRActivado(){
        System.out.println("TECLA ROJA");
        Rectangle rect = this.makeRect(RedButton);

    }
    @FXML
    private void btnBActivado(){ 
        System.out.println("TECLA AZUL");
    }
    @FXML
    private void btnYActivado(){
        System.out.println("TECLA AMARILLA");
    }
    @FXML
    private void btnGActivado(){
        System.out.println("TECLA VERDE");
    }
    @FXML
    private void btnOActivado(){
        System.out.println("TECLA NARANJA ");
    }

    private void releaseButton(Button button){
        PauseTransition pause = new PauseTransition(Duration.millis(48));
        pause.setOnFinished(actionEvent -> button.disarm());
        pause.play();
    }

    private Rectangle makeRect(Button btn){
        return new Rectangle(btn.getLayoutX(), btn.getLayoutY(), btn.getWidth(), btn.getHeight());
    }


//    private void checkColitions
    private void setPositions(){
        this.HorizontalRect.toBack();
        this.l1.toBack();
        this.l2.toBack();
        this.l3.toBack();
        this.l4.toBack();
        this.l5.toBack();
    }
}
