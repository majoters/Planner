package devs.mulham.raee.sample;

/**
 * Created by jameswich on 18/2/2561.
 */

public class NearPlaceType {
    int id_data;
    String name;
    double latitude;
    double longitude;

    public NearPlaceType(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId_data() {
        return id_data;
    }

    public void setId_data(int id_data) {
        this.id_data = id_data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
