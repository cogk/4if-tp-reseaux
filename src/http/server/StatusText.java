package http.server;

public class StatusText {
    public static String getStatusTextForCode(int code) {
        switch (code) {
            case 200: return "OK";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            default: return "";
        }
    }
}
