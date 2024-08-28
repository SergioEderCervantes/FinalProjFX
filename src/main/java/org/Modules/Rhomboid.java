package org.Modules;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static org.controllers.juegoController.ColoresPosibles;

public class Rhomboid {
    Polygon rhomboid;
    DoubleProperty X1;
    DoubleProperty Y1;
    DoubleProperty X2;
    DoubleProperty Y2;
    DoubleProperty X3;
    DoubleProperty Y3;
    DoubleProperty X4;
    DoubleProperty Y4;
    int slope;
    double height;

    /**
     * Constructor de romboide que crea un poligono plano en las coordenadas (x,0) (x+width,0), y asocia
     * sus doubleProperty con listeners para posteriormente poder modificarlos de manera mas sencilla
     * @param Slope la pendiente del romboide, el cual definira el tipo de romboide que se crea
     * @param x la coordenada de x donde aparecera
     * @param width el ancho del romboide
     * @param numColor el color del romboide
     */
    public Rhomboid(int Slope, double x,int width, int numColor) {
        rhomboid = new Polygon();
        X1 = new SimpleDoubleProperty(x);
        Y1 = new SimpleDoubleProperty(155);
        X2 = new SimpleDoubleProperty(x + width);
        Y2 = new SimpleDoubleProperty(155);
        X3 = new SimpleDoubleProperty(x+ width);
        Y3 = new SimpleDoubleProperty(155);
        X4 = new SimpleDoubleProperty(x);
        Y4 = new SimpleDoubleProperty(155);
        slope = Slope;
        height = 0;
        rhomboid.getPoints().addAll(
                X1.get(), Y1.get(),
                X2.get(), Y2.get(),
                X3.get(), Y3.get(),
                X4.get(), Y4.get()
        );

        X1.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(0, newVal.doubleValue()));
        Y1.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(1, newVal.doubleValue()));
        X2.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(2, newVal.doubleValue()));
        Y2.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(3, newVal.doubleValue()));
        X3.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(4, newVal.doubleValue()));
        Y3.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(5, newVal.doubleValue()));
        X4.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(6, newVal.doubleValue()));
        Y4.addListener((obs, oldVal, newVal) -> rhomboid.getPoints().set(7, newVal.doubleValue()));

        rhomboid.setFill(ColoresPosibles[numColor]);
        rhomboid.setStroke(Color.TRANSPARENT);
        //TODO Agregar efectos bonitos al rhomboid
    }

    public Rhomboid (Polygon rhomboid){
        this.rhomboid = rhomboid;
    }

    public void growthRhomboid(double dt){
        double factorY = dt/4;
        double factorX = this.getFactorX(factorY);
        X3.set(X3.get() + factorX);
        X4.set(X4.get() + factorX);
        Y3.set(Y3.get() + factorY);
        Y4.set(Y4.get() + factorY);
        height += factorY;
    }
    public void translateRhomboid(double dt){
        double factorY = dt/4;
        double factorX = this.getFactorX(factorY);
        X1.set(X1.get() + factorX);
        Y1.set(Y1.get() + factorY);
        X2.set(X2.get() + factorX);
        Y2.set(Y2.get() + factorY);
        X3.set(X3.get() + factorX);
        Y3.set(Y3.get() + factorY);
        X4.set(X4.get() + factorX);
        Y4.set(Y4.get() + factorY);
    }

    public void  decreaseRhomboid(double dt){
        double factorY = dt/4;
        double factorX = this.getFactorX(factorY);
        X3.set(X3.get() - factorX);
        Y3.set(Y3.get() - factorY);
        X4.set(X4.get() - factorX);
        Y4.set(Y4.get() - factorY);
        X1.set(X1.get() + factorX);
        Y1.set(Y1.get() + factorY);
        X2.set(X2.get() + factorX);
        Y2.set(Y2.get() + factorY);

        height -= 2*factorY;
    }
    private double getFactorX(double dt){
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
    public Polygon getRhomboid(){
        return rhomboid;
    }
    public double getHeight(){
        return height;
    }
    public double getFloor(){
        return Y3.get();
    }


}
