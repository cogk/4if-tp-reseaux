package http.server;

import java.util.ArrayList;
import java.util.List;

public class Headers {
    private final List<Header> headers = new ArrayList<>();

    public void add(String key, String value) {
        headers.add(new Header(key, value));
    }

    public String toHttpString() {
        final String CRLF = "\r\n";

        StringBuilder out = new StringBuilder();
        for (Header header : this.headers) {
            out.append(header.toString()).append(CRLF);
        }

        return out.toString();
    }
}
