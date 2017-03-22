package gweltaz.calori.channelmessaging.model;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class Message
{
    private int userID;
    private String message;
    private String date;
    private String imageUrl;
    private String username;
    private String messageImageUrl;
    private String soundUrl;

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

    public String getMessageImageUrl() {
        return messageImageUrl;
    }

    public void setMessageImageUrl(String messageImageUrl) {
        this.messageImageUrl = messageImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Message(int userID, String message, String date, String imageUrl,String username,String messageImageUrl,String soundUrl) {
        this.userID = userID;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.username = username;
        this.messageImageUrl = messageImageUrl;
        this.soundUrl =soundUrl;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "userID=" + userID +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", imageUrl='" + imageUrl + '\'' +
                ", username='" + username + '\'' +
                ", messageimageurl='" + messageImageUrl + '\'' +
                ", soundUrl='" + soundUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (userID != message1.userID) return false;
        if (message != null ? !message.equals(message1.message) : message1.message != null)
            return false;
        if (date != null ? !date.equals(message1.date) : message1.date != null) return false;
        if (imageUrl != null ? !imageUrl.equals(message1.imageUrl) : message1.imageUrl != null)
            return false;
        return username != null ? username.equals(message1.username) : message1.username == null;

    }


}
