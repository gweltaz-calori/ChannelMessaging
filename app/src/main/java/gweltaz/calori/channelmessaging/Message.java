package gweltaz.calori.channelmessaging;

import java.util.Date;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class Message
{
    private int userID;
    private String message;
    private String date;
    private String imageUrl;


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

    public Message(int userID, String message, String date, String imageUrl) {
        this.userID = userID;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
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
                '}';
    }
}
