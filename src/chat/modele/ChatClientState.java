package chat.modele;

public class ChatClientState {
    private String pseudo = "(anonyme)"; // anonyme
    private String room = ""; // salle principale

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
