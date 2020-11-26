package chat.udp;

import chat.modele.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class ChatClientUDPReceiveThread extends Thread {
    private final MulticastSocket socket;

    private final ChatClientState chatClientState;

    private boolean shouldStop = false;

    /**
     * Constructeur de thread de réception chat.client
     *
     * @param socket       Information sur la socket utilisée par le thread pour recevoir des messages
     * @param chatClientState
     */
    public ChatClientUDPReceiveThread(MulticastSocket socket, ChatClientState chatClientState) {
        this.socket = socket;
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

        byte[] buffer = new byte[1024];
        while (!shouldStop) {
            DatagramPacket incomingDatagramPacket = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(incomingDatagramPacket);
            } catch (IOException e) {
                // e.printStackTrace();
                break;
            }

            if (chatClientState.getPseudo().length() == 0) {
                continue;
            }

            line = new String(
                    incomingDatagramPacket.getData(),
                    incomingDatagramPacket.getOffset(),
                    incomingDatagramPacket.getLength()
            );

            boolean shouldStopNow = ChatClientUtils.handleIncomingData(line, chatClientState, System.out::println);
            if (shouldStopNow) {
                shouldStop = true;
            }
        }
    }
}
