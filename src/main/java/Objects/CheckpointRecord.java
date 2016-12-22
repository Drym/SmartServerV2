package Objects;

import Utils.MyMath;

/**
 * Created by thibault on 22/12/2016.
 */
public class CheckpointRecord {

    private Checkpoint checkpoint;
    private int average;
    private int label;

    public CheckpointRecord(Checkpoint checkpoint, int average) {
        this.checkpoint = checkpoint;
        this.average = average;
        this.label = MyMath.getLabel(this.checkpoint.getTime(),average);
    }

    public Checkpoint getCheckpoint() {

        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
