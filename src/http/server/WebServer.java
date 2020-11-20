///A Simple Web Server (WebServer.java)

package http.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

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
     * WebServer constructor.
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
                BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
                PrintWriter out = new PrintWriter(remote.getOutputStream());

                Request req = new Request();

                // Lecture de l'entête
                String line = in.readLine();
                if (line == null || line.length() == 0) {
                    Response res = new Response(400);
                    out.print(res.toHttpString());
                    out.flush();
                    remote.close();
                    continue;
                }

                int indexPremierEspace = line.indexOf(" ");
                int indexDernierEspace = line.lastIndexOf(" ");
                req.setMethod(line.substring(0, indexPremierEspace));
                req.setUrl(line.substring(indexPremierEspace + 1, indexDernierEspace));

                // Lecture des headers
                line = in.readLine();
                while (line.length() > 0) {
                    String[] segments = line.split(": ", 2);
                    req.getHeaders().add(segments[0], segments[1]);
                    line = in.readLine(); // last line will be empty
                }

                if (in.ready()) {
                    char[] buffer = new char[256];
                    int returnValue = in.read(buffer, 0, buffer.length);
                    while (returnValue > 0) {
                        System.out.println(returnValue);
                        // on a lu des caractères
                        req.appendBody(new String(buffer, 0, returnValue));
                        if (!in.ready()) {
                            break;
                        }
                        returnValue = in.read(buffer, 0, buffer.length);
                    }
                }

                System.out.println(s.getInetAddress() + " " + req);

                String url = req.getUrl().replace("..", "");
                Path fullPath = Path.of(documentRoot, url);
                File file = fullPath.toFile();

                if (!file.canRead()) {
                    Response res = new Response(404);
                    System.err.println("404 " + req + "\n" + url);
                    res.setBody("Fichier non trouvé");
                    out.print(res.toHttpString());
                } else {
                    String contents = new String(Files.readAllBytes(fullPath));

                    Response res = new Response(200);
                    res.getHeaders().add("Content-Type", MimeType.getTypeForPath(fullPath));
                    res.getHeaders().add("Server", "Bot");
                    res.setBody(contents);
                    out.print(res.toHttpString());
                }

                out.flush();
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
