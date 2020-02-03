package android.app.mobilebattery.Pojo;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class ChatListData {

    private String from;
    private String to;
    private String message;
    private Long timestamp;

    public ChatListData(String from, String message, Long timestamp, String to) {
        this.from = from;
        this.message = message;
        this.timestamp = timestamp;
        this.to = to;
    }

    public ChatListData() {
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
