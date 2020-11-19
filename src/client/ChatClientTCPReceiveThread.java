package client;

import modele.Message;
import modele.Protocol;
import modele.Rename;

import java.io.*;
import java.util.Arrays;

public class ChatClientTCPReceiveThread extends Thread {
    private final BufferedReader socketInput;

    private boolean shouldStop = false;

    ChatClientTCPReceiveThread(BufferedReader socketInput) throws IOException {
        this.socketInput = socketInput;
    }

    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        String line = null;
        String[] arguments;

        while (!shouldStop) {
            try {
                line = socketInput.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line == null) {
                continue;
            }

            // Parse the input command
            arguments = line.split("\u0000", 2);

            String commande = arguments.length > 0 ? arguments[0] : "";
            String parametres = arguments.length > 1 ? arguments[1] : "";

            switch (commande) {
                case "msg":
                    Message message = Protocol.readMessage(parametres);
                    System.out.println(message.toString());
                    break;
                case "rename":
                    Rename rename = Protocol.readRename(parametres);
                    System.out.println(rename.toString());
                    break;
                case "protocol error":
                    System.err.println("Erreur de protocole");
                    return;
                case "disconnect":
                    System.err.println("Vous avez été déconnecté.e");
                    return;
                default:
                    System.err.println("Commande serveur inconnue: " + line);
                    return; // stop the thread
            }
        }
    }
}
