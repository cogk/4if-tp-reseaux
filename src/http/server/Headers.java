package http.server;

import java.util.ArrayList;
import java.util.List;

public class Headers {
    private final List<Header> headers = new ArrayList<>();

    /**
     * Cette fonction permet d'ajouter un Header dans la liste
     * @param key Nom du Header
     * @param value Valeur associée au Header
     */
    public void add(String key, String value) {
        headers.add(new Header(key, value));
    }

    /**
     * Cette fonction permet de transformer la liste headers en chaîne de caractères lisible
     * @return La chaîne décrivant les headers
     */
    public String toHttpString() {
        final String CRLF = "\r\n";

        StringBuilder out = new StringBuilder();
        for (Header header : this.headers) {
            out.append(header.toString()).append(CRLF);
        }

        return out.toString();
    }

    protected List<Header> getList() {
        return headers;
    }
}
