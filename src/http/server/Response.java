package http.server;

import java.io.PrintWriter;

public class Response {
    private int status;

    private final Headers headers = new Headers();

    private String body = "";

    public Response(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return StatusText.getStatusTextForCode(this.status);
    }

    public int getStatus() {
        return status;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toHttpString() {
        final String HTTP_VERSION = "1.0";
        final String CRLF = "\r\n";

        String out = "";
        out += "HTTP/" + HTTP_VERSION + " " + status + " " + getStatusText() + CRLF;
        out += headers.toHttpString();
        out += CRLF;
        out += body;

        return out;
    }
}
