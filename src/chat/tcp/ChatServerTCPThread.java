package chat.tcp;

import chat.modele.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class ChatServerTCPThread extends Thread {

    private final ChatManager chatManager;
    private final Socket clientSocket;

    private BufferedReader socIn = null;
    private PrintStream socOut = null;

    private String clientId = "Client" + new Random().nextInt(999);

    private boolean stopped = false;

    /**
     * Constructeur de thread de chat pour le client côté serveur
     *
     * @param s           Information sur la socket utilisée pour transmettre des messages et demandes de renommage
     * @param chatManager Permet de connaître le chat manager qui gère le stockage de l'historique des messages
     */
    public ChatServerTCPThread(Socket s, ChatManager chatManager) {
        this.clientSocket = s;
        this.chatManager = chatManager;
    }

    public void run() {
        try {
            if (clientSocket.isClosed()) {
                return;
            }

            socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socOut = new PrintStream(clientSocket.getOutputStream());

            while (!clientSocket.isClosed()) {
                String line = socIn.readLine();
                if (line == null) {
                    break;
                } else {
                    interpreter(line);
                }
            }

            clientSocket.close();
        } catch (Exception e) {
            System.err.println("Error in ChatClientThread: ");
            e.printStackTrace();
        }
        stopped = true;
    }

    /**
     * Cette fonction permet d'interpréter la ligne écrite par le chat.client et d'agir en fonction du type (msg : message; rename : demande de renommage)
     *
     * @param line Ligne écrite par un chat.client
     */
    private void interpreter(String line) {
        // Parse the input command
        String[] arguments = line.split("\0", 2);

        String commande = arguments.length > 0 ? arguments[0] : "";
        String parametres = arguments.length > 1 ? arguments[1] : "";

        switch (commande) {
            case "M":
                Message msg = Protocol.deserializeMessage(parametres);
                chatManager.pushMessage(msg);
                break;
            case "R":
                Rename rename = Protocol.deserializeRename(parametres);
                String newPseudo = rename.getNewPseudo().replaceAll("[^a-zA-Z0-9_-]", "");
                rename.setNewPseudo(newPseudo);
                this.clientId = newPseudo;
                chatManager.pushRename(rename);
                break;
            case "H":
                Hello hello = Protocol.deserializeHello(parametres);
                this.clientId = hello.getInitialPseudo();
                chatManager.pushMessage(new Message("~server~", hello.toString()));

                List<Message> messages = chatManager.getMessagesByRoom(hello.getInitialRoom());
                for (Message message : messages) {
                    socOut.println(Protocol.serializeMessage(message));
                }
                break;
            case "B":
                Bye bye = Protocol.deserializeBye(parametres);
                chatManager.pushMessage(new Message("~server~", bye.toString()));
                break;
            default:
                System.err.println("Commande client inconnue pour le serveur: " + line.replace("\0", "\\0"));
                break; // stop the thread
        }
    }

    /**
     * @param message Le message qui a été reçu
     */
    public void gotMessage(Message message) {
        if (!stopped) {
            socOut.println(Protocol.serializeMessage(message));
        }
    }

    /**
     * @param rename Le renommage qui a été reçu
     */
    public void gotRename(Rename rename) {
        if (!stopped) {
            socOut.println(Protocol.serializeRename(rename));
        }
    }

    public boolean hasStopped() {
        return stopped;
    }
}
