package org.Modules;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tecla {

    double tiempoInicio;
    double tiempoFin;
    private final Integer tecla_ID;
    private final List<Arista> aristasAdyacentes;
    public int numColor;
    public Tecla() {
        aristasAdyacentes = new ArrayList<>();
        numColor = (int)(Math.random() * 5);
        tiempoInicio = 0;
        tiempoFin = 0;
        tecla_ID = 0;

    }

    public Tecla(int id) {
        this.tecla_ID = id;
        numColor = (int)(Math.random() * 4);
        tiempoInicio = 0;
        tiempoFin = 0;

        this.aristasAdyacentes = new ArrayList<>();
    }


    public Tecla (Tecla t) throws Exception {
        if (t != null && t != this){
            this.tecla_ID = t.tecla_ID;
            this.tiempoFin = t.tiempoFin;
            this.tiempoInicio = t.tiempoInicio;
            this.numColor = t.numColor;
            this.aristasAdyacentes = t.aristasAdyacentes;

        }
        else {
            throw new Exception("Exception: llamada al constructor copia de un objeto nulo o intento de reasignacion");
        }
    }

    public Tecla (Integer tecla_ID, double tiempoInicio, int numColor) {
        this.tecla_ID = tecla_ID;
        this.tiempoInicio = tiempoInicio;
        this.numColor = numColor;
        this.aristasAdyacentes = new ArrayList<>();
    }

    public Integer getTecla_ID() {
        return tecla_ID;
    }

    public double getTiempoInicio(){
        return tiempoInicio;
    }

    public List<Arista> getAristasAdyacentes() {
        return aristasAdyacentes;
    }

    public double getDtSiguiente() {
        return aristasAdyacentes.get(0).peso();
    }

    public int getNumAristasAdyacentes(){
        return aristasAdyacentes.size();
    }

    public void agregarArista(Tecla destino, double peso) {
        Arista arista = new Arista(this, destino, peso);
        aristasAdyacentes.add(arista);
    }

    public int getNumColor() {
        return numColor;
    }

    public Tecla getTeclaSig(){
        return this.aristasAdyacentes.get(0).destino();
    }


}