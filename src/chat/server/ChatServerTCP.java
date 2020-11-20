package chat.server;

import chat.modele.Message;
import chat.modele.Rename;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerTCP implements ChatServer {
    private final List<ChatClientThread> clients = new ArrayList<>();

    private final int port;

    /**
     * Cette fonction simule le serveur central qui gère l'échange de messages et le renommage des clients en TCP
     * @param port Port du serveur
     * @param chatManager Permet de connaître le chat manager qui gère le stockage de l'historique des messages
     */
    public ChatServerTCP(int port, ChatManager chatManager) {
        chatManager.setChatServer(this);

        this.port = port;

        try {
            ServerSocket listenSocket = new ServerSocket(port);
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("TCP connection from:" + clientSocket.getInetAddress());
                ChatClientThread ct = new ChatClientThread(clientSocket, chatManager);
                clients.add(ct);
                ct.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in ChatServerTCP:" + e);
        }
    }

    /**
     * Cette fonction permet de donner l'information de l'envoi d'un message par un chat.client à tous les clients connectés (broadcast)
     * @param msg Information sur le message envoyé
     */
    public void pushMessage(Message msg) {
        for (int i = 0; i < clients.size(); i++) {
            ChatClientThread ct = clients.get(i);
            ct.gotMessage(msg);
        }
    }

    /**
     * Cette fonction permet de donner l'information du rename à tous les clients connectés (broadcast)
     * @param rename Information sur le renommage d'un chat.client
     */
    public void pushRename(Rename rename) {
        for (int i = 0; i < clients.size(); i++) {
            ChatClientThread ct = clients.get(i);
            ct.gotRename(rename);
        }
    }
}
