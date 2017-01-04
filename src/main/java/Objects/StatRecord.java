package Objects;

/**
 * Created by thibault on 04/01/2017.
 */
public class StatRecord {

    private int time;
    private int hour;
    private int day;


    public StatRecord(){

    }

    public StatRecord(int time, int hour) {
        this.time = time;
        this.hour = hour;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
