package org.Modules;


import java.util.ArrayList;

public class TestGraph {
    public static Song RealizarTest(){
        ArrayList<Tecla> teclas = new ArrayList<>();
        Song test = new Song("Vamonos a marte", "D:/files/UNI/Cuarto Semetre/Progra III/ProyectoFinal/FinalProjFX/src/main/resources/songs/vamonos_a_marte.mp3",200);
        teclas.add(new Tecla(1,666,0));
        teclas.add(new Tecla(2,1000,1));
        teclas.add(new Tecla(3,1333,2));

        teclas.add(new Tecla(4,2333,1));
        teclas.add(new Tecla(5,2666,2));
        teclas.add(new Tecla(6,3000,3));

        teclas.add(new Tecla(7,4000,2));
        teclas.add(new Tecla(8,4333,1));
        teclas.add(new Tecla(9,4666,0));
        teclas.add(new Tecla(10,5333,0));
        teclas.add(new Tecla(11,5666,1));
        teclas.add(new Tecla(12,6000,2));
        teclas.add(new Tecla(13,6333,3));

        teclas.add(new Tecla(14,7333,2));
        teclas.add(new Tecla(15,7333,3));
        teclas.add(new Tecla(16,8333,4));

        teclas.add(new Tecla(17,9333, 2));
        teclas.add(new Tecla(18,9333, 3));
        teclas.add(new Tecla(19,10000, 4));

        teclas.add(new Tecla(20,11000, 3));
        teclas.add(new Tecla(21,11000, 4));

        teclas.add(new Tecla(22,13500, 1));
        teclas.add(new Tecla(23,13500, 2));
        teclas.add(new Tecla(24,14000, 3));

        teclas.add(new Tecla(25,14666,1));
        teclas.add(new Tecla(26,14666, 2));
        teclas.add(new Tecla(27,15500, 3));






        test.ConstruirGrafo(teclas);
        return test;
    }
}
