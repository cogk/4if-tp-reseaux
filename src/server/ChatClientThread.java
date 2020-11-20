/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import modele.Message;
import modele.Protocol;
import modele.Rename;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ChatClientThread extends Thread {

    private final ChatManager chatManager;
    private final Socket clientSocket;

    private BufferedReader socIn = null;
    private PrintStream socOut = null;

    private String clientId = "(anonyme)";

    ChatClientThread(Socket s, ChatManager chatManager) {
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

    private void interpreter(String line) {
        // Parse the input command
        String[] arguments = line.split("\u0000", 2);

        String commande = arguments.length > 0 ? arguments[0] : "";
        String parametres = arguments.length > 1 ? arguments[1] : "";

        switch (commande) {
            case "msg":
                Message msg = Protocol.deserializeMessage(parametres);
                if (msg.getCreatedBy().equals(this.clientId) || msg.getCreatedBy().length() == 0) {
                    chatManager.pushMessage(msg);
                } else {
                    System.out.println("ignored msg from spoofed id " + msg.getCreatedBy());
                }
                break;
            case "rename":
                Rename rename = Protocol.deserializeRename(parametres);
                if (rename.getOldPseudo().equals(this.clientId) || rename.getOldPseudo().length() == 0) {
                    String newPseudo = rename.getNewPseudo().toLowerCase().replaceAll("[^a-z0-9_-]", "");
                    rename.setNewPseudo(newPseudo);
                    this.clientId = newPseudo;
                    chatManager.pushRename(rename);
                } else {
                    System.out.println("ignored rename from spoofed id " + rename.getOldPseudo());
                }
                break;
            default:
                System.err.println("Commande client inconnue pour le serveur: " + line.replace("\u0000", "\\0"));
                return; // stop the thread
        }
    }

    public void gotMessage(Message message) {
        String from = message.getCreatedBy();
        if (from.equals(this.clientId) && !from.equals("(anonymous)")) {
            socOut.println(Protocol.serializeMessage(message));
        } else {
            socOut.println(Protocol.serializeMessage(message));
        }
    }

    public void gotRename(Rename rename) {
        socOut.println(Protocol.serializeRename(rename));
    }
}
