/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import modele.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ChatClientThread extends Thread {

    private final ChatManager chatManager;
    private final Socket clientSocket;

    private BufferedReader socIn = null;
    private PrintStream socOut = null;

    private String clientId = "(anonymous)";

    ChatClientThread(Socket s, ChatManager chatManager) {
        this.clientSocket = s;
        this.chatManager = chatManager;
    }

    /**
     * receives a request from client then sends an echo to the client
     */
    public void run() {
        try {
            socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socOut = new PrintStream(clientSocket.getOutputStream());

            while (true) {
                String line = socIn.readLine();
                interpreter(line);
            }
        } catch (Exception e) {
            System.err.println("Error in ChatClientThread:" + e);
        }
    }

    private void interpreter(String line) {
        // Parse the input command
        String[] arguments = line.split("\u0000");

        if (arguments[0].equals("msg")) {
            String text = arguments[1];
            chatManager.pushMessage(new Message(this.clientId, "ALL", text));
        } else if (arguments[0].equals("pseudo")) {
            String text = arguments[1];
            String newPseudo = text.toLowerCase().replaceAll("[^a-z0-9_-]", "");
            chatManager.pushRename(this.clientId, newPseudo);
            this.clientId = newPseudo;
        } else {
            System.err.println("Commande inconnue: " + line);
            socOut.println("protocol error");
        }
    }

    public void gotMessage(Message message) {
        String from = message.getCreatedBy();
        if (from.equals(this.clientId) && !from.equals("(anonymous)")) {
            socOut.println("msg" + "\u0000" + from + "\u0000" + message.getMessage());
        } else {
            socOut.println("msg" + "\u0000" + from + "\u0000" + message.getMessage());
        }
    }

    public void gotRename(String oldPseudo, String newPseudo) {
        socOut.println("pseudo" + "\u0000" + oldPseudo + "\u0000" + newPseudo);
    }
}


















