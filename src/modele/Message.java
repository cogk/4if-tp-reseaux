package modele;

public class Message {

    private final String createdBy;

    private final String text;

    private String room;


    public Message(String from, String text) {
        this.createdBy = from;
        this.text = text;
        this.room = ""; // canal principal
    }

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
