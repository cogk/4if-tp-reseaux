package chat.tcp;

import chat.modele.*;

import java.io.*;

public class ChatClientTCPReceiveThread extends Thread {
    private final BufferedReader socketInput;
    private final ChatClientState chatClientState;

    private boolean shouldStop = false;

    /**
     * Constructeur de thread de réception chat.client
     * @param socketInput Information sur la socket utilisée par le thread pour recevoir des messages
     * @param chatClientState Etat du chat client
     */
    public ChatClientTCPReceiveThread(
            BufferedReader socketInput,
            ChatClientState chatClientState
    ) {
        this.socketInput = socketInput;
        this.chatClientState = chatClientState;
    }

    /**
     * Cette fonction donne l'information de demande d'arrêt du thread en modifiant shouldStop
     */
    public void endThread() {
        shouldStop = true;
    }

    public void run() {
        String line;

        while (!shouldStop) {
            try {
                line = socketInput.readLine();
            } catch (IOException e) {
                // e.printStackTrace();
                break;
            }

            if (line == null) {
                continue;
            }

            if (chatClientState.getPseudo().length() == 0) {
                continue;
            }

            boolean shouldStopNow = ChatClientUtils.handleIncomingData(line, chatClientState, System.out::println);
            if (shouldStopNow) {
                shouldStop = true;
            }
        }
    }
}
