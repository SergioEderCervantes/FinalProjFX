package org.controllers;



import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.Modules.Arista;
import org.Modules.Song;
import org.Modules.Tecla;
import org.Modules.TestGraph;

public class PrimaryController{

    private Pane root;
    private final Song prueba;
    public Timeline timeline;
    private Tecla aux;
    private final ArrayList<Circle> teclasEnPantalla = new ArrayList<>();
    public static final Color[] ColoresPosibles = 
            {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.ORANGE};
    public PrimaryController(Pane root) {
        this.prueba = TestGraph.RealizarTest();
        this.timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> print()));
        this.root = root;
        this.aux = prueba.getTeclas_pulsadas().getVertice(0);
        this.timeline.setCycleCount((int)prueba.getDuracion());
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    private void print() {
        
        int xDef = 0;
        root.getChildren().clear();
        Tecla lastTecla = null;

        //Primero desplaza todas las que ya existen hacia abajo
        for (Circle circulo : teclasEnPantalla){
            circulo.setCenterY(circulo.getCenterY() + circulo.getRadius()*3);
            if (circulo.getCenterY() < 600){
                root.getChildren().add(circulo);
            }
        }
        //Eliminamos del ArrayList las teclas que ya no se ven
        teclasEnPantalla.removeIf(circle -> circle.getCenterY() > 600);
        
        //Agregamos las nuevas teclas que deben de aparecer
        if (aux.getNumAristasAdyacentes() != 0){
            for (Arista i : aux.getAristasAdyacentes()){

                switch (i.destino().numColor){
                    case 0:
                        xDef = 50;
                        break;
                    case 1:
                        xDef = 100;
                        break;
                    case 2:
                        xDef = 150;
                        break;
                    case 3:
                        xDef = 200;
                        break;
                    case 4:
                        xDef = 250;
                        break;
                    case 5:
                        xDef = 300;
                        break;
                }
                Circle circle = new Circle(xDef,30,20,ColoresPosibles[i.destino().numColor]);
                root.getChildren().add(circle);
                teclasEnPantalla.add(circle);
                lastTecla = i.destino();
            }
            aux = lastTecla;
        }
    }
}
