package Objects;

import Utils.MyMath;

/**
 * Created by thibault on 22/12/2016.
 */
public class CheckpointRecord {

    private Checkpoint checkpoint;
    private int average;
    private float label;

    public CheckpointRecord(Checkpoint checkpoint, int average) {
        this.checkpoint = checkpoint;
        this.average = average;
        this.label = MyMath.getCheckpointLabel(this.checkpoint.getTime(), average);
        System.out.println("label de merde " + this.label);
    }

    public CheckpointRecord(Checkpoint checkpoint, float _label, int _avg) {
        this.checkpoint = checkpoint;
        this.average = _avg;
        this.label = _label;
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

    public float getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
