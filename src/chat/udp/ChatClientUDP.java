package chat.udp;

import chat.modele.ChatClientState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ChatClientUDP {
    /**
     * main method
     * accepts a connection, receives a message from chat.client then sends an echo to the chat.client
     **/
    public static void main(String[] args) {
        MulticastSocket socket;
        BufferedReader stdIn;

        ChatClientUDPSendThread sendThread;
        ChatClientUDPReceiveThread receiveThread;

        if (args.length != 2) {
            System.out.println("Usage: java ChatClientUDP <chat.server host> <port>");
            System.exit(1);
        }

        InetAddress groupAddr;
        try {
            groupAddr = InetAddress.getByName(args[0]);
            int groupPort = Integer.parseInt(args[1]);

            socket = new MulticastSocket(groupPort);
            // socket = new MulticastSocket(null);
            // socket.setReuseAddress(true);
            // socket.setBroadcast(true);
            // socket.bind(new InetSocketAddress(5555));
            socket.joinGroup(groupAddr);
            // socket.bind(new InetSocketAddress(InetAddress.getByName("::"), groupPort));

            stdIn = new BufferedReader(new InputStreamReader(System.in));

            ChatClientState chatClientState = new ChatClientState();

            sendThread = new ChatClientUDPSendThread(socket, stdIn, groupAddr, groupPort, chatClientState);
            receiveThread = new ChatClientUDPReceiveThread(socket, chatClientState);

            sendThread.start();
            receiveThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + args[0]);
            System.exit(1);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            // System.err.println("Couldn't get I/O for the connection to: " + args[0]);
            System.exit(1);
            return;
        }

        try {
            sendThread.join(); // on attend l'interface
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendThread.endThread();
        receiveThread.endThread();
        try {
            socket.leaveGroup(groupAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}


