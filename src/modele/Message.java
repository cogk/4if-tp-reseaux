package modele;

public class Message {

    private final String createdBy;

    // private String destination;

    private final String message;

    public Message(String from, String to, String message) {
        this.createdBy = from;
        // this.destination = to;
        this.message = message;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getMessage() {
        return message;
    }
}
