package modele;

public class Rename {
    private String oldPseudo;

    private String newPseudo;

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
