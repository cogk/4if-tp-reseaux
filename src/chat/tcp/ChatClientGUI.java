package chat.tcp;

import chat.modele.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ChatClientGUI {
    /**
     * main method
     * accepts a connection, receives a message from chat.client then sends an echo to the chat.client
     **/
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintStream socOut = null;
        BufferedReader socIn = null;

        ChatClientState chatClientState = new ChatClientState();

        if (args.length != 2) {
            System.out.println("Usage: java ChatClientGUI <server host> <port>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socOut = new PrintStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.err.println("Erreur: Hôte inconnu: " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Erreur: Impossible d'établir la connexion avec le serveur " + args[0]);
            System.exit(2);
        }

        ChatClientGUIWindow fenetre = new ChatClientGUIWindow(socOut, chatClientState);

        String line;
        String[] arguments;

        fenetre.appendText("Bienvenue dans l'application de chat");
        fenetre.appendText("Entrez du texte, puis appuyez sur <Entrée> pour envoyer un message");
        fenetre.appendText("Des commandes sont disponibles :");
        fenetre.appendText("  /quit    -- quitter l'application");
        fenetre.appendText("  /pseudo <nouveau pseudo>    -- changer votre pseudonyme");
        fenetre.appendText("  /room <nom de la salle de discussion>    -- changer de salle de discussion");
        fenetre.appendText("  /clear    -- effacer la vue textuelle");
        fenetre.appendText("");

        chatClientState.setPseudo("AnonymeGUI" + new Random().nextInt(999));

        socOut.println(Protocol.serializeHello(new Hello(chatClientState.getRoom(), chatClientState.getPseudo())));

        while (fenetre.estOuverte()) {
            try {
                line = socIn.readLine();
            } catch (IOException e) {
                // e.printStackTrace();
                break;
            }

            if (line == null) {
                continue;
            }

            boolean shouldStopNow = ChatClientUtils.handleIncomingData(line, chatClientState, fenetre::appendText);
            if (shouldStopNow) {
                fenetre.fermer();
                break;
            }
        }

        socket.close();
        socIn.close();
        socOut.close();
    }
}
