package chat.modele;

import java.util.Date;

public class Message {

    private final String createdBy;

    private final String text;

    private final String room;

    private final Date createdTime;

    /**
     * Constructeur de Message pour le canal principal (room non spécifiée)
     * @param from Indique le chat.client qui a écrit le message
     * @param text Message écrit par le chat.client
     */
    public Message(String from, String text) {
        this.createdBy = from;
        this.text = text;
        this.room = ""; // canal principal
        this.createdTime = new Date();
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
        this.createdTime = new Date();
    }

    public Message(String from, String text, String room, Date createdTime) {
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

    public Date getCreatedTime() {
        return createdTime;
    }

    @Override
    public String toString() {
        if (room.length() == 0) {
            return createdTime.toString() + " <" + createdBy + ">: " + text;
        } else {
            return createdTime.toString() + " <" + createdBy + "@" + room + ">: " + text;
        }
    }
}
