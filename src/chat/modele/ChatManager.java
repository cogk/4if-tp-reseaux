package chat.modele;

import chat.modele.ChatServer;
import chat.modele.Message;
import chat.modele.Rename;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ChatManager {
    /**
     * Liste des messages envoyés représentant l'historique des messages du chat
     **/
    private final List<Message> messages = new ArrayList<>();
    private ChatServer chatServer;

    private final static String historyFileLocation = "history.txt";

    public ChatManager() {
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (messages.size() == 0) {
            messages.add(new Message("~server~", "Bonjour et bienvenue sur le serveur"));
            messages.add(new Message("~server~", "Rendez-vous dans la room #help si vous avez besoin d'aide (tapez /room help)"));

            try {
                persist();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Initial messages:");
        for (Message m : messages) {
            System.out.println(m);
        }
    }

    public void setChatServer(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public void pushMessage(Message msg) {
        messages.add(msg);
        chatServer.pushMessage(msg);
        try {
            persist();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyyMMdd HH:mm:ss.SSS")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    public void persist() throws IOException {
        File f = new File(historyFileLocation);
        if (!f.exists()) {
            boolean created = f.createNewFile();
            if (!created) {
                return;
            }
        }
        if (f.canWrite()) {
            FileWriter fw = new FileWriter(f);
            for (Message message : messages) {
                fw.write(message.getCreatedBy());
                fw.write(0);
                fw.write(dateTimeFormatter.format(message.getCreatedTime()));
                fw.write(0);
                fw.write(message.getRoom());
                fw.write(0);
                fw.write(message.getText());
                fw.write(0);
            }
            fw.close();
        }
    }

    public void load() throws IOException {
        File f = new File(historyFileLocation);
        if (f.canRead()) {
            Scanner fr = new Scanner(f).useDelimiter("\0");

            messages.clear();

            while (fr.hasNext()) {
                String createdBy = fr.next();
                String dateFormatted = fr.next();
                LocalDateTime createdTime = LocalDateTime.from(dateTimeFormatter.parse(dateFormatted));
                String room = fr.next();
                String text = fr.next();
                messages.add(new Message(createdBy, text, room, createdTime));
            }
            fr.close();
        }
    }
}
