package http.server;

import java.io.*;
import java.nio.file.Path;

public class Request {
    private final InputStream inputStream;

    private final String method;

    private final String url;

    private final String documentRoot;

    private final Headers headers = new Headers();

    // Constructor
    public Request(InputStream inputStream, String method, String url, String documentRoot) {
        this.inputStream = inputStream;
        this.method = method;
        this.url = url;
        this.documentRoot = documentRoot;
    }

    // Getters/Setters
    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Headers getHeaders() {
        return headers;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                '}';
    }

    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    public Path getAbsolutePath() {
        return Path.of(documentRoot, getUrl().replace("..", ""));
    }

    public String getDocumentRoot() {
        return documentRoot;
    }
}
