package org.controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import org.Modules.*;

import java.util.ArrayList;


public class Pruebas {
    private CustomRunnable<Button,Color> effect;
    @FXML
    public AnchorPane Principal;
    public Rectangle longNote;
    public Glow glowEffect = new Glow(0.8);
    public DropShadow dropShadow;
    @FXML
    public  Button RedButton;
    @FXML
    public  Button BlueButton;
    @FXML
    public  Button YellowButton;
    @FXML
    public  Button GreenButton;
    @FXML
    public  Button OrangeButton;

    enum AnimationMoments {CRECIENDO,TRASLADANDO,DECRECIENDO,ACABADO;}
    AnimationMoments animation = AnimationMoments.CRECIENDO;
    boolean empezado = false;
    Rectangle rectangle;
    Timeline timeline;
    @FXML
    private void inicialLaAnimacion(){

        // Crear un círculo que siga al cursor
//        Circle follower = new Circle(10, Color.BLUE);
//        follower.setVisible(false); // Invisible al inicio
//
//        // Crear un cuadro de texto para mostrar las coordenadas
//        Label coordinatesLabel = new Label();
//        coordinatesLabel.setStyle("-fx-background-color: white; -fx-padding: 2px;");
//        coordinatesLabel.setLayoutX(20);
//        coordinatesLabel.setLayoutY(400);
//        // Agregar el círculo y el cuadro de texto al contenedor principal
//        Principal.getChildren().addAll(follower, coordinatesLabel);
//        Scene scene = Principal.getScene();
//        // Manejar el movimiento del cursor
//        scene.setOnMouseMoved(event -> {
//            // Mover el círculo al cursor
//            follower.setCenterX(event.getX());
//            follower.setCenterY(event.getY());
////            follower.setVisible(true);
//
//            // Actualizar las coordenadas del cuadro de texto
//            coordinatesLabel.setText(String.format("(%.2f, %.2f)", event.getX(), event.getY()));
////            coordinatesLabel.setLayoutX(event.getX() + 15);
////            coordinatesLabel.setLayoutY(event.getY() - 10);
//        });

//        this.ejemplo1();
//        this.ejemplo2();
//        this.ejemplo3();
//        this.ejemplo4();
//        this.ejemplo5();
//        this.ejemplo6();
//        this.ejemplo7();
        this.ejemplo8();
//        this.ejemplo9();
    }

    @FXML
    private void reset(){
        Principal.getChildren().clear();
        Button btn1 = new Button();
        btn1.setOnAction(e -> inicialLaAnimacion());
        Button btn2 = new Button();
        btn2.setOnAction(e -> reset());
        Principal.getChildren().addAll(btn1, btn2);
        btn1.setLayoutX(57);
        btn2.setLayoutX(57);
        btn1.setLayoutY(150);
        btn2.setLayoutY(200);
        btn1.setText("Iniciar");
        btn2.setText("Reiniciar");
    }

    private void ejemplo1(){
        timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> animation()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void ejemplo2(){

        Timeline timeline = new Timeline(
                // Cambio de color a medida que avanza el tiempo
                new KeyFrame(Duration.ZERO, new KeyValue(longNote.fillProperty(), Color.LIMEGREEN)),
                new KeyFrame(Duration.seconds(2), new KeyValue(longNote.fillProperty(), Color.DARKGREEN)),

                // Aumentar el grosor del borde para indicar progreso
                new KeyFrame(Duration.ZERO, new KeyValue(longNote.strokeWidthProperty(), 2)),
                new KeyFrame(Duration.seconds(2), new KeyValue(longNote.strokeWidthProperty(), 8)),

                // Efecto Glow que se intensifica
                new KeyFrame(Duration.ZERO, new KeyValue(glowEffect.levelProperty(), 0.0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(glowEffect.levelProperty(), 0.8))
        );

        // Ejecutar la animación (simula la duración de la nota larga)
        timeline.play();
    }
    private void ejemplo3(){
        Rectangle rect = new Rectangle(200,200,50,100);
        rect.setFill(Color.LIMEGREEN);
        Principal.getChildren().add(rect);

        Timeline tm = new Timeline(new KeyFrame(Duration.millis(50), event -> {
           rect.setY(rect.getY() + 1);
           rect.setHeight(rect.getHeight() - 1);

        }));
        tm.setCycleCount(600);
        tm.play();
        tm.setOnFinished(event -> Principal.getChildren().remove(rectangle));
    }
    private void ejemplo4(){
        // Timeline para animar el resplandor (Glow) y la sombra (DropShadow)
        Glow glow = new Glow(0.0);

        // Crear un efecto compuesto que combine DropShadow y Glow
        Blend combinedEffect = new Blend();
        combinedEffect.setBottomInput(dropShadow);
        combinedEffect.setTopInput(glow);
        longNote.setEffect(combinedEffect);

        // Timeline para animar la sombra y el resplandor
        Timeline effectsTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(dropShadow.radiusProperty(), 10),
                        new KeyValue(dropShadow.colorProperty(), Color.NAVY),
                        new KeyValue(glow.levelProperty(), 0.0)
                ),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(dropShadow.radiusProperty(), 20),
                        new KeyValue(dropShadow.colorProperty(), Color.DARKBLUE),
                        new KeyValue(glow.levelProperty(), 0.8)
                )
        );

        // ScaleTransition para hacer que la nota "pulse" ligeramente
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), longNote);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);

        // Iniciar las animaciones
        effectsTimeline.setCycleCount(Timeline.INDEFINITE);
        effectsTimeline.setAutoReverse(true);
        effectsTimeline.play();

        scaleTransition.play();
    }
    private void ejemplo5(){
        // Crear el rectángulo

        // Crear la curva sinusoidal
        // Dimensiones iniciales del rectángulo
        double rectWidth = 40;
        double rectHeight = 200;

        // Crear el rectángulo contenedor
        Rectangle rect = new Rectangle(rectWidth, rectHeight);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLACK);

        // Crear la onda sinusoidal
        Path sineWave = createSineWave(rectWidth, rectHeight, rectWidth/5, 0.008,0 );
        Path sineWave2 = createSineWave(rectWidth,rectHeight,rectWidth/5,0.008, Math.PI);
        sineWave.setStroke(Color.BLUE);
        sineWave2.setStroke(Color.RED);
        rect.setX(500);
        rect.setY(200);
        sineWave.setLayoutX(500);
        sineWave.setLayoutY(200);
        sineWave2.setLayoutX(500);
        sineWave2.setLayoutY(200);
        Group group = new Group(rect,sineWave,sineWave2);

        // Configurar el clip para que la onda esté contenida dentro del rectángulo
        Principal.getChildren().add(group);

        // Animar la altura del rectángulo
//        animateRectangleHeight(rect);
        // Animar la altura del rectangulo
        animateSineWaveHeight(sineWave, 2.0, 3000); // Escalar al doble de la altura original


    }

    private void ejemplo6(){
        // Dimensiones iniciales del contenedor
        double rectWidth = 40;
        double rectHeight = 500;

        // Crear la onda sinusoidal
        Path sineWave = createSineWave(rectWidth, rectHeight, rectWidth/5, 0.01, 0);
        sineWave.setStroke(Color.BLUE);
        Path sineWave2 = createSineWave(rectWidth,rectHeight,rectWidth/5,0.01,Math.PI);
        sineWave2.setStroke(Color.BLUE);
        sineWave.setLayoutX(700);
        sineWave.setLayoutY(200);
        sineWave2.setLayoutX(700);
        sineWave2.setLayoutY(200);

        //Creacion de efectos para las ondas
        DropShadow dropShadow = new DropShadow();
        Glow glow = new Glow(0.0);
        Blend combinedEffect = new Blend();
        combinedEffect.setBottomInput(dropShadow);
        combinedEffect.setTopInput(glow);

        // Crear un rectángulo que actuará como el contenedor (clip)
        Rectangle clipRect = new Rectangle(rectWidth, rectHeight);
        Rectangle clipRect2 = new Rectangle(rectWidth, rectHeight);
        sineWave.setClip(clipRect);
        sineWave2.setClip(clipRect2);
        sineWave.setEffect(combinedEffect);
        sineWave2.setEffect(combinedEffect);
        Principal.getChildren().add(sineWave);
        Principal.getChildren().add(sineWave2);



        animateClipHeight(clipRect, dropShadow,glow, 0, 200, 500,
                clipRect.getY(),clipRect.getY()+200);
        animateClipHeight(clipRect2, dropShadow,glow, 0, 200, 500,
                clipRect.getY(),clipRect.getY()+200);
    }
    private long timeInicio;
    private void ejemplo7(){
        Rhomboid rb = new Rhomboid(-2,400,20,0);
        timeInicio = System.currentTimeMillis();
        Timeline tm = new Timeline(new KeyFrame(Duration.millis(16),event -> this.fisicas(rb)));
        Principal.getChildren().add(rb.getRhomboid());
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.setAutoReverse(false);
        tm.play();

    }
    private void initButtons(Timeline tm){
        this.RedButton.toFront();
        this.BlueButton.toFront();
        this.YellowButton.toFront();
        this.GreenButton.toFront();
        this.OrangeButton.toFront();

        //Configurar el efecto de presionado de un boton
        this.effect = (Button btn, Color color) -> {
            Rectangle aux = new Rectangle();
            aux.setWidth(btn.getWidth());
            aux.setHeight(btn.getHeight());
            aux.setArcWidth(30);
            aux.setArcHeight(30);
            aux.setFill(color.deriveColor(1,1,1,0.5));
            aux.setX(btn.getLayoutX());
            aux.setY(btn.getLayoutY());

            Principal.getChildren().add(aux);

            KeyValue kvWidth = new KeyValue(aux.widthProperty(), btn.getWidth() * 2);
            KeyValue kvHeight = new KeyValue(aux.heightProperty(), btn.getHeight() * 2);
            KeyValue kvX = new KeyValue(aux.xProperty(),btn.getLayoutX() - btn.getWidth() / 2);
            KeyValue kvY = new KeyValue(aux.yProperty(),btn.getLayoutY() - btn.getHeight() / 2);
            KeyValue kvOpacity = new KeyValue(aux.opacityProperty(), 0);
            KeyFrame kf = new KeyFrame(Duration.millis(500), kvWidth, kvOpacity,kvX,kvHeight,kvY);

            tm.getKeyFrames().add(kf);
            tm.setOnFinished(event -> Principal.getChildren().remove(aux));
            tm.play();


        };

    }
    ArrayList<TeclaLarga> tls;
    private void ejemplo8(){
        timeInicio = System.currentTimeMillis();
        Timeline tm = new Timeline(new KeyFrame(Duration.millis(16), event -> fEjemplo8()));

        this.initKeyboard(tm);
        this.initButtons(tm);
        tls = new ArrayList<>();
        tls.clear();
        tls.add(new TeclaLarga(0,460,700, false));
        tls.add(new TeclaLarga(1,525,700, false));
        tls.add(new TeclaLarga(2,600,700, false));
        tls.add(new TeclaLarga(3,675,700, false));
        tls.add(new TeclaLarga(4,740,700, false));

       Principal.getChildren().addAll(tls);

        tm.setCycleCount(Timeline.INDEFINITE);
        tm.setAutoReverse(false);
        tm.play();
    }

    private void fEjemplo8(){
        long timeFin = System.currentTimeMillis();
        double dt = timeFin - timeInicio;
        for(TeclaLarga t: tls){
            t.fisica    (10);
        }
    }
    private void initKeyboard(Timeline tm){
        this.Principal.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            KeyCode code = keyEvent.getCode();

            if(tm.getStatus() == Animation.Status.RUNNING) {
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
                    default:
                        break;
                }
            }
        });
    }
    private void checkColitions(Rectangle rect){

        for (TeclaLarga t : tls) {
            if (rect.contains(t.getCircle().getCenterX(), t.getCircle().getCenterY()) ||
                    rect.contains(t.getCircle().getCenterX(), t.getCircle().getCenterY() + t.getCircle().getRadius()) ||
                    rect.contains(t.getCircle().getCenterX(), t.getCircle().getCenterY() - t.getCircle().getRadius())){

                //TODO sprite de explosion de circulo, aumentar el marcador
                t.setVisible(false);

                //Esto es mil veces mejor que destruirlo aqui
                return;
            }

        }
        //TODO Quitar puntos porque se presiono una tecla cuando no habia nada
    }
    private void ejemplo9(){
        EfectoOndaSinoidal onda = new EfectoOndaSinoidal(-2,200,460,0);
        Principal.getChildren().add(onda.getSineWave1());
        Principal.getChildren().add(onda.getSineWave2());
    }
    @FXML
    private void btnRActivado(){
        Rectangle rect = this.makeRect(RedButton);
        checkColitions(rect);
        effect.run(RedButton,Color.RED);
    }


    @FXML
    private void btnBActivado(){
        Rectangle rect = this.makeRect(BlueButton);
        checkColitions(rect);
        effect.run(BlueButton,Color.BLUE);
    }


    @FXML
    private void btnYActivado(){
        Rectangle rect = this.makeRect(YellowButton);
        checkColitions(rect);
        effect.run(YellowButton,Color.YELLOW);
    }


    @FXML
    private void btnGActivado(){
        Rectangle rect = this.makeRect(GreenButton);
        checkColitions(rect);
        effect.run(GreenButton,Color.GREEN);
    }


    @FXML
    private void btnOActivado(){
        Rectangle rect = this.makeRect(OrangeButton);
        checkColitions(rect);
        effect.run(OrangeButton,Color.ORANGE);
    }

    private Rectangle makeRect(Button btn){
        return new Rectangle(btn.getLayoutX(), btn.getLayoutY(), btn.getWidth(), btn.getHeight());
    }
    private void fisicas(Rhomboid rb){
        long time_final = System.currentTimeMillis();
        double dt = (double) (time_final - timeInicio) ;
        timeInicio = time_final;
        if (rb.getHeight() < 300)rb.growthRhomboid(dt);
        else if (!(rb.getFloor() > Principal.getHeight() - 200)) rb.translateRhomboid(dt);
        else rb.decreaseRhomboid(dt);
    }

    private void animation(){
        if(!empezado){
            empezado = true;
            rectangle = new Rectangle(500,100,20,0);
            Principal.getChildren().add(rectangle);
        }else{

            switch (animation){
                case CRECIENDO:
                    rectangle.setHeight(rectangle.getHeight() + 5);
                    if (rectangle.getHeight() > 100) animation = AnimationMoments.TRASLADANDO;

                    break;
                case TRASLADANDO:
                    rectangle.setY(rectangle.getY() + 5);
                    if(rectangle.getY() >= Principal.getHeight() - 150) animation = AnimationMoments.DECRECIENDO;
                    break;
                case DECRECIENDO:
                    rectangle.setY(rectangle.getY() + 5);
                    rectangle.setHeight(rectangle.getHeight() - 5);
                    if (rectangle.getHeight() <= 0) animation = AnimationMoments.ACABADO;
                    break;
                case ACABADO:
                    Principal.getChildren().remove(rectangle);
                    empezado = false;
                    animation = AnimationMoments.CRECIENDO;
                    timeline.stop();
                    break;
            }
        }
    }
    /**
     * Método para crear una onda sinusoidal vertical con un desplazamiento
     * @param width Ancho del área donde se dibuja la onda
     * @param height Altura del área donde se dibuja la onda
     * @param amplitude Amplitud de la onda
     * @param frequency Frecuencia de la onda
     * @param offset Desplazamiento en radianes para la onda
     * @return Path representando la onda sinusoidal vertical
     */
    private Path createSineWave(double width, double height, double amplitude, double frequency, double offset) {
        Path path = new Path();

        // Mover al punto inicial de la onda
        path.getElements().add(new MoveTo(width / 2, 0));

        // Dibujar la onda sinusoidal vertical
        for (double y = 0; y <= height; y++) {
            double x = width / 2 + amplitude * Math.sin(frequency * y * 2 * Math.PI + offset);
            path.getElements().add(new LineTo(x, y));
        }

        path.setStrokeWidth(2);
        return path;
    }
    /**
     * Método para animar la altura del rectángulo
     * @param rect El rectángulo cuya altura se animará

     */
    private void animateRectangleHeight(Rectangle rect, Path sw1, Path sw2) {

        Timeline tm = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            rect.setY(rect.getY() + 1);
            rect.setHeight(rect.getHeight() - 1);


        }));
        tm.setCycleCount(600);
        tm.play();
        tm.setOnFinished(event -> Principal.getChildren().remove(rect));
    }
    /**
     * Método para animar la altura de la onda sinusoidal sin modificar su forma
     * @param sineWave La onda sinusoidal a animar
     * @param scaleFactor El factor de escala para la altura
     * @param duration La duración de la animación en milisegundos
     */
    private void animateSineWaveHeight(Path sineWave, double scaleFactor, long duration) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sineWave.scaleYProperty(), 1.0)),
                new KeyFrame(Duration.millis(duration), new KeyValue(sineWave.scaleYProperty(), scaleFactor))
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    /**
     * Método para animar la altura del rectángulo clip sin modificar la forma de la onda
     * @param clipRect El rectángulo clip cuya altura se animará
     * @param minHeight La altura mínima del clip
     * @param maxHeight La altura máxima del clip
     * @param duration La duración de la animación en milisegundos
     */
    private void animateClipHeight(Rectangle clipRect, DropShadow dropShadow, Glow glow,
                                   double minHeight, double maxHeight,
                                   long duration, double actualY, double finalY) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(clipRect.heightProperty(), maxHeight),
                        new KeyValue(clipRect.yProperty(),actualY),
                        new KeyValue(dropShadow.radiusProperty(),10),
                        new KeyValue(dropShadow.colorProperty(), Color.NAVY),
                        new KeyValue(glow.levelProperty(),0.0)),
                new KeyFrame(Duration.millis(duration),
                        new KeyValue(clipRect.heightProperty(), minHeight),
                        new KeyValue(clipRect.yProperty(),finalY),
                        new KeyValue(dropShadow.radiusProperty(),20),
                        new KeyValue(dropShadow.colorProperty(), Color.DARKBLUE),
                        new KeyValue(glow.levelProperty(),0.8))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }
}
