package http.client;

import java.net.InetAddress;
import java.net.Socket;

public class WebPing {
    /**
     * Main du WebPing
     * @param args Contient le host du Serveur et le port du serveur.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage java WebPing <host name> <port number>");
            return;
        }

        String httpServerHost = args[0];
        int httpServerPort = Integer.parseInt(args[1]);
        httpServerHost = args[0];
        httpServerPort = Integer.parseInt(args[1]);

        try {
            InetAddress addr;
            Socket sock = new Socket(httpServerHost, httpServerPort);
            addr = sock.getInetAddress();
            System.out.println("Connected to " + addr);
            sock.close();
        } catch (java.io.IOException e) {
            System.err.println("Can't connect to " + httpServerHost + ":" + httpServerPort);
            System.err.println(e);
        }
    }
}