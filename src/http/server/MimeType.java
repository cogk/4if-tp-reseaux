package http.server;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MimeType {
    private static final Map<String, String> extensionsVersMime = new HashMap<>();

    static {
        extensionsVersMime.put("aac", "audio/aac");
        extensionsVersMime.put("abw", "application/x-abiword");
        extensionsVersMime.put("arc", "application/x-freearc");
        extensionsVersMime.put("avi", "video/x-msvideo");
        extensionsVersMime.put("azw", "application/vnd.amazon.ebook");
        extensionsVersMime.put("bin", "application/octet-stream");
        extensionsVersMime.put("bmp", "image/bmp");
        extensionsVersMime.put("bz", "application/x-bzip");
        extensionsVersMime.put("bz2", "application/x-bzip2");
        extensionsVersMime.put("csh", "application/x-csh");
        extensionsVersMime.put("css", "text/css");
        extensionsVersMime.put("csv", "text/csv");
        extensionsVersMime.put("doc", "application/msword");
        extensionsVersMime.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        extensionsVersMime.put("eot", "application/vnd.ms-fontobject");
        extensionsVersMime.put("epub", "application/epub+zip");
        extensionsVersMime.put("gz", "application/gzip");
        extensionsVersMime.put("gif", "image/gif");
        extensionsVersMime.put("htm", "text/html");
        extensionsVersMime.put("html", "text/html");
        extensionsVersMime.put("ico", "image/vnd.microsoft.icon");
        extensionsVersMime.put("ics", "text/calendar");
        extensionsVersMime.put("jar", "application/java-archive");
        extensionsVersMime.put("jpg", "image/jpeg");
        extensionsVersMime.put("jpeg", "image/jpeg");
        extensionsVersMime.put("js", "text/javascript");
        extensionsVersMime.put("json", "application/json");
        extensionsVersMime.put("jsonld", "application/ld+json");
        extensionsVersMime.put("mid", "audio/midi audio/x-midi");
        extensionsVersMime.put("midi", "audio/midi audio/x-midi");
        extensionsVersMime.put("mjs", "text/javascript");
        extensionsVersMime.put("mp3", "audio/mpeg");
        extensionsVersMime.put("mpeg", "video/mpeg");
        extensionsVersMime.put("mpkg", "application/vnd.apple.installer+xml");
        extensionsVersMime.put("odp", "application/vnd.oasis.opendocument.presentation");
        extensionsVersMime.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        extensionsVersMime.put("odt", "application/vnd.oasis.opendocument.text");
        extensionsVersMime.put("oga", "audio/ogg");
        extensionsVersMime.put("ogv", "video/ogg");
        extensionsVersMime.put("ogx", "application/ogg");
        extensionsVersMime.put("opus", "audio/opus");
        extensionsVersMime.put("otf", "font/otf");
        extensionsVersMime.put("png", "image/png");
        extensionsVersMime.put("pdf", "application/pdf");
        extensionsVersMime.put("php", "application/x-httpd-php");
        extensionsVersMime.put("ppt", "application/vnd.ms-powerpoint");
        extensionsVersMime.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        extensionsVersMime.put("rar", "application/vnd.rar");
        extensionsVersMime.put("rtf", "application/rtf");
        extensionsVersMime.put("sh", "application/x-sh");
        extensionsVersMime.put("svg", "image/svg+xml");
        extensionsVersMime.put("swf", "application/x-shockwave-flash");
        extensionsVersMime.put("tar", "application/x-tar");
        extensionsVersMime.put("tif", "image/tiff");
        extensionsVersMime.put("tiff", "image/tiff");
        extensionsVersMime.put("ts", "video/mp2t");
        extensionsVersMime.put("ttf", "font/ttf");
        extensionsVersMime.put("txt", "text/plain");
        extensionsVersMime.put("vsd", "application/vnd.visio");
        extensionsVersMime.put("wav", "audio/wav");
        extensionsVersMime.put("weba", "audio/webm");
        extensionsVersMime.put("webm", "video/webm");
        extensionsVersMime.put("webp", "image/webp");
        extensionsVersMime.put("woff", "font/woff");
        extensionsVersMime.put("woff2", "font/woff2");
        extensionsVersMime.put("xhtml", "application/xhtml+xml");
        extensionsVersMime.put("xls", "application/vnd.ms-excel");
        extensionsVersMime.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        extensionsVersMime.put("xml", "application/xml");
        extensionsVersMime.put("xul", "application/vnd.mozilla.xul+xml");
        extensionsVersMime.put("zip", "application/zip");
        extensionsVersMime.put("3gp", "video/3gpp");
        extensionsVersMime.put("3g2", "video/3gpp2");
        extensionsVersMime.put("7z", "application/x-7z-compressed");
    }

    public static String getTypeForPath(Path path) {
        String ext = getExtension(path.getFileName().toString());
        return extensionsVersMime.getOrDefault(ext, "");
    }

    private static String getExtension(String path) {
        int i = path.lastIndexOf('.');
        if (i > 0) {
            return path.substring(i + 1);
        } else {
            return "";
        }
    }
}

