package chat.server;

import chat.modele.Message;
import chat.modele.Rename;

public interface ChatServer {
    void pushMessage(Message msg);

    void pushRename(Rename rename);
}
