package org.Modules;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.List;

public class FileUtils {

    public static void MigrarSong(){
        final ArrayList<String> lines = new ArrayList<>();
        final String query = "SELECT * FROM song WHERE idSong = 11";
        final String query2 = "SELECT numColor, tiempoInicio FROM teclas WHERE idSong = 11";
        final MySqlConn conn = new MySqlConn();
        conn.consult(query);
        if (conn.rs != null){
            try {
                lines.add(conn.rs.getString("idSong"));
                lines.add(conn.rs.getString("name"));
                lines.add(conn.rs.getString("sourcePath"));
                lines.add(conn.rs.getString("duracion"));
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        conn.consult(query2);
        int n = 0;
        if (conn.rs != null){
            try{
                conn.rs.last();
                n = conn.rs.getRow();
                conn.rs.first();

                for (int i = 0; i < n; i++){
                    String aux = conn.rs.getString("numColor") +
                            "-" + conn.rs.getString("tiempoInicio");
                    lines.add(aux);
                    conn.rs.next();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        final String targetRoot = "src/main/resources/songs/03";
        try{
            Files.write(Paths.get(targetRoot),lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.closeRsStmt();

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
            int id = Integer.parseInt(idSong);
            String strPath = "src/main/resources/songs/" + idSong;
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

    public static void main(String[] args) {
        MigrarSong();

    }

}
