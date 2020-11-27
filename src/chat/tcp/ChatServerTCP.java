package chat.tcp;

import chat.modele.ChatServer;
import chat.modele.Message;
import chat.modele.Rename;
import chat.modele.ChatManager;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerTCP implements ChatServer {
    private final List<ChatServerTCPThread> clients = new ArrayList<>();

    /**
     * Cette fonction simule le serveur central qui gère l'échange de messages et le renommage des clients en TCP
     * @param port Port du serveur
     * @param chatManager Permet de connaître le chat manager qui gère le stockage de l'historique des messages
     */
    public ChatServerTCP(int port, ChatManager chatManager) {
        chatManager.setChatServer(this);

        try {
            ServerSocket listenSocket = new ServerSocket(port);
            System.out.println("Server ready...");
            while (true) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("TCP connection from:" + clientSocket.getInetAddress());
                ChatServerTCPThread ct = new ChatServerTCPThread(clientSocket, chatManager);
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
        for (ChatServerTCPThread ct : clients) {
            ct.gotMessage(msg);
        }

        clients.removeIf(ChatServerTCPThread::hasStopped);
    }

    /**
     * Cette fonction permet de donner l'information du rename à tous les clients connectés (broadcast)
     * @param rename Information sur le renommage d'un chat.client
     */
    public void pushRename(Rename rename) {
        for (ChatServerTCPThread ct : clients) {
            ct.gotRename(rename);
        }

        clients.removeIf(ChatServerTCPThread::hasStopped);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java chat.tcp.ChatServerTCP <port>");
            System.exit(1);
        } else {
            try {
                int port = Integer.parseInt(args[0]);
                ChatManager chatManager = new ChatManager();
                new ChatServerTCP(port, chatManager);
            } catch (Exception e) {
                System.err.println("port invalide");
            }
        }
    }
}
