package org.Modules;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class Conexion_UDP extends Thread{

    private static final int SERVER_PORT = 4545;
    private static final int CLIENT_PORT = 5558;

    DatagramSocket socket;
    DatagramPacket packet;
    private volatile boolean alive;
    private volatile boolean goFlag;
    private InetAddress IPAddress;
    private InetAddress IPTarget;
    private int PORT;
    private int TARGET_PORT;
    private String lastRecieved;
    /**
     * Creacion del socket para la comunicacion de un dispositivo a solo otro dispositivo
     * @param name  Nombre del hilo
     * @param IPAddress Direccion ip de este dispositivo
     * @param IPTarget  Direccion ip del dispositivo de destino
     * @param isServer boleano que dicta si este objeto se usara como servidor o como cliente (Para los puertos)
     */
    public Conexion_UDP(String name, String IPAddress, String IPTarget, boolean isServer) {
        super(name);
        try {
            this.IPAddress = InetAddress.getByName(IPAddress);
            this.IPTarget = InetAddress.getByName(IPTarget);
            this.PORT = (isServer ? SERVER_PORT : CLIENT_PORT);
            this.TARGET_PORT = (isServer ? CLIENT_PORT : SERVER_PORT);
            this.alive = true;
            this.goFlag = false;
            this.socket = new DatagramSocket(this.PORT,this.IPAddress);
            start();
        }catch (UnknownHostException e) {
            System.err.println("ERROR Los ip's: "+e.getMessage());
        } catch (SocketException e) {
            System.err.println("Error en Socket: "+e.getMessage());
        }
    }

    public String getLastRecieved() {
        return lastRecieved;
    }


    /**
     * Funcion run que se mantiene pendiente de recibir paquetes
     */
    @Override
    public void run() {

        try {
            while (alive) {
                synchronized (this) {
                    while (!goFlag){
                        wait();
                    }
                }
                lastRecieved = recievePacket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }   catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }

    }

    /**
     * Metodo que maneja la entrada de un paquete
     */
    private String recievePacket() throws IOException, InterruptedException {
        DatagramPacket recibido = new DatagramPacket(new byte[1024], 1024);
        try {
            socket.receive(recibido);
        } catch (SocketException e) {
            if (!alive) {
                return ""; // Si el socket se cierra mientras recibe, simplemente retornar
            }
            throw e;
        }

        return new String(recibido.getData()).split("\0")[0];
        //TODO logica para la validacion y manejo del dato
//        sendResponse(recibido);

    }

    /**
     * Metodo para regresar una respuesta a un paquete recibido (Manda la hora actual del sistema
     */
    private void sendResponse(DatagramPacket recibido) throws IOException {
        String message = "HORA DEL SERVIDOR " + new Date() + "\0";
        byte[] msg = message.getBytes();
        DatagramPacket response = new DatagramPacket(msg,1024,recibido.getAddress(),recibido.getPort());
        socket.send(response);
    }

    /**
     * Metodo que manda un string al dispositivo destino
     * @param data el dato o mensaje a mandar
     */
    public synchronized void sendData(String data) throws IOException {
        interrupt();
        data += "\0";
        byte[] msg = data.getBytes();
        packet = new DatagramPacket(msg,msg.length,this.IPTarget,this.TARGET_PORT);

        socket.send(packet);
        notify();
    }

    private void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public synchronized void suspendConnection() {
        goFlag = false;
        interrupt();
    }
    public synchronized void resumeConnection() {
        goFlag = true;
        this.notify();
    }
    public synchronized void killConnection(){
        alive = false;
        closeSocket();
        this.interrupt();
    }
}
