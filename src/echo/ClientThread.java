/***
 * ClientThread
 * Example of a TCP chat.server
 * Date: 14/12/08
 * Authors:
 */

package echo;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {

    private Socket clientSocket;

    ClientThread(Socket s) {
        this.clientSocket = s;
    }

    /**
     * receives a request from chat.client then sends an echo to the chat.client
     */
    public void run() {
        try {
            BufferedReader socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());

            while (true) {
                String line = socIn.readLine();
                System.out.print("<<< " + line);
                socOut.println("echo: " + line);
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

}

  