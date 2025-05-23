package org.Modules;


import java.util.ArrayList;

public class Song {
    private String name;
    private String ruta;
    private double duracion;
    Grafo teclas_pulsadas;

    public Song() {
        this.name = "";
        this.ruta = "";
        this.duracion = 0;
        this.teclas_pulsadas = new Grafo();
    }

    public Song(String name, String ruta, double duracion) {
        this.name = name;
        this.ruta = ruta;
        this.duracion = duracion;
        this.teclas_pulsadas = new Grafo();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public Grafo getTeclas_pulsadas() {
        return teclas_pulsadas;
    }

    public void setTeclas_pulsadas(Grafo teclas_pulsadas) {
        this.teclas_pulsadas = teclas_pulsadas;
    }

    public void ConstruirGrafo(ArrayList<Tecla> teclas){
        teclas_pulsadas.agregarVertice(0);  //Agregando Vertice inicial
        //Conectando los vertices en si
        ArrayList<Tecla> teclas_siguientes;
        boolean band = true;
        int ID_ANTERIOR = 0,LAST_ID = 0;
        double TIEMPO_ANTERIOR = 0;
        while(band){
            teclas_siguientes = this.nextTeclas(teclas);
            band = !teclas_siguientes.isEmpty();
            //El tiempo entre la ultima y la siguiente
            if (band){
                //teclas_siguientes.sort(null);     //nsc ni jala este pedo
                // OJO: realmente no se exactamente como es que se esta ordenando esto
                    //Puede causar errores, sobretodo cuando el arrayList viene de la base de datos o cuando los IDs de
                    //Las teclas no estan en orden en si

                double dt = teclas_siguientes.get(0).tiempoInicio - TIEMPO_ANTERIOR;
                //Agregando los vertices escogidos en el grafo

                for (Tecla siguiente : teclas_siguientes) {

                    teclas_pulsadas.agregarVertice(siguiente);
                    teclas_pulsadas.agregarArista(ID_ANTERIOR, siguiente.getTecla_ID(),dt);
                    LAST_ID = siguiente.getTecla_ID();
                    TIEMPO_ANTERIOR = siguiente.tiempoInicio;
                }

                ID_ANTERIOR = LAST_ID;
                teclas.removeIf(teclas_siguientes::contains);
            }
        }
        teclas_pulsadas.agregarVertice(Integer.MAX_VALUE);
        teclas_pulsadas.agregarArista(ID_ANTERIOR,Integer.MAX_VALUE,duracion - TIEMPO_ANTERIOR);
    }
    private ArrayList<Tecla> nextTeclas(ArrayList<Tecla> teclas){
        ArrayList<Tecla> teclas_siguientes = new ArrayList<>();
        double menor = 100000000;
        for (Tecla tecla : teclas) {
            if (tecla.tiempoInicio < menor) {
                //Si encuentra un valor menor, se limpia la lista y su nuevo elemento es i
                teclas_siguientes.clear();
                teclas_siguientes.add(tecla);
                menor = tecla.tiempoInicio;
            } else {
                if (tecla.tiempoInicio == menor) {
                    //Si encuentra un valor igual, se agrega a la lista porque aparecen al mismo tiempo
                    teclas_siguientes.add(tecla);
                }
            }
        }
        return teclas_siguientes;
    }
}