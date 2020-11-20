/***
 * EchoClient
 * Example of a TCP chat.client
 * Date: 10/01/04
 * Authors:
 */
package chat.client;

import java.io.*;
import java.net.*;

public class ChatClientTCP {
    /**
     * main method
     * accepts a connection, receives a message from chat.client then sends an echo to the chat.client
     **/
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

        ChatClientTCPSendThread sendThread = null;
        ChatClientTCPReceiveThread receiveThread = null;

        if (args.length != 2) {
            System.out.println("Usage: java ChatClient <chat.server host> <port>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            socket = new Socket(args[0], Integer.parseInt(args[1]));
            socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socOut = new PrintStream(socket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            sendThread = new ChatClientTCPSendThread(socOut, stdIn);
            receiveThread = new ChatClientTCPReceiveThread(socIn);

            sendThread.start();
            receiveThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + args[0]);
            System.exit(1);
        }

        try {
            sendThread.join(); // on attend l'interface
            receiveThread.endThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendThread.endThread();
        receiveThread.endThread();

        socket.close();
        socIn.close();
        socOut.close();
    }
}


