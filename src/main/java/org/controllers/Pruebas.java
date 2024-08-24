package org.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Pruebas {
    @FXML
    public AnchorPane Principal;
    public Rectangle longNote;
    public Glow glowEffect;
    public DropShadow dropShadow;
    enum AnimationMoments {CRECIENDO,TRASLADANDO,DECRECIENDO,ACABADO}
    AnimationMoments animation = AnimationMoments.CRECIENDO;
    boolean empezado = false;
    Rectangle rectangle;
    Timeline timeline;
    @FXML
    private void inicialLaAnimacion(){
        this.ejemplo1();
//        this.ejemplo2();
//        this.ejemplo3();
        this.ejemplo4();
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
}
