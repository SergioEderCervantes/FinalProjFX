package org.Modules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Grafo {
    private final Map<Integer, Tecla> vertices;

    public Grafo() {
        this.vertices = new HashMap<>();
    }

    public void agregarVertice(Integer id) {
        Tecla vertice = new Tecla(id);
        vertices.put(id, vertice);
    }

    public void agregarVertice(Tecla tecla){
        try{
            Tecla copia = new Tecla(tecla);
            vertices.put(copia.getTecla_ID(),copia);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void agregarArista(Integer id_origen, Integer id_Destino, double peso) {
        Tecla origen = vertices.get(id_origen);
        Tecla destino = vertices.get(id_Destino);
        if (origen != null && destino != null) {
            origen.agregarArista(destino, peso);
        } else {
            throw new IllegalArgumentException("Los vértices especificados no existen en el grafo.");
        }
    }

    public Tecla getVertice(Integer id) {
        return vertices.get(id);
    }

    public Collection<Tecla> getVertices() {
        return vertices.values();
    }
}

