package http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private int status = 200;

    private final Headers headers = new Headers();

    private final OutputStream outputStream;

    private boolean headersSent = false;

    private boolean ended = false;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response(OutputStream outputStream, int status) {
        this.outputStream = outputStream;
        this.status = status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        return Status.getStatusTextForCode(this.status);
    }

    public Headers getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    /**
     * Cette fonction écrit les headers s'ils n'ont pas déjà été écrits.
     */
    public void writeHeaders() {
        if (headersSent) {
            System.err.println("Headers already sent");
            return;
        }
        headersSent = true;

        final String HTTP_VERSION = "1.0";
        writeln("HTTP/" + HTTP_VERSION + " " + status + " " + getStatusText());
        for (Header header : headers.getList()) {
            writeln(header.toString());
        }
        writeln();
    }

    /**
     * Cette fonction permet d'écrire sur le Stream d'output.
     *
     * @param b Chaîne de bits à écrire.
     */
    public void write(byte[] b) {
        if (!headersSent) {
            writeHeaders();
        }
        if (!ended) {
            try {
                outputStream.write(b);
                outputStream.flush();
            } catch (IOException e) {
                // e.printStackTrace();
                end();
            }
        } /*else {
            System.err.println("Can't write(), already ended");
        }*/
    }

    /**
     * Cette fonction permet de transformer une chaîne de caractères en chaînes de bits pour l'écrire sur le Stream d'output
     *
     * @param s Chaîne de caractères à écrire.
     */
    public void write(String s) {
        write(s.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Cette fonction permet d'écrire une chaîne de caractères et un passage à la ligne.
     *
     * @param s Chaîne de caractères à écrire.
     */
    public void writeln(String s) {
        write(s);
        write("\r\n");
    }

    /**
     * Cette fonction permet d'écrire un retour à la ligne.
     */
    public void writeln() {
        write("\r\n");
    }

    /**
     * Cette fonction écrit la chaîne de bits fournie puis met fin au Stream.
     *
     * @param b Chaîne de bits à écrire.
     */
    public void end(byte[] b) {
        if (!ended) {
            write(b);
            end();
        }
    }

    /**
     * Cette fonction écrit la chaîne de caractères fournie puis met fin au Stream.
     *
     * @param s Chaîne de caractères à écrire.
     */
    public void end(String s) {
        if (!ended) {
            write(s);
            end();
        }
    }

    /**
     * Cette fonction termine le Stream.
     */
    private void end() {
        if (!ended) {
            ended = true;
            try {
                outputStream.close(); // flush aussi
            } catch (IOException e) {
                e.printStackTrace();
            }
        } /*else {
            System.err.println("Can't end(), already ended");
        }*/
    }

    /**
     * Cette fonction termine le Stream s'il n'est pas déjà terminé.
     */
    public void endIfNotEnded() {
        if (!ended) {
            end();
        }
    }
}
