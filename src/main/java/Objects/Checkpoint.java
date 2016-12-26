package Objects;

/**
 * Created by thibault on 09/12/2016.
 */
public class Checkpoint {

    private int travel_id;
    private int id;
    private float latitude;
    private float longitude;
    private int time;

    public Checkpoint(int travel_id, int id, float latitude, float longitude, int time) {

        this.travel_id = travel_id;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public Checkpoint(int id, float latitude, float longitude, int time) {

        this.travel_id = -1;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    /*
        ID = 1
        LABEL = 0
        TRAVEL = 1
        lAT = 15.0
        LONG = 42.0
        TIME = 129
     */

    public int getTravel_id() {
        return travel_id;
    }

    public void setTravel_id(int travel_id) {
        this.travel_id = travel_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
