package client;

import modele.Message;
import modele.Protocol;
import modele.Rename;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ChatClientUDPSendThread extends Thread {
    private final DatagramSocket socket;
    private final BufferedReader terminalInput;

    private String pseudo = "(anonyme)";

    private boolean shouldStop = false;
    private final InetAddress serverAddress;
    private final int serverPort;

    public ChatClientUDPSendThread(DatagramSocket socket, BufferedReader terminalInput, InetAddress serverAddress, int serverPort) {
        this.socket = socket;
        this.terminalInput = terminalInput;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        System.out.println("Bienvenue dans l'application de chat");
        System.out.println("  Entrez du texte, puis appuyez sur <Entrée> pour envoyer un message");
        System.out.println("  Des commandes sont disponibles :");
        System.out.println("    /pseudo <votre nouveau pseudo>");
        System.out.println();

        try {
            while (!shouldStop) {
                String line = null;
                try {
                    line = terminalInput.readLine().trim();
                } catch (IOException e) {
                    System.out.println("* Erreur");
                    return;
                }

                if (line.equals("q")) {
                    System.out.println("* Déconnexion");
                    break;
                } else if (line.length() == 0) {
                    continue;
                } else if (line.startsWith("/pseudo ")) {
                    String newPseudo = line.substring(8).trim();
                    send(Protocol.writeRename(new Rename(pseudo, newPseudo)));
                    pseudo = newPseudo;
                } else {
                    send(Protocol.writeMessage(new Message(pseudo, line)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String s) throws IOException {
        byte[] buf = s.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, serverPort);
        socket.send(packet);
    }
}
