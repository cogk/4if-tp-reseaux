package http.server;

public class Status {
    public static final int OK = 200;
    public static final int Created = 201;
    public static final int NoContent = 204;
    public static final int BadRequest = 400;
    public static final int Forbidden = 403;
    public static final int NotFound = 404;
    public static final int InternalServerError = 500;
    public static final int NotImplemented = 501;

    /**
     * Cette fonction renvoie le statut correspondant au code fourni.
     * @param code Correspond au code numérique du statut.
     * @return Le message correspondant au code recherché.
     */
    public static String getStatusTextForCode(int code) {
        switch (code) {
            case 200: return "OK";
            case 201: return "Created";
            case 204: return "No Content";
            case 400: return "Bad Request";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 500: return "Internal Server Error";
            case 501: return "Not Implemented";
            default: return "";
        }
    }
}
