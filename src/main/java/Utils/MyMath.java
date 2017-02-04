package Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by thibault on 09/12/2016.
 */
public class MyMath {


    //range of label is [-1:1] size is 2
    public static int interval = 2;
    public static int beguining_interval = -1;
    //our initial label vary between -3 and 3
    public static int borne_min = -3;
    public static int borne_max = 3;

    public static boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x < upper;
    }
    public static ArrayList<String> days = new ArrayList<String>();
    static {
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
    }

    public static int getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getStart(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /*
        return the time predicted for a travel, label is the evaluation we get from SVM
     */
    public static int getTimesFromLabel(int label,int average){
        float time;
        if(average < 0){
            return -1;
        }
        switch (label){
            case 0:
                time = average;
                break;
            case 1:
                time = (float)(average * 1.2);
                break;
            case 2:
                time = (float)(average * 1.4);
                break;
            case -1:
                time = (float)(average * 0.8);
                break;
            case -2:
                time = (float)(average * 0.6);
                break;
            case -3:
                time = (float)(average * 0.4);
                break;
            default:
                time = (float)(average * 1.6);
        }
        return (int)time;
    }

    public static float getCheckpointLabel(int time,int mediane){
        float res = (float)time/mediane;
        int label;
        if(isBetween(res,(float)0.9,(float)1.1)){
            label = 0;
        }
        else if(isBetween(res,(float)1.1,(float)1.3)){
            label = 1;
        }
        else if(isBetween(res,(float)1.3,(float)1.5)){
            label = 2;
        }
        else if(res >= 1.5){
            label = 3;
        }
        else if(isBetween(res,(float)0.7,(float)0.9)){
            label = -1;
        }
        else if(isBetween(res,(float)0.5,(float)0.7)){
            label = -2;
        }
        else label = -3;

        float pourcentage = (label-borne_min)/(float)(borne_max-borne_min);
        return (float)((interval * pourcentage)+beguining_interval);
    }


    public static int getLabel(int time,int mediane){
        float res = (float)time/mediane;
        if(isBetween(res,(float)0.9,(float)1.1)){
            return 0;
        }
        else if(isBetween(res,(float)1.1,(float)1.3)){
            return 1;
        }
        else if(isBetween(res,(float)1.3,(float)1.5)){
            return 2;
        }
        else if(res >= 1.5){
            return 3;
        }
        else if(isBetween(res,(float)0.7,(float)0.9)){
            return -1;
        }
        else if(isBetween(res,(float)0.5,(float)0.7)){
            return -2;
        }
        else return -3;
    }

}
