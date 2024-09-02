package org.Modules;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.Blend;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


import static org.controllers.juegoController.ColoresPosibles;

public class EfectoOndaSinoidal {
    Rectangle clip1;
    Rectangle clip2;
    Path sineWave1;
    Path sineWave2;
    int slope;
    double totalHeight;
    double x;
    DropShadow dsEffect;
    Glow glowEffect;
    Timeline effectTimeline;
    final double Ylayout = 550;

    public EfectoOndaSinoidal(int slope, double totalHeight, double x, int numColor) {
        this.totalHeight = totalHeight;
        this.slope = slope;
        int amplitude = 12;
        //Para acomodar en x de manera correcta, se debe de tomar en cuenta el slope y la amplitud, si se cambia la amplitud
        //de las ondas es necesario modificar el factor
        this.x = x - 15 + (amplitude * slope);
        sineWave1 = createSineWave(60,totalHeight,amplitude,0.005,0.0);
        sineWave2 = createSineWave(60,totalHeight,amplitude,0.005,Math.PI);
        dsEffect = new DropShadow();
        glowEffect = new Glow(0.0);


        //Como al principio los efectos no se veran se crean en altura 0, ya despues crece esta altura para que se vaya
        //el efecto
        //esto va a fallar despues
        clip1 = new Rectangle(60,totalHeight);
        clip2 = new Rectangle(60,totalHeight);
        configureSineWave(sineWave1,this.x,numColor,dsEffect,glowEffect,clip1,slope);
        configureSineWave(sineWave2,this.x,numColor,dsEffect,glowEffect,clip2,slope);

    }

    public Path getSineWave1() {
        return sineWave1;
    }

    public Path getSineWave2() {
        return sineWave2;
    }

    public Timeline getEffectTimeline(){
        return effectTimeline;
    }

    public double getTotalHeight(){
        return totalHeight;
    }

    public double getHeight(){
        return clip1.getHeight();
    }

    public double getFloor(){
        return sineWave1.getLayoutY() + clip1.getHeight();
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
    private static Path createSineWave(double width, double height, double amplitude, double frequency, double offset) {
        Path path = new Path();

        // Mover al punto inicial de la onda
        path.getElements().add(new MoveTo(width / 2, 0));
        // Dibujar la onda sinusoidal vertical
        for (double y = 0; y <= height; y++) {
            double x = width / 2 + amplitude * Math.sin(frequency * y * 2 * Math.PI + offset);
            path.getElements().add(new LineTo(x, y));
        }
        return path;
    }

    /**
     * Funcion que, ya creada la onda sinoidal y el rectangulo, la acomoda con respecto a x y a su angulo,
     * crea los rectangulos que contendran el clip y le da los estilos correspondientes
     * @param x la x donde se ubicara
     * @param numColor el color de la onda
     */
    private void configureSineWave(Path sineWave, double x, int numColor, DropShadow dsEffect,
                                   Glow gEffect, Rectangle clip, int slope){
        sineWave.setLayoutX(x);
        sineWave.setLayoutY(Ylayout - totalHeight);
        sineWave.setStrokeWidth(2);
        sineWave.setStroke(ColoresPosibles[numColor]);

        Blend combinedEffect = new Blend();
        combinedEffect.setBottomInput(dsEffect);
        combinedEffect.setTopInput(gEffect);



        sineWave.setClip(clip);
        sineWave.setEffect(combinedEffect);
        //Darle al clip y a la onda la inclinacion correspondiente
        double rotateAngle = 180;
        switch (slope){
            case -2:
                rotateAngle += Math.toDegrees(Math.atan((double) 1 /6));
                break;
            case -1:
                rotateAngle += Math.toDegrees(Math.atan((double) 1 /12));
                break;
            case 1:
                rotateAngle += -Math.toDegrees(Math.atan((double) 1 /12));
                break;
            case 2:
                rotateAngle += -Math.toDegrees(Math.atan((double) 1 /6));
                break;
        }
        double centerX = sineWave.getBoundsInParent().getWidth() / 2;
        double centerY = sineWave.getBoundsInParent().getHeight() / 2;
        System.out.println(rotateAngle);
        Rotate rotate = new Rotate(rotateAngle, centerX, centerY);
        sineWave.getTransforms().add(rotate);
    }

    public void growthWave(double dt){
        double factor = dt / 4;
        clip1.setHeight(clip1.getHeight() + factor);
        clip2.setHeight(clip2.getHeight() + factor);
    }

    public void translateWave(double dt){
        double factor = dt / 4;
        sineWave1.setLayoutY(sineWave1.getLayoutY() + factor);
        sineWave2.setLayoutY(sineWave2.getLayoutY() + factor);
    }

    public void decreaseWave(double dt){
        double factor = dt / 4;
        clip1.setHeight(clip1.getHeight() - factor);
        clip2.setHeight(clip2.getHeight() - factor);
    }

    public static double getFactorX(double dt, int slope){
        double factorX = 0;
        switch (slope){
            case -2:
                factorX = -dt/6;
                break;
            case -1:
                factorX = -dt/12;
                break;
            case 1:
                factorX = dt/12;
                break;
            case 2:
                factorX = dt/6;
                break;

        }
        return factorX;
    }

    /**
     * Configura e inicia la timeline que crea los efectos visuales de las odnas, se repite indefinidamente
     * y dura dos segundos, tiene el setAutoReverse para que se vea fluido el cambio, cualquier cosa que se quiera para
     * animar los efectos visuales de la onda se tiene que cambiar aqui
     */
    public void turnOnEffects(){
        this.effectTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(dsEffect.radiusProperty(),10),
                        new KeyValue(dsEffect.colorProperty(), Color.NAVY),
                        new KeyValue(glowEffect.levelProperty(),0.0)),
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(dsEffect.radiusProperty(),20),
                        new KeyValue(dsEffect.colorProperty(), Color.DARKBLUE),
                        new KeyValue(glowEffect.levelProperty(),1.2))
        );
        this.effectTimeline.setCycleCount(Timeline.INDEFINITE);
        this.effectTimeline.setAutoReverse(true);
        this.effectTimeline.play();
    }
}
