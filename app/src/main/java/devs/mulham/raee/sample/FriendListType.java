package devs.mulham.raee.sample;

/**
 * Created by jameswich on 22/2/2561.
 */

public class FriendListType {

    private String username;
    private boolean check;
    private String ID;

    public FriendListType(String username, boolean check) {
        this.username = username;
        this.check = check;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
