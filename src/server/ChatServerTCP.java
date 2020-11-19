package server;

import modele.Message;
import modele.Rename;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerTCP implements ChatServer {
    private final List<ChatClientThread> clients = new ArrayList<>();

    private final int port;

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

    public void pushMessage(Message msg) {
        for (int i = 0; i < clients.size(); i++) {
            ChatClientThread ct = clients.get(i);
            ct.gotMessage(msg);
        }
    }

    public void pushRename(Rename rename) {
        for (int i = 0; i < clients.size(); i++) {
            ChatClientThread ct = clients.get(i);
            ct.gotRename(rename);
        }
    }
}
