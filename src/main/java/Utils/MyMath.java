package Utils;

/**
 * Created by thibault on 09/12/2016.
 */
public class MyMath {

    public static boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x < upper;
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
