package chat.modele;

public class ChatClientUtils {
    /**
     * @param line            Les données reçues à analyser
     * @param chatClientState L'état du client de messagerie
     * @return Renvoie true si le thread doit s'arrêter.
     */
    public static boolean handleIncomingData(String line, ChatClientState chatClientState, StringCallback callback) {
        // Parse the input command
        String[] arguments = line.split("\0", 2);

        String commande = arguments.length > 0 ? arguments[0] : "";
        String parametres = arguments.length > 1 ? arguments[1] : "";

        switch (commande) {
            case "M":
                Message message = Protocol.deserializeMessage(parametres);
                if (chatClientState.getRoom().equals(message.getRoom())) {
                    if (!chatClientState.getPseudo().equals(message.getCreatedBy())) {
                        callback.afficher(message.toString());
                    }
                }
                break;
            case "R":
                Rename rename = Protocol.deserializeRename(parametres);
                if (!rename.getNewPseudo().equals(chatClientState.getPseudo())) {
                    callback.afficher(rename.toString());
                }
                break;
            case "H":
                Hello hello = Protocol.deserializeHello(parametres);
                if (!hello.getInitialPseudo().equals(chatClientState.getPseudo())) {
                    callback.afficher(hello.toString());
                }
                break;
            case "B":
                Bye bye = Protocol.deserializeBye(parametres);
                if (!bye.getPseudo().equals(chatClientState.getPseudo())) {
                    callback.afficher(bye.toString());
                }
                break;
            case "E":
                callback.afficher("Erreur de protocole");
                return true; // stop the thread
            case "D":
                callback.afficher("Vous avez été déconnecté.e");
                return true; // stop the thread
            default:
                callback.afficher("Commande serveur inconnue: " + line);
                return true; // stop the thread
        }

        return false; // On continue la boucle
    }
}
