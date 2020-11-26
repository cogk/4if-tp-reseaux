package http.server;

import java.io.*;
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
            Response res;
            if (contents.equals("")){
                res = new Response(204);
            }
            else{
                res = new Response(200);
            }
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

    public Response putAction(BufferedInputStream inputStream, String documentRoot){
        File file = getFile(documentRoot);
        boolean existed = file.exists();
        try {
            PrintWriter in = new PrintWriter(file);
            in.close();
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[256];
            while(inputStream.available() > 0) {
                int nbRead = inputStream.read(buffer);
                fileOut.write(buffer, 0, nbRead);
            }
            fileOut.flush();
            fileOut.close();

            if (existed){
                return new Response(204);
            }
            else{
                return new Response(201);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(500);
        }
    }

    public Response postAction(BufferedInputStream inputStream, String documentRoot){
        File file = getFile(documentRoot);
        boolean existed = file.exists();
        try {
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[256];
            while(inputStream.available() > 0) {
                int nbRead = inputStream.read(buffer);
                fileOut.write(buffer, 0, nbRead);
            }
            fileOut.flush();
            fileOut.close();

            if (existed){
                return new Response(204);
            }
            else{
                return new Response(201);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(500);
        }
    }

    public Response deleteAction(String documentRoot){
        String url = this.getUrl().replace("..", "");
        Path fullPath = Path.of(documentRoot, url);
        File file = fullPath.toFile();
        boolean existed = file.exists();
        boolean deleted = false;
        if (existed && file.isFile()) {
            deleted = file.delete();
        }
        if (deleted) {
            return new Response(204);
        } else if (!existed) {
            return new Response(404);
        } else {
            return new Response(403);
        }
    }

    public File getFile(String documentRoot){
        String url = this.getUrl().replace("..", "");
        Path fullPath = Path.of(documentRoot, url);
        File file = fullPath.toFile();
        return file;
    }
}
