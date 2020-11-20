package chat.client;

import chat.modele.Message;
import chat.modele.Protocol;
import chat.modele.Rename;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class ChatClientTCPSendThread extends Thread {
    private final PrintStream socketOutput;
    private final BufferedReader terminalInput;

    private String pseudo = "(anonyme)";

    private boolean shouldStop = false;

    /**
     * Constructeur de thread d'envoi chat.client
     * @param socketOutput Information sur la socket utilisée par le thread pour envoyer des messages
     * @param terminalInput Terminal pour la lecture des messages
     */
    public ChatClientTCPSendThread(PrintStream socketOutput, BufferedReader terminalInput) {
        this.socketOutput = socketOutput;
        this.terminalInput = terminalInput;
    }

    /**
     * Cette fonction donne l'information de demande d'arrêt du thread en modifiant shouldStop
     */
    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        System.out.println("Bienvenue dans l'application de chat");
        System.out.println("  Entrez du texte, puis appuyez sur <Entrée> pour envoyer un message");
        System.out.println("  Des commandes sont disponibles :");
        System.out.println("    /pseudo <votre nouveau pseudo>");
        System.out.println();

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
                socketOutput.println(Protocol.serializeRename(new Rename(pseudo, newPseudo)));
                pseudo = newPseudo;
            } else {
                socketOutput.println(Protocol.serializeMessage(new Message(pseudo, line)));
            }
        }
    }
}
