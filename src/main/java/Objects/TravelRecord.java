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

    public static int number_of_checkpoint = 19;

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
        format();
        String sample = "";
        sample += this.getLabel() + " 1:" + (float)this.getDay_of_week()/6;
        sample += " 2:" + (float)this.getStart_hour()/24;
        for(CheckpointRecord i : checkpoints){
            //System.out.println("value " + i + "average" + average);
            sample +=  " " +(i.getCheckpoint().getId()+3) + ":";
            //compute the class of the checkpoint [-3,3]
            sample += i.getLabel();

        }
        return  sample;
    }


    public svm_node[] getNodes(){
        format();
        int size = checkpoints.size();
        svm_node[] res = new svm_node[size];
        for(int i=0;i<size;i++) {
            res[i] = new svm_node();
        }
        res[0] = new svm_node();
        res[0].index = 1;
        res[0].value = (float)day_of_week/6;
        res[1] = new svm_node();
        res[1].index = 2;
        res[1].value = (float)start_hour/24;
        for(int i = 2;i<size;i++){
            res[i] = new svm_node();
            res[i].index = checkpoints.get(i-2).getCheckpoint().getId() + 3;
            res[i].value = checkpoints.get(i-2).getLabel();
        }
        for(int i = 0;i< size; i++){
            System.out.println(res[i].index + ": " + res[i].value);
        }
        return res;
    }

    public void format(){
        int len = checkpoints.size();
        if(len < number_of_checkpoint){
            float avg = average_label();
            int nb = number_of_checkpoint - checkpoints.size();
            for(int i=len;i < number_of_checkpoint; i++){
                checkpoints.add(new CheckpointRecord(new Checkpoint(i,-1,-1,-1),avg,-1));
            }
        }
    }

    public float average_label(){
        float res;
        float cmp = 0;
        int nb = checkpoints.size();
        for(int i=0; i<nb; i++){
            cmp += checkpoints.get(i).getLabel();
        }
        res = cmp / nb;
        System.out.println("average compute " + res);
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
