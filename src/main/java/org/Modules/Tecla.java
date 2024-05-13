package org.Modules;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tecla {
    Color color;
    String tipo;
    double tiempoInicio;
    double tiempoFin;
    private final Integer tecla_ID;
    private final List<Arista> aristasAdyacentes;
    public static final Color[] ColoresPosibles = {Color.RED,Color.BLUE,Color.YELLOW,Color.GREEN, Color.ORANGE};
    public int numColor;
    public Tecla() {
        aristasAdyacentes = new ArrayList<>();
        numColor = (int)(Math.random() * 5);
        color = ColoresPosibles[numColor];
        tiempoInicio = 0;
        tiempoFin = 0;
        tecla_ID = 0;
        tipo = "null";
    }

    public Tecla(int id) {
        this.tecla_ID = id;
        numColor = (int)(Math.random() * 5);
        color = ColoresPosibles[numColor];
        tiempoInicio = 0;
        tiempoFin = 0;
        tipo = "null";
        this.aristasAdyacentes = new ArrayList<>();
    }

    public Tecla(Integer tecla_ID, double tiempoInicio,double tiempoFin) {
        this.tecla_ID = tecla_ID;
        this.tiempoFin = tiempoFin;
        this.tiempoInicio = tiempoInicio;
        this.tipo = "null";
        this.numColor = (int)(Math.random() * 5);
        this.color = ColoresPosibles[numColor];
        this.aristasAdyacentes = new ArrayList<>();
    }
    public Tecla(Integer tecla_ID, double tiempoInicio) {
        this.tecla_ID = tecla_ID;
        this.tiempoInicio = tiempoInicio;
        this.tiempoFin = tiempoInicio +1;
        this.tipo = "c";
        this.numColor = tecla_ID % 5;
        this.color = ColoresPosibles[numColor];
        this.aristasAdyacentes = new ArrayList<>();

    }

    public Tecla (Tecla t) throws Exception {
        if (t != null && t != this){
            this.tecla_ID = t.tecla_ID;
            this.tiempoFin = t.tiempoFin;
            this.tiempoInicio = t.tiempoInicio;
            this.numColor = t.numColor;
            this.color = t.color;
            this.aristasAdyacentes = t.aristasAdyacentes;
        }
        else {
            throw new Exception("Exception: llamada al constructor copia de un objeto nulo o intento de reasignacion");
        }
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

    public Color getColor() {
        return color;
    }

    public void agregarArista(Tecla destino, double peso) {
        Arista arista = new Arista(this, destino, peso);
        aristasAdyacentes.add(arista);
    }

    public void dibujaTecla(Graphics g, int x, int y){
        g.setColor(this.color);
        g.fillOval(x,y,20,20);
        g.setColor(Color.BLACK);
        g.drawString(this.getTecla_ID().toString(),x + 7,y + 10);
    }
}