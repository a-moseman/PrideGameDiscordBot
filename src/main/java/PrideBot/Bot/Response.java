package PrideBot.Bot;

public class Response {
    public final String TARGET_UUID;
    public final String[] MESSAGES;

    public Response(String message) {
        this.TARGET_UUID = null;
        this.MESSAGES = new String[]{message};
    }

    public Response(String targetUUID, String message) {
        this.TARGET_UUID = targetUUID;
        this.MESSAGES = new String[]{message};
    }

    public Response(String[] messages) {
        this.TARGET_UUID = null;
        this.MESSAGES = messages;
    }

    public Response(String targetUUID, String[] messages) {
        this.TARGET_UUID = targetUUID;
        this.MESSAGES = messages;
    }
}
