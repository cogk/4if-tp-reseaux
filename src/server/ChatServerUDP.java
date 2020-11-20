package server;

import modele.Message;
import modele.Protocol;
import modele.Rename;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class ChatServerUDP implements ChatServer {
    /**
     * Port sur lequel le serveur écoute
     */
    private final int serverPort;
    private DatagramSocket socket;
    //private MulticastSocket socket;

    public ChatServerUDP(int port, ChatManager chatManager) {
        chatManager.setChatServer(this);

        this.serverPort = port;

        try {
            // this.socket = new MulticastSocket(null);
            this.socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
            // socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), serverPort));
            socket.bind(new InetSocketAddress(serverPort));

            System.out.println("Server ready...");

            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    if (socket.isClosed()) {
                        System.err.println("Socket closed");
                        break;
                    }

                    DatagramPacket incomingDatagramPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(incomingDatagramPacket);
                    onData(incomingDatagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in ChatServerUDP:" + e);
        }
    }

    private void onData(DatagramPacket incomingDatagramPacket) {
        String line = new String(
                incomingDatagramPacket.getData(),
                incomingDatagramPacket.getOffset(),
                incomingDatagramPacket.getLength()
        );

        System.out.println(incomingDatagramPacket.getAddress()
                + ":" + incomingDatagramPacket.getPort() + " > " + line);

        // Parse the input command
        String[] arguments = line.split("\u0000", 2);
        String commande = arguments.length > 0 ? arguments[0] : "";
        String parametres = arguments.length > 1 ? arguments[1] : "";

        switch (commande) {
            case "msg":
                Message message = Protocol.deserializeMessage(parametres);
                sendBroadcast(Protocol.serializeMessage(message));
                break;
            case "rename":
                Rename rename = Protocol.deserializeRename(parametres);
                sendBroadcast(Protocol.serializeRename(rename));
                break;
            default:
                System.err.println("? " + line.replace("\u0000", "\\0"));
        }
    }

    public void pushMessage(Message msg) {
        /*InetSocketAddress group = null;
        try {
            group = new InetSocketAddress(InetAddress.getLocalHost(), port);
            socket.joinGroup(group, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // send(Protocol.writeMessage(msg));

        /*try {
            socket.leaveGroup(group, NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();*/
    }

    public void pushRename(Rename rename) {
        // send(Protocol.writeRename(rename));
    }

    private void reply(String s, InetAddress replyAdress, int replyPort) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(null);
            socket.setReuseAddress(false);
            socket.connect(new InetSocketAddress(replyAdress, replyPort));
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] buf = s.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, replyAdress, replyPort);
        try {
            assert socket != null;
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        socket.close();
    }

    private void sendBroadcast(String s) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress broadcast = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback())
                    continue; // Do not want to use the loopback interface.
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    broadcast = interfaceAddress.getBroadcast();
                    if (broadcast != null) {
                        break;
                    }

                    // Do something with the address.
                }
            }

            if (broadcast != null) {
                int port = 5555; // le port de broadcast
                InetAddress addr = InetAddress.getByAddress(broadcast.getAddress());
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(false);
                    socket.connect(new InetSocketAddress(addr, port));

                    byte[] buf = s.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
                    socket.send(packet);

                    System.out.println("broadcast: " + addr.toString() + ":" + port + " " + s);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
