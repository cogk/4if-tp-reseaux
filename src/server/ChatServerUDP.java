package server;

import modele.Message;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServerUDP implements ChatServer {
    private final int port;
    // private DatagramSocket socket;
    private MulticastSocket socket;

    public ChatServerUDP(int port, ChatManager chatManager) {
        this.port = port;

        try {
            this.socket = new MulticastSocket(null);
            // socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port));

            System.out.println("Server ready...");

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket incomingDatagramPacket = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(incomingDatagramPacket);
                    System.out.println("UDP datagram from:" + socket.getInetAddress());
                    System.out.println(incomingDatagramPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in ChatServerUDP:" + e);
        }
    }

    public void pushMessage(Message msg) {
        InetSocketAddress group = null;
        try {
            group = new InetSocketAddress(InetAddress.getByName("localhost"), port);
            socket.joinGroup(group, NetworkInterface.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DatagramPacket packet;
        for (int i = 0; i < 5; i++) {
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String received = new String(packet.getData());
            System.out.println("Quote of the Moment: " + received);
        }

        try {
            socket.leaveGroup(group, NetworkInterface.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    public void pushRename(String oldPseudo, String newPseudo) {
    }
}
