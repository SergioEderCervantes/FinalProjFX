package org.Modules;

import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import org.controllers.juegoController;
import static org.controllers.juegoController.ColoresClaros;
import static org.controllers.juegoController.ColoresPosibles;

public class TeclaLarga extends Region {
    boolean isState1 = true;
    Group state1;
    Group state2;
    ESTADOS estado = ESTADOS.GROWTH;
    Circle circulo;
    Rhomboid rb;
    EfectoOndaSinoidal sineWaves;
    double x;
    double totalHeight;
    int numColor;

    public Circle getCircle(){

        return circulo;
    }
    public TeclaLarga(int numColor, double x, double totalHeight) {
        this.loadState1(numColor,x);
        this.getChildren().add(state1);
        //TODO lo largo del romboide y de las ondas esta dado por el delta t total de la tecla / 4
        this.x = x;
        this.numColor = numColor;
        this.totalHeight = totalHeight;
    }

    private void loadState1(int numColor, double x){
        state1 = new Group();
        circulo = new Circle(x,155,20,ColoresPosibles[numColor]);
        circulo.setStrokeWidth(3);
        circulo.setStroke(ColoresClaros[numColor]);
        circulo.setStrokeType(StrokeType.CENTERED);
        rb = new Rhomboid(numColor - 2, x - 10,20,numColor);
        state1.getChildren().addAll(circulo, rb.getRhomboid());
    }

    private void loadState2(){
        state2 = new Group();
        sineWaves = new EfectoOndaSinoidal(rb.slope,this.totalHeight,this.x,this.numColor);
        sineWaves.turnOnEffects();
        state2.getChildren().addAll(sineWaves.sineWave1,sineWaves.sineWave2);
    }

    public void fisicaTeclaLarga(double dt){
        //TODO cuando se integre en el juego principal, es mejor que las condiciones de cambio de estado esten afuera
        // de este metodo
        if(isState1){
            if(estado != ESTADOS.DECREASE && estado != ESTADOS.STOPPED) juegoController.fisicaCirculo(circulo,dt);
            switch(estado){
                case GROWTH:
                    rb.growthRhomboid(dt);
                    if (rb.getHeight() > this.totalHeight) estado = ESTADOS.TRANSITION;
                    break;
                case TRANSITION:
                    rb.translateRhomboid(dt);
                    if(rb.getFloor() > 700) estado = ESTADOS.DECREASE;
                    break;
                case DECREASE:
                    rb.decreaseRhomboid(dt);
//                    this.toggleState();
                    if (rb.getHeight() <= 0) estado = ESTADOS.STOPPED;
                    break;
            }
        }else {
//            switch(estado){
//                case GROWTH:
//                    sineWaves.growthWave(dt);
//                    if(sineWaves.getHeight() > this.totalHeight) estado = ESTADOS.TRANSITION;
//                    break;
//                case TRANSITION:
//                    sineWaves.translateWave(dt);
//                    if(sineWaves.getFloor() > 550) estado = ESTADOS.DECREASE;
//                    break;
//                case DECREASE:
//                    sineWaves.decreaseWave(dt);
//                    if(sineWaves.getHeight() <= 0) estado = ESTADOS.STOPPED;
//                    break;
//            }
        }
    }


    public void toggleState(){
        this.getChildren().clear();
        if (isState1){
            if (state2 == null) this.loadState2();
            this.getChildren().add(state2);
        }
        else
            this.getChildren().add(state1);
        isState1 = !isState1;
    }
}
