package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class ChatClientSendThread extends Thread {
    private final PrintStream socketOutput;
    private final BufferedReader terminalInput;

    private boolean shouldStop = false;

    public ChatClientSendThread(PrintStream socketOutput, BufferedReader terminalInput) {
        this.socketOutput = socketOutput;
        this.terminalInput = terminalInput;
    }

    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        System.out.println("Bienvenue dans l'application de chat");
        System.out.println("  Entrez du texte, puis appuyez sur <Entrée> pour envoyer un message");
        System.out.println("  Des commandes sont disponibles :");
        System.out.println("  - /pseudo <votre nouveau pseudo>");
        System.out.println();

        while (true) {
            if (shouldStop) {
                break;
            }

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
                String pseudo = line.substring(8).trim();
                socketOutput.println("pseudo" + "\u0000" + pseudo);
            } else {
                socketOutput.println("msg" + "\u0000" + line);
            }
        }
    }
}
