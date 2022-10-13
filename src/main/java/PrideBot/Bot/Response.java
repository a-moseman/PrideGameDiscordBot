package PrideBot.Bot;

public class Response {
    public final String TARGET_UUID;
    public final String TYPE;
    public final String[] MESSAGES;

    public Response(String type, String message) {
        this.TARGET_UUID = null;
        this.TYPE = type;
        this.MESSAGES = new String[]{message};
    }

    public Response(String targetUUID, String type, String message) {
        this.TARGET_UUID = targetUUID;
        this.TYPE = type;
        this.MESSAGES = new String[]{message};
    }

    public Response(String type, String[] messages) {
        this.TARGET_UUID = null;
        this.TYPE = type;
        this.MESSAGES = messages;
    }

    public Response(String targetUUID, String type, String[] messages) {
        this.TARGET_UUID = targetUUID;
        this.TYPE = type;
        this.MESSAGES = messages;
    }
}
