package chat.server;

import chat.modele.Message;
import chat.modele.Rename;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    /**
     * Liste des messages envoyés représentant l'historique des messages du chat
     **/
    private final List<Message> messages = new ArrayList<>();
    private ChatServer chatServer;

    public ChatManager() {
        messages.add(new Message("~server~", "Bonjour et bienvenue sur le serveur"));
        messages.add(new Message("~server~", "Rendez-vous dans la room #help si vous avez besoin d'aide (tapez /room help)"));
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

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getMessagesByRoom(String room) {
        final List<Message> filtered = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getRoom().equals(room)) {
                filtered.add(msg);
            }
        }
        return filtered;
    }
}
