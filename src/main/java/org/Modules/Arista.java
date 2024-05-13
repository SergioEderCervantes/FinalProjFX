package org.Modules;

public class Arista {
    private final Tecla origen;
    private final Tecla destino;
    private final double peso;

    public Arista(Tecla origen, Tecla destino, double peso){
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public Tecla origen() {
        return origen;
    }


    public Tecla destino() {
        return destino;
    }


    public double peso() {
        return peso;
    }
}