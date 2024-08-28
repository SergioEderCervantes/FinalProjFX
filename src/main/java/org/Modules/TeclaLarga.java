package org.Modules;

import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import org.controllers.juegoController;

import static org.controllers.juegoController.ColoresPosibles;


public class TeclaLarga extends Region {
    boolean isState1 = true;
    Group state1;
    Group state2;
    ESTADOS estado = ESTADOS.GROWTH;
    Circle circulo;
    Rhomboid rb;
    public TeclaLarga(int numColor, double x) {
        state1 = new Group();
        state2 = new Group();
        circulo = new Circle(x,155,20,ColoresPosibles[numColor]);
        rb = new Rhomboid(numColor - 2, x - 10,20,numColor);
        state1.getChildren().addAll(circulo, rb.getRhomboid());

        this.getChildren().add(state1);
    }

    public void fisicaTeclaLarga(double dt){
        if(isState1){
            if(estado != ESTADOS.DECREASE && estado != ESTADOS.STOPPED) juegoController.fisicaCirculo(circulo,dt);
            switch(estado){
                case GROWTH:
                    rb.growthRhomboid(dt);
                    if (rb.getHeight() > 200) estado = ESTADOS.TRANSITION;
                    break;
                case TRANSITION:
                    rb.translateRhomboid(dt);
                    if(rb.getFloor() > 550) estado = ESTADOS.DECREASE;
                    break;
                case DECREASE:
                    rb.decreaseRhomboid(dt);
                    if (rb.getHeight() <= 0) estado = ESTADOS.STOPPED;
                    break;
            }
        }
    }

    public ESTADOS getEstado() {
        return this.estado;
    }

    public void setEstado(ESTADOS estado) {
        this.estado = estado;
    }
    public Rhomboid getRb(){
        return this.rb;
    }
}
