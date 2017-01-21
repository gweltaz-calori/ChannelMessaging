package gweltaz.calori.channelmessaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class Users
{
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Users() {
        this.users = new ArrayList<>();
    }
}
