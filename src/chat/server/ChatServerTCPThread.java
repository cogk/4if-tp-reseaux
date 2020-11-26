/***
 * ClientThread
 * Example of a TCP chat.server
 * Date: 14/12/08
 * Authors:
 */

package chat.server;

import chat.modele.Hello;
import chat.modele.Message;
import chat.modele.Protocol;
import chat.modele.Rename;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class ChatServerTCPThread extends Thread {

    private final ChatManager chatManager;
    private final Socket clientSocket;

    private BufferedReader socIn = null;
    private PrintStream socOut = null;

    private String clientId = "(anonyme)";

    /**
     * Constructeur de thread de chat pour le client côté serveur
     * @param s Information sur la socket utilisée pour transmettre des messages et demandes de renommage
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
            e.printStackTrace();
            System.err.println("Error in ChatClientThread:" + e);
        }
    }

    /**
     * Cette fonction permet d'interpréter la ligne écrite par le chat.client et d'agir en fonction du type (msg : message; rename : demande de renommage)
     * @param line Ligne écrite par un chat.client
     */
    private void interpreter(String line) {
        // Parse the input command
        String[] arguments = line.split("\u0000", 2);

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
                List<Message> messages = chatManager.getMessagesByRoom(hello.getInitialRoom());
                for (Message message : messages) {
                    socOut.println(Protocol.serializeMessage(message));
                }
                if (!this.clientId.startsWith("(")) {
                    chatManager.pushMessage(new Message("~server~", this.clientId + " vient d'arriver sur le serveur."));
                }
                break;
            default:
                System.err.println("Commande client inconnue pour le serveur: " + line.replace("\u0000", "\\0"));
                break; // stop the thread
        }
    }

    /**
     *
     * @param message
     */
    public void gotMessage(Message message) {
        String from = message.getCreatedBy();
        boolean isMyMessage = from.equals(this.clientId) && !this.clientId.startsWith("(");
        if (!isMyMessage){
            socOut.println(Protocol.serializeMessage(message));
        }
    }

    /**
     * Cette fonction permet d'afficher
     * @param rename
     */
    public void gotRename(Rename rename) {
        socOut.println(Protocol.serializeRename(rename));
    }
}
