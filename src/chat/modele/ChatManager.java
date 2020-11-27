package chat.modele;

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

    /**
     * Ajoute un message dans l'historique et l'ajoute également au fichier texte représentant cet historique
     * @param msg Message à ajouter
     */
    public void pushMessage(Message msg) {
        messages.add(msg);
        chatServer.pushMessage(msg);
        try {
            persist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie une demande de rename au serveur
     * @param rename Demande de rename
     */
    public void pushRename(Rename rename) {
        chatServer.pushRename(rename);
    }

    /**
     * Renvoie la liste des messages correspondant à un salon donné
     * @param room Salon dont on veut récupérer les messages
     * @return Liste des messages du salon
     */
    public List<Message> getMessagesByRoom(String room) {
        final List<Message> filtered = new ArrayList<>();
        for (Message msg : messages) {
            if (msg.getRoom().equals(room)) {
                filtered.add(msg);
            }
        }
        return filtered;
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyyMMdd HH:mm:ss.SSS")
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());

    /**
     * Permet d'enregistrer l'historique dans un fichier texte, ou de le créer s'il n'existe pas encore
     * @throws IOException Erreur lors de la sortie des messages vers le fichier texte
     */
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

    /**
     * Charge l'historique des messages présent dans le fichier texte qui sert d'historique
     * @throws IOException Erreur lors de la lecture des messages dans l'historique
     */
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
