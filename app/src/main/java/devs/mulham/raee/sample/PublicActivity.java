package devs.mulham.raee.sample;

/**
 * Created by jameswich on 18/2/2561.
 */

public class PublicActivity {

    int BaseAccept;
    String PostDate;
    String Detail;
    String Time;
    String LocationName;
    double latitude;
    double longitude;
    int User_ID;
    String Username;
    String firebasekey;
    int Joiner;

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }


    public PublicActivity(int User_ID,String Username,String key) {
        this.User_ID=User_ID;
        this.Username=Username;
        this.firebasekey=key;
    }

    public int getJoiner() {
        return Joiner;
    }

    public void setJoiner(int joiner) {
        Joiner = joiner;
    }

    public String getFirebasekey() {
        return firebasekey;
    }

    public void setFirebasekey(String firebasekey) {
        this.firebasekey = firebasekey;
    }

    public int getBaseAccept() {
        return BaseAccept;
    }

    public void setBaseAccept(int baseAccept) {
        BaseAccept = baseAccept;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
