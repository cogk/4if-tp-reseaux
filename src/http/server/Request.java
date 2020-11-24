package http.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Request {
    private String method;

    private String url = "";

    private final Headers headers = new Headers();

    private String body = "";

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public void appendBody(String segment) {
        this.body += segment;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public Response getAction(String documentRoot){
        String url = this.getUrl().replace("..", "");
        Path fullPath = Path.of(documentRoot, url);
        File file = fullPath.toFile();
        if (!file.canRead()) {
            Response res = new Response(404);
            System.err.println("404 " + this + "\n" + url);
            res.setBody("Fichier non trouvé");
            return(res);
        } else {
            String contents = null;
            try {
                contents = new String(Files.readAllBytes(fullPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Response res = new Response(200);
            res.getHeaders().add("Content-Type", MimeType.getTypeForPath(fullPath));
            res.getHeaders().add("Server", "Bot");
            res.setBody(contents);
            return(res);
        }
    }

    public Response headAction(String documentRoot){
        String url = this.getUrl().replace("..", "");
        Path fullPath = Path.of(documentRoot, url);
        File file = fullPath.toFile();
        if (!file.canRead()) {
            Response res = new Response(404);
            System.err.println("404 " + this + "\n" + url);
            res.setBody("Fichier non trouvé");
            return(res);
        } else {
            Response res = new Response(200);
            res.getHeaders().add("Content-Type", MimeType.getTypeForPath(fullPath));
            res.getHeaders().add("Server", "Bot");
            return(res);
        }
    }

    public Response putAction(String documentRoot){
        return new Response(404);
    }

    public Response postAction(String documentRoot){
        return new Response(404);
    }

    public Response deleteAction(String documentRoot){
        return new Response(404);
    }

    public File getFile(String documentRoot){
        String url = this.getUrl().replace("..", "");
        Path fullPath = Path.of(documentRoot, url);
        File file = fullPath.toFile();
        return file;
    }
}
