package gweltaz.calori.channelmessaging;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class User
{
    private int userID;
    private String identifiant;
    private int lastactivity;
    private String imageUrl;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public int getLastactivity() {
        return lastactivity;
    }

    public void setLastactivity(int lastactivity) {
        this.lastactivity = lastactivity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", identifiant='" + identifiant + '\'' +
                ", lastactivity=" + lastactivity +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public User() {
    }

    public User(int userID, String identifiant, int lastactivity, String imageUrl) {
        this.userID = userID;
        this.identifiant = identifiant;
        this.lastactivity = lastactivity;
        this.imageUrl = imageUrl;
    }
}
