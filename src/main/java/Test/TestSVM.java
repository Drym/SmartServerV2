package Test;

import Objects.Checkpoint;
import Objects.CheckpointRecord;
import Objects.TravelRecord;
import Utils.MyMath;
import Utils.SvmManager;
import Utils.svm_predict;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import database.SQLDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thibault on 30/01/2017.
 */
public class TestSVM {


    public static SQLDatabase database;
    public static String test_path = "src/ressources/test";
    public static String model_path = "src/ressources/model";
    public static String ouput_path = "src/ressources/output";

    public static void main(String[] args) {
        database = new SQLDatabase("src/main/java/database/test.db");
        try {
            System.out.println("average:" + database.getAverageTravel() + " " + database.getAverageCheckpoints(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ArrayList<CheckpointRecord> checkpoints = new ArrayList<CheckpointRecord>();
        int travel_time = 0;
        for(int i=0; i< 3; i++){
            checkpoints.add(new CheckpointRecord(new Checkpoint(i,40.0f,40.0f,135),100));
        }
        //TravelRecord travel = new TravelRecord(MyMath.getStart(date),MyMath.getDay(date),checkpoints);
        TravelRecord travel = new TravelRecord(17,3,checkpoints);
        System.out.println(travel);
        int result = 0;
        try {
            result = SvmManager.predict(travel);
            //result = MyMath.getTimesFromLabel(result,database.getAverageTravel());
            System.out.println("res " + result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String argv[] = {test_path,model_path,ouput_path};

        try {
            svm_predict.run(argv);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
