package chat.tcp;

import chat.modele.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class ChatClientTCPSendThread extends Thread {
    private final PrintStream socketOutput;
    private final BufferedReader terminalInput;
    private final ChatClientState chatClientState;

    private boolean shouldStop = false;

    /**
     * Constructeur de thread d'envoi chat.client
     * @param socketOutput Information sur la socket utilisée par le thread pour envoyer des messages
     * @param terminalInput Terminal pour la lecture des messages
     */
    public ChatClientTCPSendThread(
            PrintStream socketOutput,
            BufferedReader terminalInput,
            ChatClientState chatClientState
    ) {
        this.socketOutput = socketOutput;
        this.terminalInput = terminalInput;
        this.chatClientState = chatClientState;
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
            chatClientState.setPseudo(terminalInput.readLine().trim());
        } catch (IOException e) {
            System.out.println("* Erreur");
            return;
        }

        socketOutput.println(Protocol.serializeHello(new Hello(chatClientState.getRoom(), chatClientState.getPseudo())));

        while (!shouldStop) {
            String line = null;
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
                String pseudo = chatClientState.getPseudo();
                String newPseudo = line.substring(8).replaceAll("[^a-zA-Z0-9_-]", "");
                socketOutput.println(Protocol.serializeRename(new Rename(pseudo, newPseudo)));
                chatClientState.setPseudo(newPseudo);
                System.out.println("Votre nouveau pseudo est <" + newPseudo + ">");
            } else if (line.startsWith("/room ") || line.equals("/room")) {
                String room = chatClientState.getRoom();
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
                chatClientState.setRoom(newRoom);
            } else {
                if (line.length() > 0) {
                    // Il s'agit d'un message textuel
                    String pseudo = chatClientState.getPseudo();
                    String room = chatClientState.getRoom();
                    Message message = new Message(pseudo, line, room);
                    System.out.println(message);
                    socketOutput.println(Protocol.serializeMessage(message));
                }
            }
        }
    }
}
