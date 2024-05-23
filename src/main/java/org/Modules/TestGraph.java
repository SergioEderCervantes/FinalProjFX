package org.Modules;


import java.util.ArrayList;

public class TestGraph {
    public static Song RealizarTest(){
        ArrayList<Tecla> teclas = new ArrayList<>();
        Song test = new Song("Vamonos a marte", "../../resources/songs/vamonos_a_marte.mp3",200);
        double time = 0;
        for (int i = 0; i < 200; i++) {
            Tecla aux = new Tecla(i+1, time * 1000);
            teclas.add(aux);

            double factorRand = Math.random();
            //Si el Math.random es menor a 0.2, entonces mejor setearlo como 0, si no, hacerlo un poco mas grande
            factorRand = factorRand < 0.2 ? 0 : factorRand;
            time += factorRand;
        }
        test.ConstruirGrafo(teclas);
        return test;
    }
}
