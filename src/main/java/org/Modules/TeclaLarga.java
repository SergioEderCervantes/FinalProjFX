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
    double tiempoFin;
    int numColor;
    boolean largerThanScreen;

    public Circle getCircle(){
        return circulo;
    }
    public TeclaLarga(int numColor, double x, double tiempoFin, boolean largerThanScreen) {
        this.loadState1(numColor,x);
        this.getChildren().add(state1);
        this.x = x;
        this.numColor = numColor;
        this.tiempoFin = tiempoFin;
        this.largerThanScreen = largerThanScreen;
    }

    public double getTiempoFin(){
        return tiempoFin;
    }

    public void setEstado(ESTADOS estado){
        this.estado = estado;
    }

    public ESTADOS getEstado(){
        return estado;
    }

    public Rhomboid getRhomboid(){
        return this.rb;
    }

    public boolean isLargerThanScreen(){
        return largerThanScreen;
    }
    private void loadState1(int numColor, double x){
        state1 = new Group();
        circulo = new Circle(x,155,20,ColoresPosibles[numColor]);
        circulo.setStrokeWidth(3);
        circulo.setStroke(ColoresClaros[numColor]);
        circulo.setStrokeType(StrokeType.CENTERED);
        rb = new Rhomboid(numColor - 2, x - 10,20,numColor);
        state1.getChildren().addAll(rb.getRhomboid(), circulo);

    }

    private void loadState2(){
        state2 = new Group();
        sineWaves = new EfectoOndaSinoidal(rb.slope,rb.height,this.x,this.numColor);
        sineWaves.turnOnEffects();
        state2.getChildren().addAll(sineWaves.sineWave1,sineWaves.sineWave2);
    }

    public boolean isState1(){
        return isState1;
    }

    public void fisica(double dt){
        if (estado != ESTADOS.DECREASE && estado != ESTADOS.STOPPED) juegoController.fisicaCirculo(circulo, dt);
        switch (estado) {
            case GROWTH:
                rb.growthRhomboid(dt);
                break;
            case TRANSITION:
                rb.translateRhomboid(dt);
                break;
            case DECREASE:
                rb.decreaseRhomboid(dt);
                break;
        }
    }


    public void toggleState(){
        this.rb.getRhomboid().setFill(ColoresClaros[numColor]);
        isState1 = !isState1;
    }

    public void clearAll(){
        this.getChildren().clear();
    }
}
