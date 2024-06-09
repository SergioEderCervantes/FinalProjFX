package org.Modules;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;

public class ConnectionUtils {

    public static final String devIp = getDeviceIP();

    public static final int SERVER_PORT = 4545;
    public static final int CLIENT_PORT = 5558;

    private static String getDeviceIP() {

        String ip = null;
        try{
            ip = getVpnPrivateIpAddress();
            if (ip == null) {
                ip = getIPAdress();
            }
        } catch (SocketException e) {
            System.err.println("SocketException: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
        }
        return ip;
    }


    private static String getVpnPrivateIpAddress() throws SocketException {
        for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                    if (inetAddress.isSiteLocalAddress() && networkInterface.getDisplayName().toLowerCase().contains("vpn")) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        return null;
    }

    private static String getIPAdress() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }


}
