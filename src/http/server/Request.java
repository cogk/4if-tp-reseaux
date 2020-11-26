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

    public String getDocumentRoot() {
        return documentRoot;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                '}';
    }

    /**
     * Ajoute un Header dans la liste des Headers.
     * @param key Clé du Header à ajouter.
     * @param value Valeur du Header à ajouter.
     */
    public void addHeader(String key, String value) {
        headers.add(key, value);
    }

    /**
     * Cette fonction permet d'obtenir le chemin absolu grâce à l'URL.
     * @return Le chemin absolu pour l'URL.
     */
    public Path getAbsolutePath() {
        return Path.of(documentRoot, getUrl().replace("..", ""));
    }
}
