package chat.tcp;

import chat.modele.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;
import java.util.Random;

public class ChatClientGUIWindow {
    private static final Color[] couleurs = {
            Color.getHSBColor(0.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(45.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(90.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(135.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(180.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(225.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(270.0f/360, 0.1f, 0.99f),
            Color.getHSBColor(315.0f/360, 0.1f, 0.99f),
    };

    private final TextArea textAreaHistorique;
    private final TextField entreeTextuelle;
    private final Button boutonEnvoyer;
    private final Frame frame;
    private final PrintStream socketOutput;
    private final ChatClientState chatClientState;

    public ChatClientGUIWindow(PrintStream socketOutput, ChatClientState chatClientState) {
        this.socketOutput = socketOutput;
        this.chatClientState = chatClientState;

        frame = new Frame();
        frame.setSize(800, 600);
        frame.setMinimumSize(new Dimension(200, 300));
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                recalculerTailles();
            }
        });

        // Historique de conversation
        textAreaHistorique = new TextArea();
        textAreaHistorique.setEditable(false); // lecture seule
        final Color couleur = couleurs[new Random().nextInt(couleurs.length)];
        textAreaHistorique.setBackground(couleur);
        frame.add(textAreaHistorique);

        // Entrée
        entreeTextuelle = new TextField();
        frame.add(entreeTextuelle);

        // Bouton
        boutonEnvoyer = new Button("Envoyer");
        frame.add(boutonEnvoyer);

        recalculerTailles();

        entreeTextuelle.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    envoyer();
                }
            }
        });
        boutonEnvoyer.addActionListener((clickEvent) -> envoyer());
    }

    public void fermer() {
        frame.dispose();
        System.exit(0);
    }

    public boolean estOuverte() {
        return frame.isVisible();
    }

    private void recalculerTailles() {
        final int W = frame.getWidth();
        final int H = frame.getHeight();

        Insets insets = frame.getInsets();
        final int largeur = W - insets.right - insets.left;
        final int hauteur = H - insets.bottom - insets.top;

        final int windowPadding = 8;
        final int margin = 8;

        final int largeurBouton = 80;
        final int hauteurBouton = 35;

        final int largeurEntree = largeur - largeurBouton - margin - windowPadding * 2;
        final int hauteurEntree = 35;

        final int largeurTexte = largeur - windowPadding * 2;
        final int hauteurTexte = hauteur - Math.max(hauteurBouton, hauteurEntree) - margin * 2 - windowPadding;

        textAreaHistorique.setBounds(insets.left + windowPadding, insets.top + windowPadding, largeurTexte, hauteurTexte);
        entreeTextuelle.setBounds(insets.left + windowPadding, insets.top + hauteur - insets.bottom - windowPadding - hauteurEntree, largeurEntree, hauteurEntree);
        boutonEnvoyer.setBounds(insets.left + largeur - largeurBouton - windowPadding, insets.top + hauteur - insets.bottom - hauteurBouton - windowPadding, largeurBouton, hauteurBouton);
    }

    public void appendText(String t) {
        textAreaHistorique.setText(textAreaHistorique.getText() + t + "\n");
    }

    private void envoyer() {
        String line = entreeTextuelle.getText().trim(); // on récupère le texte entré
        if (line.length() == 0) {
            return;
        }

        entreeTextuelle.setText(""); // on efface le champ

        String pseudo = chatClientState.getPseudo();
        String room = chatClientState.getRoom();

        if (line.equals("/quit") || line.equals("/dc") || line.equals("/bye")) {
            System.out.println("* Déconnexion");
            Bye bye = new Bye(chatClientState.getPseudo(), chatClientState.getRoom());
            socketOutput.println(Protocol.serializeBye(bye));
            fermer();
        } else if (line.startsWith("/pseudo ")) {
            String newPseudo = line.substring(8);
            socketOutput.println(Protocol.serializeRename(new Rename(pseudo, newPseudo)));
            chatClientState.setPseudo(newPseudo);
            appendText("Votre nouveau pseudo est <" + newPseudo + ">");
        } else if (line.startsWith("/clear")) {
            textAreaHistorique.setText("");
        } else if (line.startsWith("/room ") || line.equals("/room")) {
            String newRoom = line.equals("/room") ? "" : line.substring(6).trim();
            if (room.equals(newRoom)) {
                appendText("* Commande ignorée");
            } else if (room.length() == 0) {
                appendText("* Vous avez quitté la salle principale et rejoint #" + newRoom);
            } else if (newRoom.length() == 0) {
                appendText("* Vous avez quitté #" + room + " et rejoint la salle principale");
            } else {
                appendText("* Vous avez quitté #" + room + " et rejoint #" + newRoom);
            }
            chatClientState.setRoom(newRoom);
        } else {
            // Il s'agit d'un message textuel
            Message message = new Message(pseudo, line, room);
            appendText(message.toString());
            socketOutput.println(Protocol.serializeMessage(message));
        }
    }
}