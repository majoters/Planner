package devs.mulham.raee.sample;

/**
 * Created by supakorn on 28/11/2560.
 */

public class List_Database {

    private int ID=-1;
    private int Date;
    private int Time ;
    private String Description;
    private String locationName;
    private double latitude;
    private double longitude;
    private int share;
    private int arrive;
    private int important;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List_Database(String Description){
        this.Description=Description;
    }

    public List_Database(int Date,int Time,String Description,String locationName,double latitude,double longitude,int share){
        this.Date=Date;
        this.Time=Time;
        this.Description=Description;
        this.locationName=locationName;
        this.latitude=latitude;
        this.longitude=longitude;
        setStatus(share);
    }

    public List_Database(int Date,int Time,String Description,String locationName,double latitude,double longitude,int share,boolean arrive){
        this.Date=Date;
        this.Time=Time;
        this.Description=Description;
        this.locationName=locationName;
        this.latitude=latitude;
        this.longitude=longitude;
        setStatus(share);
        this.arrive = arrive? 1 : 0;
    }
    public List_Database(int Date,int Time,String Description,String locationName,double latitude,double longitude,int share,int arrive){
        this.Date=Date;
        this.Time=Time;
        this.Description=Description;
        this.locationName=locationName;
        this.latitude=latitude;
        this.longitude=longitude;
        setStatus(share);
        this.arrive = arrive;
    }

    public List_Database(int Date,int Time,String Description,String locationName,double latitude,double longitude,int share,boolean important,boolean arrive){
        this.Date=Date;
        this.Time=Time;
        this.Description=Description;
        this.locationName=locationName;
        this.latitude=latitude;
        this.longitude=longitude;
        setStatus(share);
        this.arrive = arrive? 1 : 0;
        this.important = important? 1 : 0;
    }
    public List_Database(int Date,int Time,String Description,String locationName,double latitude,double longitude,int share,int important,int arrive){
        this.Date=Date;
        this.Time=Time;
        this.Description=Description;
        this.locationName=locationName;
        this.latitude=latitude;
        this.longitude=longitude;
        setStatus(share);
        this.arrive = arrive;
        this.important = important;
    }

    public int getStatus() {
        return share;
    }

    public void setStatus(int share) {
        this.share=share;
    }

    public int getDate(){return Date;}

    public void setDate(int Date){this.Date = Date;}

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public boolean getArrive() {
        boolean result = (arrive != 0);
        return result;
    }
    public int getIntArrive() {
        return arrive;
    }

    public void setArrive(boolean arrive) {
        this.arrive = arrive? 1 : 0;
    }

    public boolean getImportant() {
        boolean result = (important != 0);
        return result;
    }
    public int getIntImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important? 1 : 0;
    }
}
