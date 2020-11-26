package chat.modele;

import java.util.Arrays;

public class Protocol {
    /**
     * Cette fonction permet de transformer un objet Message en une chaîne de caractères exploitable pour afficher le message
     *
     * @param message Objet message à transcrire en chaîne de caractères
     * @return La chaîne de caractères constituée du type msg, de l'émetteur du message et du message lui-même, séparés par des null character
     */
    public static String serializeMessage(Message message) {
        return "M"
                + "\0" + message.getCreatedBy()
                + "\0" + message.getRoom()
                + "\0" + message.getText();
    }

    /**
     * Cette fonction permet de transformer une chaîne de caractères en un objet Message pour le transmettre à d'autres clients
     *
     * @param str La chaîne de caractères écrite par le chat.client
     * @return Un objet Message contenant les informations de la chaîne fournie en entrée
     */
    public static Message deserializeMessage(String str) {
        String[] segments = str.split("\0", -1);
        String createdBy = segments[0];
        String room = segments[1];
        String text = segments[2];
        return new Message(createdBy, text, room);
    }

    /**
     * Cette fonction transforme un objet Rename en uen chaîne de caractères équivalente
     *
     * @param rename Contient la demande de renommage d'un chat.client
     * @return La chaîne de caractères constituée des informations du type rename ainsi que des pseudonymes avant et après renommage
     */
    public static String serializeRename(Rename rename) {
        return "R" + "\0" + rename.getOldPseudo() + "\0" + rename.getNewPseudo();
    }

    /**
     * Cette fonction transforme une chaine de caractères représentant une demande de renommage en l'objet Rename correspondant
     *
     * @param str La chaîne écrite par le chat.client pour demander un renommage
     * @return L'objet Rename à exploiter pour changer le pseudonyme du chat.client
     */
    public static Rename deserializeRename(String str) {
        String[] segments = str.split("\0", -1);
        String oldPseudo = segments[0];
        String newPseudo = segments[1];
        return new Rename(oldPseudo, newPseudo);
    }

    public static String serializeHello(Hello hello) {
        return "H\0" + hello.getInitialRoom() + "\0" + hello.getInitialPseudo();
    }

    public static Hello deserializeHello(String str) {
        String[] segments = str.split("\0", -1);
        String initialRoom = segments[0];
        String initialPseudo = segments[1];
        return new Hello(initialRoom, initialPseudo);
    }

    public static String serializeBye(Bye bye) {
        return "B\0" + bye.getPseudo() + "\0" + bye.getRoom();
    }

    public static Bye deserializeBye(String str) {
        String[] segments = str.split("\0", -1);
        String pseudo = segments[0];
        String room = segments[1];
        return new Bye(pseudo, room);
    }
}
