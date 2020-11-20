package server;

import modele.Message;
import modele.Rename;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    /**
     * Liste des messages envoyés représentant l'historique des messages du chat
     * **/
    private List<Message> messages = new ArrayList<>();
    private ChatServer chatServer;

    public ChatManager() {
    }

    public void setChatServer(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public void pushMessage(Message msg) {
        messages.add(msg);
        chatServer.pushMessage(msg);
    }

    public void pushRename(Rename rename) {
        chatServer.pushRename(rename);
    }
}
