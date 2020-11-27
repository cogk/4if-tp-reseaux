package chat.modele;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

    private final String createdBy;

    private final String text;

    private final String room;

    private final LocalDateTime createdTime;

    /**
     * Constructeur de Message pour le canal principal (room non spécifiée)
     * @param from Indique le chat.client qui a écrit le message
     * @param text Message écrit par le chat.client
     */
    public Message(String from, String text) {
        this.createdBy = from;
        this.text = text;
        this.room = ""; // canal principal
        this.createdTime = LocalDateTime.now();
    }

    /**
     * Constructeur de Message pour un canal spécifique
     * @param from Indique le chat.client qui a écrit le message
     * @param text Message écrit par le chat.client
     * @param room Indique le nom de la room dans laquelle le message doit apparaître
     */
    public Message(String from, String text, String room) {
        this.createdBy = from;
        this.text = text;
        this.room = room;
        this.createdTime = LocalDateTime.now();
    }

    /**
     * Constructeur de Message pour un canal spécifique avec une heure précisée pour le message
     * @param from Indique le chat.client qui a écrit le message
     * @param text Message écrit par le chat.client
     * @param room Indique le nom de la room dans laquelle le message doit apparaître
     * @param createdTime Indique la date et l'heure du message
     */
    public Message(String from, String text, String room, LocalDateTime createdTime) {
        this.createdBy = from;
        this.text = text;
        this.room = room;
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getText() {
        return text;
    }

    public String getRoom() {
        return room;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        if (room.length() == 0) {
            return dateTimeFormatter.format(createdTime) + " <" + createdBy + ">: " + text;
        } else {
            return dateTimeFormatter.format(createdTime) + " <" + createdBy + "@" + room + ">: " + text;
        }
    }
}
