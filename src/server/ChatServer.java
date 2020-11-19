package server;

import modele.Message;
import modele.Rename;

public interface ChatServer {
    void pushMessage(Message msg);

    void pushRename(Rename rename);
}
