package modele;

public class Protocol {
    public static String serializeMessage(Message message) {
        return "msg"
                + "\u0000" + message.getCreatedBy()
                + "\u0000" + message.getText();
        // + "\u0000" + message.getRoom();
    }

    public static Message deserializeMessage(String str) {
        String[] segments = str.split("\u0000");
        return new Message(segments[0], segments[1]);
    }

    public static String serializeRename(Rename rename) {
        return "rename" + "\u0000" + rename.getOldPseudo() + "\u0000" + rename.getNewPseudo();
    }

    public static Rename deserializeRename(String str) {
        String[] segments = str.split("\u0000");
        String oldPseudo = segments[0];
        String newPseudo = segments[1];
        return new Rename(oldPseudo, newPseudo);
    }

    public static String serializeProtocolError() {
        return "protocol error";
    }
}
