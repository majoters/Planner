package devs.mulham.raee.sample;

/**
 * Created by jameswich on 25/2/2561.
 */

public class FriendRequestType {
    int ID;
    String name;
    public FriendRequestType(int ID,String name) {
        this.ID = ID;
        this.name=name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
