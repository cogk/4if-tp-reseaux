package http.server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandlers {

    public static void getAction(Request request, Response res) {
        Path fullPath = request.getAbsolutePath();
        if (Files.isDirectory(fullPath)) {
            fullPath = Path.of(fullPath.toString(), "index.html");
        }

        if (!Files.isReadable(fullPath)) {
            e404(res, fullPath);
            return;
        }

        byte[] contents;
        try {
            contents = Files.readAllBytes(fullPath);
        } catch (IOException e) {
            e500(res, fullPath, e);
            return;
        }

        int status = contents.length == 0 ? Status.NoContent : Status.OK;
        res.setStatus(status);
        res.addHeader("Content-Type", MimeType.getTypeForPath(fullPath));
        res.addHeader("Content-Length", Integer.toString(contents.length));
        res.addHeader("Server", "TP");
        res.end(contents);
    }

    public static void headAction(Request request, Response res) {
        Path fullPath = request.getAbsolutePath();
        if (!Files.isReadable(fullPath)) {
            e404(res, fullPath);
        } else {
            res.setStatus(Status.OK);
            res.getHeaders().add("Content-Type", MimeType.getTypeForPath(fullPath));
            long contentLength = 0;
            try {
                contentLength = Files.size(fullPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            res.addHeader("Content-Length", Long.toString(contentLength));
            res.addHeader("Server", "TP");
            res.end();
        }
    }

    public static void putAction(Request request, Response res) {
        Path fullPath = request.getAbsolutePath();
        boolean existed = Files.exists(fullPath);
        if (!Files.isWritable(fullPath)) {
            res.setStatus(Status.Forbidden);
            res.end("Le fichier n'est pas inscriptible");
            return;
        }
        if (Files.isDirectory(fullPath)) {
            res.setStatus(Status.BadRequest);
            res.end("PUT impossible sur un dossier");
            return;
        }

        try {
            InputStream inputStream = request.getInputStream();
            File file = new File(fullPath.toUri());
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
            while (inputStream.available() > 0) {
                byte[] buffer = new byte[256];
                int nRead = inputStream.read(buffer, 0, buffer.length);
                fileOut.write(buffer, 0, nRead);
            }
            fileOut.close();

            if (existed) {
                long fileSize = Files.size(fullPath);
                if (fileSize == 0) {
                    res.setStatus(Status.NoContent);
                } else {
                    res.setStatus(Status.OK);
                }
            } else {
                res.setStatus(Status.Created);
            }

            Path relativePath = fullPath.relativize(Path.of(request.getDocumentRoot()));
            res.addHeader("Content-Location", relativePath.toString());
        } catch (IOException e) {
            e500(res, fullPath, e);
        }
    }

    public static void postAction(Request request, Response res) {
        Path fullPath = request.getAbsolutePath();
        boolean existed = Files.exists(fullPath);
        try {
            InputStream inputStream = request.getInputStream();
            File file = new File(fullPath.toUri());
            BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));
            while (inputStream.available() > 0) {
                byte[] buffer = new byte[256];
                int nRead = inputStream.read(buffer, 0, buffer.length);
                fileOut.write(buffer, 0, nRead);
            }
            fileOut.close();

            if (existed) {
                long fileSize = Files.size(fullPath);
                if (fileSize == 0) {
                    res.setStatus(Status.NoContent);
                } else {
                    res.setStatus(Status.OK);
                }
            } else {
                res.setStatus(Status.Created);
            }
        } catch (IOException e) {
            e500(res, fullPath, e);
        }
    }

    public static void deleteAction(Request request, Response res) {
        Path fullPath = request.getAbsolutePath();
        boolean existed = Files.exists(fullPath);
        boolean deleted = false;
        try {
            deleted = Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (deleted) {
            res.setStatus(Status.NoContent);
            res.end();
        } else if (!existed) {
            res.setStatus(Status.NotFound);
            res.end("Ce fichier n'existe pas");
        } else {
            res.setStatus(Status.Forbidden);
            res.end("Vous n'avez pas le droit de supprimer ce fichier");
        }
    }

    private static void e404(Response res, Path fullPath) {
        System.err.println("404 " + fullPath);
        res.setStatus(Status.NotFound);
        res.end("Fichier non trouvé");
    }

    private static void e500(Response res, Path fullPath, Exception e) {
        System.err.println("500 " + fullPath);
        e.printStackTrace();
        res.setStatus(Status.InternalServerError);
        res.end("Erreur interne: " + e.getMessage());
    }
}
