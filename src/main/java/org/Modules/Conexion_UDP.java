package org.Modules;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class Conexion_UDP extends Thread {

    private static final int SERVER_PORT = 4545;
    private static final int CLIENT_PORT = 5558;

    DatagramSocket socket;
    DatagramPacket packet;
    private volatile boolean alive;
    private volatile boolean goFlag;
    private InetAddress IPAddress;
    private InetAddress IPTarget;
    private final int PORT;
    private final int TARGET_PORT;
    private String lastReceived = "";

    /**
     * Creacion del socket para la comunicacion de un dispositivo a solo otro dispositivo
     * @param name  Nombre del hilo
     * @param IPAddress Direccion ip de este dispositivo
     * @param IPTarget  Direccion ip del dispositivo de destino
     * @param isServer boleano que dicta si este objeto se usara como servidor o como cliente (Para los puertos)
     */
    public Conexion_UDP(String name, String IPAddress, String IPTarget, boolean isServer) throws IOException {
        super(name);
        this.IPAddress = InetAddress.getByName(IPAddress);
        this.IPTarget = InetAddress.getByName(IPTarget);
        this.PORT = (isServer ? SERVER_PORT : CLIENT_PORT);
        this.TARGET_PORT = (isServer ? CLIENT_PORT : SERVER_PORT);
        this.alive = true;
        this.goFlag = false;

        // Intento de creación del socket
        boolean socketCreated = false;
        int attempts = 0;
        while (!socketCreated && attempts < 3) {
            try {
                this.socket = new DatagramSocket(this.PORT, this.IPAddress);
                socketCreated = true;
            } catch (SocketException e) {
                attempts++;
                if (attempts >= 3) {
                    throw new IOException("Error en Socket: " + e.getMessage());
                }
                try {
                    Thread.sleep(1000); // Espera un segundo antes de reintentar
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        start();
    }

    /**
     * Funcion run que se mantiene pendiente de recibir paquetes
     */
    @Override
    public void run() {
        try {
            while (alive) {
                synchronized (this) {
                    while (!goFlag) {
                        wait();
                    }
                }
                receivePacket();
            }
        } catch (IOException e) {
            if (alive) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            if (alive) {
                e.printStackTrace();
            }
        } finally {
            closeSocket();
        }
    }

    /**
     * Metodo que maneja la entrada de un paquete
     */
    private void receivePacket() throws IOException {
        DatagramPacket recibido = new DatagramPacket(new byte[1024], 1024);
        try {
            socket.receive(recibido);
            String dato = new String(recibido.getData()).split("\0")[0];
            System.out.println("Received: " + dato);
            lastReceived = dato;
        } catch (SocketException e) {
            if (!alive) {
                return; // Si el socket se cierra mientras recibe, simplemente retornar
            }
            throw e;
        }
    }

    /**
     * Metodo que manda un string al dispositivo destino
     * @param data el dato o mensaje a mandar
     */
    public synchronized void sendData(String data) throws IOException {
        System.out.println(data);
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is closed or not initialized");
        }
        data += "\0";
        byte[] msg = data.getBytes();
        packet = new DatagramPacket(msg, msg.length, this.IPTarget, this.TARGET_PORT);

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
        notify();
    }

    public synchronized void killConnection() {
        alive = false;
        closeSocket();
        this.interrupt();
    }

    public String getLastReceived() {
        return lastReceived;
    }
}
