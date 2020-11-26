package chat;

import chat.modele.ChatManager;
import chat.tcp.ChatServerTCP;

public class Main {
    /**
     * main method
     *
     * @param args port
     **/
    public static void main(String[] args) {
        ChatManager chatManager = new ChatManager();

        if (args.length != 1) {
            System.out.println("Usage: java Server <port>");
            System.exit(1);
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("port invalide");
            return;
        }

        new ChatServerTCP(port, chatManager);
    }
}