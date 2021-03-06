package gweltaz.calori.channelmessaging.model;

/**
 * Created by calorig on 23/01/2017.
 */
public class Friends
{
    private int id;
    private String username;
    private String imageUrl;

    public Friends(String imageUrl, int id, String username) {
        this.imageUrl = imageUrl;
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
