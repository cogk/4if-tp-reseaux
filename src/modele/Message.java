package modele;

public class Message {

    private final String createdBy;

    private final String text;

    private String room;

    /**
     * Constructeur de Message pour le canal principal (room non spécifiée)
     * @param from Indique le client qui a écrit le message
     * @param text Message écrit par le client
     */
    public Message(String from, String text) {
        this.createdBy = from;
        this.text = text;
        this.room = ""; // canal principal
    }

    /**
     * Constructeur de Message pour un canal spécifique
     * @param from Indique le client qui a écrit le message
     * @param text Message écrit par le client
     * @param room Indique le nom de la room dans laquelle le message doit apparaître
     */
    public Message(String from, String text, String room) {
        this.createdBy = from;
        this.text = text;
        this.room = room;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        if (room.length() == 0) {
            return "<" + createdBy + ">: " + text;
        } else {
            return "<" + createdBy + "@" + room + ">: " + text;
        }
    }
}
