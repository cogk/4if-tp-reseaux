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

    public void write(byte[] b) {
        if (!headersSent) {
            writeHeaders();
        }
        if (!ended) {
            try {
                outputStream.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Can't write(), already ended");
        }
    }

    public void write(String s) {
        write(s.getBytes(StandardCharsets.UTF_8));
    }

    public void writeln(String s) {
        write(s);
        write("\r\n");
    }

    public void writeln() {
        write("\r\n");
    }

    public void end(byte[] b) {
        if (!ended) {
            write(b);
            end();
        }
    }

    public void end(String s) {
        if (!ended) {
            write(s);
            end();
        }
    }

    public void end() {
        if (!ended) {
            ended = true;
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Can't end(), already ended");
        }
    }

    public void endIfNotEnded() {
        if (!ended) {
            end();
        }
    }
}
