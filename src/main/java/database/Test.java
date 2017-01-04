package database;

import Objects.Checkpoint;
import Objects.CheckpointRecord;
import Objects.Travel;
import Objects.TravelRecord;
import Utils.SvmManager;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by thibault on 09/12/2016.
 */
public class Test {

    public static void main(String[] args) {


        SQLDatabase database = new SQLDatabase("src/main/java/database/test.db"); //database/test.db
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy/HH/mm/ss");
        try {
            int id1;
            int id2;
            Travel t1 = new Travel(1,date,512);
            Travel t2 = new Travel(1,dateformat.parse("17/07/2016/15/12/23"),650);
            Checkpoint checkpoint1 = new Checkpoint(0,(float)15.7,(float)42.8,132);
            Checkpoint checkpoint2 = new Checkpoint(1,(float)15.7,(float)47.8,189);
            Checkpoint checkpoint3 = new Checkpoint(0,(float)15.7,(float)42.8,219);


            database.deleteAll();
            database.create_database();
            id1 = database.addTravel(t1);
            id2 = database.addTravel(t2);

            if(id1>0 && id2>0){
                checkpoint1.setTravel_id(id1);
                checkpoint2.setTravel_id(id2);
                checkpoint3.setTravel_id(id2);
                database.addCheckpoint(checkpoint1);
                database.addCheckpoint(checkpoint2);
                database.addCheckpoint(checkpoint3);
            }
            System.out.println("displaying travels");
            database.displayTravel();
            System.out.println("displaying checkpoints");
            database.displayCheckpoint();
            int median1 = database.getAverageTravel();
            int median2 = database.getAverageCheckpoints(0);
            int mean1 = database.getMeanCheckponts(1);
            int mean2 = database.getMeanTravel();
            int mean3 = database.getMeanTravelbyDay(1);
            int min = database.minTravelbyDay(1);
            System.out.println("average travel "+ median1 + " average checkpoint " + median2);
            System.out.println("mean travel " + mean2 + " mean checkpoint " + mean1 + " mean travel by day " + mean3 + " mintravel " + min);
            System.out.println("number of checkpoints " + database.getNumberOfCheckpoints());
            System.out.println("write records:");
            database.writeRecords();
            System.out.println("average test " + database.getAverage(0));
            database.train();
            database.close();


            //prediction
            ArrayList<CheckpointRecord> records = new ArrayList<>();
            records.add(new CheckpointRecord(new Checkpoint(0,(float)15.7,(float)47.8,140),database.getAverage(0)));
            records.add(new CheckpointRecord(new Checkpoint(1,(float)15.7,(float)47.8,160),database.getAverage(1)));
            int prediction = SvmManager.predict(new TravelRecord(16,3,records));
            System.out.println("prediction " + prediction);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
