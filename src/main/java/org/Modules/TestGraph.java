package org.Modules;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class TestGraph {
    public static Song RealizarTest(){
        ArrayList<Tecla> teclas = new ArrayList<>();
        Song test = new Song("Prueba1", "unasigned path",200);
        double time = 0;
        for (int i = 0; i < 200; i++) {
            Tecla aux = new Tecla(i+1, time);
            teclas.add(aux);
            time ++;
//            double factorRand = Math.random();
            //Si el Math.random es menor a 0.2, entonces mejor setearlo como 0, si no, hacerlo un poco mas grande
//            factorRand = factorRand < 0.5 ? 0 : factorRand * 3;
//            time += factorRand;
        }
        test.ConstruirGrafo(teclas);
        return test;
    }
}
