package modele;

public class Hello {
    private final String initialRoom;

    public Hello() {
        this.initialRoom = ""; // la salle principale
    }

    public Hello(String initialRoom) {
        this.initialRoom = initialRoom;
    }

    public String getInitialRoom() {
        return initialRoom;
    }
}
