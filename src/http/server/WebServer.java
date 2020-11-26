///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 * <p>
 * WebServer is a very simple web-chat.server. Any request is responded with a very
 * simple web-page.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {
    /**
     * Constructeur de WebServer.
     */
    protected void start(int port, String documentRoot) {
        ServerSocket s;

        System.out.println("Webserver starting up on port " + port);
        System.out.println("(press ctrl-c to exit)");
        try {
            // create the main chat.server socket
            s = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return;
        }

        System.out.println("Waiting for connection");
        while (true) {
            try {
                // wait for a connection
                Socket remote = s.accept();
                // remote is now the connected socket
                InputStream inputStream = remote.getInputStream();
                OutputStream outputStream = remote.getOutputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter out = new PrintWriter(remote.getOutputStream());

                // Lecture de l'entête
                String line = in.readLine();
                if (line == null || line.length() == 0) {
                    Response res = new Response(outputStream, 400);
                    res.end("Requête malformée");
                    remote.close();
                    continue;
                }

                int indexPremierEspace = line.indexOf(' ');
                int indexDernierEspace = line.lastIndexOf(' ');
                String method = line.substring(0, indexPremierEspace);
                String url = line.substring(indexPremierEspace + 1, indexDernierEspace);

                int indexOfQuestionMark = url.indexOf('?');
                String urlPathOnly = indexOfQuestionMark == -1 ? url : url.substring(0, indexOfQuestionMark);

                Request req = new Request(inputStream, method, urlPathOnly, documentRoot);

                // Lecture des headers
                line = in.readLine();
                while (line.length() > 0) {
                    String[] segments = line.split(": ", 2);
                    req.addHeader(segments[0], segments[1]);
                    line = in.readLine(); // last line will be empty
                }

                System.out.println(s.getInetAddress() + " " + req);

                Response res = new Response(outputStream);
                switch (req.getMethod()) {
                    case "GET":
                        RequestHandlers.getAction(req, res);
                        break;
                    case "HEAD":
                        RequestHandlers.headAction(req, res);
                        break;
                    case "PUT":
                        RequestHandlers.putAction(req, res);
                        break;
                    case "POST":
                        RequestHandlers.postAction(req, res);
                        break;
                    case "DELETE":
                        RequestHandlers.deleteAction(req, res);
                        break;
                    case "CONNECT":
                    case "OPTIONS":
                    case "TRACE":
                    case "PATCH":
                        res.setStatus(501);
                        res.end("Méthode non implémentée");
                        break;
                    default:
                        res.setStatus(400);
                        res.end("Requête malformée");
                }

                res.endIfNotEnded();
                remote.close();
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
        }
    }

    /**
     * Start the application.
     *
     * @param args Command line parameters are not used.
     */
    public static void main(String[] args) {
        WebServer ws = new WebServer();

        if (args.length == 2) {
            int port = Integer.parseInt(args[0]);
            String documentRoot = args[1];
            ws.start(port, documentRoot);
        } else {
            System.err.println("usage: java WebServer <port> <document root path>");
        }
    }
}
