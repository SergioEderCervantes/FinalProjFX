package org.Modules;

import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileUtils {
    private static final String directoryPath = "src/main/resources/songs/";



    public static Pair<String,String>[] loadSongsNames(){
        Pair<String,String>[] songs = null;
        String strPath = directoryPath + "dictionary.txt";
        try{
            Path path = Paths.get(strPath);
            if (!Files.exists(path)) throw new IOException("El diccionario no existe!");
            List<String> lines = Files.readAllLines(path);
            songs = new Pair[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                String [] aux = lines.get(i).split("-");
                songs[i] = new Pair<>(aux[0],aux[1]);
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return songs;
    }

    public static String getIDByName(String name) throws IOException{
        String strPath = directoryPath + "dictionary.txt";
        Path path = Paths.get(strPath);
        if (!Files.exists(path)) throw new IOException("El diccionario no existe!");
        List<String> lines = Files.readAllLines(path);
        for (int i = 0; i < lines.size(); i++) {
            String[] aux = lines.get(i).split("-");
            if (aux[1].equals(name)) return aux[0];
        }
        int aux = lines.size() + 1;
        return aux < 10 ? "0" + String.valueOf(aux) : String.valueOf(aux);
    }
    public static void saveSong(boolean editado, String songName, String songPath,
                                 ObservableList<String> items) throws IOException {
        final String idFile = FileUtils.getIDByName(songName);
        final String targetFile = directoryPath + idFile + ".txt";
        final ArrayList<String> lines = new ArrayList<>();

        if(editado){
            //borramos lo que ya existe
            Files.deleteIfExists(Paths.get(targetFile));
        } else{
            //Agregamos una nueva entrada al diccionario
            String dictionary = directoryPath + "dictionary.txt";
            String aux = idFile + "-" + songName;
            Files.write(Paths.get(dictionary), Collections.singletonList(aux), StandardOpenOption.APPEND);
        }

        lines.add(idFile);
        lines.add(songName);
        lines.add(songPath);

        for (String item : items) {
            String[] aux = item.split("-");
            String numColor = "0";
            switch (aux[0]) {
                case "Rojo":
                    break;
                case "Azul":
                    numColor = "1";
                    break;
                case "Amarillo":
                    numColor = "2";
                    break;
                case "Verde":
                    numColor = "3";
                    break;
                case "Naranja":
                    numColor = "4";
                    break;
            }
            String tiempoInicio = aux[1];
            lines.add(numColor + "-" + tiempoInicio);
        }
        Files.write(Paths.get(targetFile), lines);

    }
    public static void deleteSong(String id) {
        String strPath = directoryPath + id + ".txt";
        try{
            Files.deleteIfExists(Paths.get(strPath));
            strPath = directoryPath + "dictionary.txt";
            List<String> lines = Files.readAllLines(Paths.get(strPath));
            for (int i = 0; i < lines.size(); i++) {
                String[] aux = lines.get(i).split("-");
                if (aux[0].equals(id)) lines.remove(i);
            }
            Files.write(Paths.get(strPath), lines);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Funcion que carga el contenido del archivo con el idProvisto en un objeto Song y lo devuelve
     * @param idSong el id de la cancion seleccionada
     * @return El objeto Song con todos los datos de la cancion
     */
    public static Song loadSong(String idSong){
        Song song = new Song();
        //Validacion del ID y obtencion de los datos del archivo
        ArrayList<String> data = null;
        try {
            int id = Integer.parseInt(idSong);  //could be better
            String strPath = directoryPath + idSong + ".txt";
            Path path = Paths.get(strPath);
            if (!Files.exists(path)){ throw new Exception("EL ARCHIVO NO EXISTE");}
            List<String> lines = Files.readAllLines(path);
            data = new ArrayList<>(lines);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(idSong);
            System.exit(1);
        }
        //setteo del objeto sond
        song.setName(data.get(1));
        song.setRuta(data.get(2));
        song.setDuracion(Double.parseDouble(data.get(3)));
        ArrayList<Tecla> teclas = new ArrayList<>();
        for (int i = 4; i < data.size()-4; i++) {
            String []partes = data.get(i).split("-");
            teclas.add(new Tecla(i-3, Double.parseDouble(partes[1]), Integer.parseInt(partes[0])));
        }
        song.ConstruirGrafo(teclas);
        return song;
    }

    /**
     * Metodo que carga un MediaPlayer desde los archivos con el id de la cancion
     * @param idSong id de la cancion seleccionada
     * @return MediaPlayer funcional para su uso
     */
    public static MediaPlayer loadMP(String idSong){
        final String strPath = directoryPath + idSong + ".txt";
        MediaPlayer target = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get(strPath));
            String mediaPath = lines.get(2);
            String mediaURL = new File(mediaPath).toURI().toString();
            Media media = new Media(mediaURL);
            target = new MediaPlayer(media);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return target;
    }

    /**
     * Metodo para cargar todas las teclas relacionadas con el idsong que se pasa
     * @param idSong el id de la cancion seleccionada
     * @return ArrayList listo para usarse
     */
    public static ArrayList<String> loadTL(String idSong){
        ArrayList<String> target = new ArrayList<>();
        final String strPath = directoryPath + idSong + ".txt";
        try{
            List<String> lines = Files.readAllLines(Paths.get(strPath));
            lines.subList(0,4).clear();
            target = new ArrayList<>(lines);
        } catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println(idSong);
        }
        return target;

        //El formato de cada elemento del arrayList debe de ser:
        //NumColor-TiempoInicio
    }

    public static void main(String[] args) {
    }

}
