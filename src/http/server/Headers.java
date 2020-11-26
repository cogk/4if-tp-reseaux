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
    public String toString() {
        StringBuilder out = new StringBuilder();
        int i = 0;
        for (Header header : headers) {
            if (i > 0) {
                out.append(", ");
            }
            i++;
            out.append(header.toString());
        }
        return out.toString();
    }

    protected List<Header> getList() {
        return headers;
    }
}
