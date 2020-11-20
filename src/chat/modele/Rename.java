package chat.modele;

public class Rename {
    private String oldPseudo;

    private String newPseudo;

    /**
     * Constructeur de Rename
     * @param oldPseudo Ancien pseudonyme du client
     * @param newPseudo Nouveau pseudonyme demandé par le client
     */
    public Rename(String oldPseudo, String newPseudo) {
        this.oldPseudo = oldPseudo;
        this.newPseudo = newPseudo;
    }

    public String getOldPseudo() {
        return oldPseudo;
    }

    public String getNewPseudo() {
        return newPseudo;
    }

    public void setNewPseudo(String newPseudo) {
        this.newPseudo = newPseudo;
    }

    @Override
    public String toString() {
        return "<" + oldPseudo + "> s'est renommé <" + newPseudo + ">";
    }
}
