package chat.udp;

import chat.modele.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ChatClientUDPSendThread extends Thread {
    private final DatagramSocket socket;
    private final BufferedReader terminalInput;

    private final ChatClientState etatDuClient;

    private boolean shouldStop = false;
    private final InetAddress serverAddress;
    private final int serverPort;

    /**
     * Constructeur de thread d'envoi chat.client
     * @param socket Information sur la socket utilisée par le thread pour envoyer des messages
     * @param terminalInput Terminal pour la lecture des messages
     * @param serverAddress Adresse du serveur utilisé
     * @param serverPort Port du serveur utilisé
     * @param etatDuClient
     */
    public ChatClientUDPSendThread(DatagramSocket socket, BufferedReader terminalInput, InetAddress serverAddress, int serverPort, ChatClientState etatDuClient) {
        this.socket = socket;
        this.terminalInput = terminalInput;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.etatDuClient = etatDuClient;
    }

    /**
     * Cette fonction donne l'information de demande d'arrêt du thread en modifiant shouldStop
     */
    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        System.out.println("Bienvenue dans l'application de chat");
        System.out.println("Entrez du texte, puis appuyez sur <Entrée> pour envoyer un message");
        System.out.println("Des commandes sont disponibles :");
        System.out.println("  /quit    -- quitter l'application");
        System.out.println("  /pseudo <nouveau pseudo>    -- changer votre pseudonyme");
        System.out.println("  /room <nom de la salle de discussion>    -- changer de salle de discussion");
        System.out.println();

        System.out.println("Veuillez entrer votre pseudo :");
        try {
            etatDuClient.setPseudo(terminalInput.readLine().trim());
        } catch (IOException e) {
            System.out.println("* Erreur");
            return;
        }

        try {
            send(Protocol.serializeHello(new Hello(etatDuClient.getRoom(), etatDuClient.getPseudo())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (!shouldStop) {
                String line;
                try {
                    line = terminalInput.readLine().trim();
                } catch (IOException e) {
                    System.out.println("* Erreur");
                    return;
                }

                if (line.equals("/quit") || line.equals("/dc") || line.equals("/bye")) {
                    System.out.println("* Déconnexion");
                    break;
                } else if (line.startsWith("/pseudo ")) {
                    String newPseudo = line.substring(8).replaceAll("[^a-zA-Z0-9_-]", "");
                    send(Protocol.serializeRename(new Rename(etatDuClient.getPseudo(), newPseudo)));
                    etatDuClient.setPseudo(newPseudo);
                    System.out.println("Votre nouveau pseudo est <" + newPseudo + ">");
                } else if (line.startsWith("/room ") || line.equals("/room")) {
                    String room = etatDuClient.getRoom();
                    String newRoom = line.equals("/room") ? "" : line.substring(6).trim();
                    if (room.equals(newRoom)) {
                        System.out.println("* Commande ignorée");
                    } else if (room.length() == 0) {
                        System.out.println("* Vous avez quitté la salle principale et rejoint #" + newRoom);
                    } else if (newRoom.length() == 0) {
                        System.out.println("* Vous avez quitté #" + room + " et rejoint la salle principale");
                    } else {
                        System.out.println("* Vous avez quitté #" + room + " et rejoint #" + newRoom);
                    }
                    etatDuClient.setRoom(newRoom);
                } else {
                    if (line.length() > 0) {
                        // Il s'agit d'un message textuel
                        Message message = new Message(etatDuClient.getPseudo(), line, etatDuClient.getRoom());
                        System.out.println(message);
                        send(Protocol.serializeMessage(message));
                    }
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
