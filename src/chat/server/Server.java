package chat.server;

public class Server {
    /**
     * main method
     *
     * @param args port
     **/
    public static void main(String args[]) {
        ChatManager chatManager = new ChatManager();
        ChatServer chatServer;

        if (args.length != 2) {
            System.out.println("Usage: java Server <port> <udp|tcp>");
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

        switch (args[1]) {
            case "udp":
                chatServer = new ChatServerUDP(port, chatManager);
                break;
            case "tcp":
                chatServer = new ChatServerTCP(port, chatManager);
                break;
            default:
                System.err.println("Usage: java Server " + port + " <udp|tcp>");
                System.exit(1);
                return;
        }
    }
}