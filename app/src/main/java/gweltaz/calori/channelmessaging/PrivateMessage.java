package gweltaz.calori.channelmessaging;

/**
 * Created by calorig on 27/01/2017.
 */
public class PrivateMessage
{
    private int userID;


    public String getEverRead() {
        return everRead;
    }

    public void setEverRead(String everRead) {
        this.everRead = everRead;
    }

    private String everRead;
    private int sendbyme;
    private String message;
    private String date;
    private String imageUrl;
    private String username;

    public String getUsername() {
        return username;
    }
    public int getSendbyme() {
        return sendbyme;
    }

    public void setSendbyme(int sendbyme) {
        this.sendbyme = sendbyme;
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

    public PrivateMessage(int userID, String message, String date, String imageUrl,String username,String everRead,int sendByMe) {
        this.userID = userID;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.username = username;
        this.everRead= everRead;
        this.sendbyme = sendByMe;
    }

    public PrivateMessage() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "userID=" + userID +
                ", message='" + message + '\'' +
                ", everRead='" + everRead + '\'' +
                ", date=" + date +
                ", imageUrl='" + imageUrl + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrivateMessage that = (PrivateMessage) o;

        if (userID != that.userID) return false;
        if (sendbyme != that.sendbyme) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null)
            return false;
        return username != null ? username.equals(that.username) : that.username == null;

    }


}

