package android.app.mobilebattery.Pojo;

public class ChatMaster {

    private String chatUID;
    private String from;
    private String fromDisplayName;
    private String image;
    private String lastMessage;
    private Long timestamp;
    private String to;
    private String toDisplayName;


    public ChatMaster() {
    }

    public ChatMaster(String chatUID, String from, String fromDisplayName, String image, String lastMessage, Long timestamp, String to, String toDisplayName) {
        this.chatUID = chatUID;
        this.from = from;
        this.fromDisplayName = fromDisplayName;
        this.image = image;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.to = to;
        this.toDisplayName = toDisplayName;
    }

    public String getChatUID() {
        return chatUID;
    }

    public void setChatUID(String chatUID) {
        this.chatUID = chatUID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromDisplayName() {
        return fromDisplayName;
    }

    public void setFromDisplayName(String fromDisplayName) {
        this.fromDisplayName = fromDisplayName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToDisplayName() {
        return toDisplayName;
    }

    public void setToDisplayName(String toDisplayName) {
        this.toDisplayName = toDisplayName;
    }
}
