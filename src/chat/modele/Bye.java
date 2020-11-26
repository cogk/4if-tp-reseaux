package chat.modele;

public class Bye {

    private final String pseudo;

    private final String room;

    public Bye(String pseudo, String room) {
        this.pseudo = pseudo;
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public String getPseudo() {
        return pseudo;
    }

    @Override
    public String toString() {
        if (room.length() == 0) {
            return pseudo + " vient de quitter le serveur.";
        } else {
            return pseudo + " vient de quitter le salon #" + room + ".";
        }
    }
}
