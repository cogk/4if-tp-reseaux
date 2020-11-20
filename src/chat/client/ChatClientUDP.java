/***
 * EchoClient
 * Example of a TCP chat.client
 * Date: 10/01/04
 * Authors:
 */
package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ChatClientUDP {
    /**
     * main method
     * accepts a connection, receives a message from chat.client then sends an echo to the chat.client
     **/
    public static void main(String[] args) throws IOException {
        int serverPort;
        InetAddress serverAddress;

        DatagramSocket receiveSocket = null;
        DatagramSocket sendSocket = null;
        BufferedReader stdIn = null;

        ChatClientUDPSendThread sendThread = null;
        ChatClientUDPReceiveThread receiveThread = null;

        if (args.length != 2) {
            System.out.println("Usage: java ChatClientUDP <chat.server host> <port>");
            System.exit(1);
        }

        try {
            serverPort = Integer.parseInt(args[1]); // aussi le port de broadcast
            serverAddress = InetAddress.getLocalHost();

            receiveSocket = new DatagramSocket(null);
            receiveSocket.setReuseAddress(true);
            receiveSocket.setBroadcast(true);
            receiveSocket.bind(new InetSocketAddress(5555));
            // receiveSocket.joinGroup(InetAddress.getByName("239.255.255.250"));
            // receiveSocket.bind(new InetSocketAddress(InetAddress.getByName("::"), 5555));

            sendSocket = new DatagramSocket();
            sendSocket.setReuseAddress(true);
            sendSocket.setBroadcast(false);

            stdIn = new BufferedReader(new InputStreamReader(System.in));

            sendThread = new ChatClientUDPSendThread(sendSocket, stdIn, serverAddress, serverPort);
            receiveThread = new ChatClientUDPReceiveThread(receiveSocket);

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
            receiveThread.endThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendThread.endThread();
        receiveThread.endThread();

        sendSocket.close();
        receiveSocket.close();
    }
}


