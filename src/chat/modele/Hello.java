package chat.modele;

public class Hello {
    private final String initialRoom;
    private final String initialPseudo;

    public Hello(String initialRoom, String initialPseudo) {
        this.initialRoom = initialRoom;
        this.initialPseudo = initialPseudo;
    }

    public String getInitialRoom() {
        return initialRoom;
    }

    public String getInitialPseudo() {
        return initialPseudo;
    }
}
