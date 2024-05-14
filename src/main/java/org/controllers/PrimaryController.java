package org.controllers;



import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.Modules.*;

public class PrimaryController{
    public static final Color[] ColoresPosibles =
            {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE};

    private Pane root;
    private final Song prueba;
    public Timeline timeline;
    private Tecla aux;
    private final ArrayList<Circle> teclasEnPantalla = new ArrayList<>();
    private long t_inicio;
    //Dicta la frecuencia de refresco de la animacion, mientas menor sea el numero mayor seran los fps del juego
    //Aproximadamente, 50 dan 20fps, 33 son 30 fps, 16 son 60fps
    private final int REPETITION_MILIS = 16;


    public PrimaryController(Pane root) {
        this.prueba = TestGraph.RealizarTest();
        this.timeline = new Timeline(new KeyFrame(Duration.millis(REPETITION_MILIS), event -> print()));    //Animacion
        this.root = root;
        this.aux = prueba.getTeclas_pulsadas().getVertice(0);
        this.timeline.setCycleCount(Timeline.INDEFINITE);   //Ciclos que durara la animacion
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public void setT_inicio(long t_inicio) {
        this.t_inicio = t_inicio;
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
                    root.getChildren().remove(circulo);
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
                    root.getChildren().add(circle);
                    teclasEnPantalla.add(circle);
                    lastTecla = i.destino();
                }
                aux = lastTecla;
            }
        }
    }
}
