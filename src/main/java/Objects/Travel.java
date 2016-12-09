package Objects;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by thibault on 09/12/2016.
 */
public class Travel {

    private int id;
    private Date date;
    private int time;

    public Travel(int id, Date date, int time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    /*
        return the number of the day in the week
     */
    public int getDay(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /*
    return the number of seconds this midnight
     */
    public int getStart(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY) * 3600 +  cal.get(Calendar.MINUTE) * 60 + cal.get(Calendar.SECOND);
    }
}
