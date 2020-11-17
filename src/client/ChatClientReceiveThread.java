package client;

import java.io.*;

public class ChatClientReceiveThread extends Thread {
    private final BufferedReader socketInput;

    private boolean shouldStop = false;

    ChatClientReceiveThread(BufferedReader socketInput) throws IOException {
        this.socketInput = socketInput;
    }

    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        String line = null;
        String[] arguments;

        while (true) {
            if (shouldStop) {
                break;
            }

            try {
                line = socketInput.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line == null) {
                continue;
            }

            // Parse the input command
            arguments = line.split("\u0000");
            if (arguments.length == 0) {
                continue;
            }

            switch (arguments[0]) {
                case "msg":
                    String from = arguments[1];
                    String text = arguments[2];
                    String room = arguments.length >= 4 ? arguments[3] : "";
                    if (room.length() == 0) {
                        System.out.println("<" + from + ">: " + text);
                    } else {
                        System.out.println("<" + from + "@" + room + ">: " + text);
                    }
                    break;
                case "pseudo":
                    System.out.println("<" + arguments[1] + "> s'est renommé <" + arguments[2] + ">");
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
