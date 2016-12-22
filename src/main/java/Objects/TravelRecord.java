package Objects;

import Utils.MyMath;
import libsvm.svm_node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thibault on 20/12/2016.
 */
public class TravelRecord {

    private int start_hour;
    private int day_of_week;
    private ArrayList<CheckpointRecord> checkpoints;
    private int label = -1;

    public TravelRecord(int start_hour, int day_of_week, ArrayList<CheckpointRecord> checkpoints, int label) {
        this.start_hour = start_hour;
        this.day_of_week = day_of_week;
        this.checkpoints = checkpoints;
        this.label = label;
    }

    public TravelRecord(int start_hour, int day_of_week, ArrayList<CheckpointRecord> checkpoints) {
        this.start_hour = start_hour;
        this.day_of_week = day_of_week;
        this.checkpoints = checkpoints;
    }

    @Override
    public String toString() {
        String sample = "";
        sample += this.getLabel() + " 1:" + this.getDay_of_week();
        sample += " 2:" + this.getStart_hour();
        for(CheckpointRecord i : checkpoints){
            //System.out.println("value " + i + "average" + average);
            sample += " " + (i.getCheckpoint().getId()+3) + ":";
            //compute the class of the checkpoint [-3,3]
            sample += i.getLabel();
        }
        return  sample;
    }


    public svm_node[] getNodes(){
        int size = 2 + checkpoints.size();
        svm_node[] res = new svm_node[size];
        for(int i=0;i<size;i++) {
            res[i] = new svm_node();
        }
        res[0].index = 0;
        res[0].value = day_of_week;
        res[1].index = 1;
        res[1].value = start_hour;
        for(int i = 2;i<size;i++){
            res[i].index = checkpoints.get(i-2).getCheckpoint().getId();
            res[i].value = checkpoints.get(i-2).getLabel();
        }
        return res;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
