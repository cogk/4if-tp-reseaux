package chat.modele;

public interface ChatServer {
    void pushMessage(Message msg);

    void pushRename(Rename rename);
}
