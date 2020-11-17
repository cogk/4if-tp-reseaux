package server;

import modele.Message;

public interface ChatServer {
    void pushMessage(Message msg);

    void pushRename(String oldPseudo, String newPseudo);
}
