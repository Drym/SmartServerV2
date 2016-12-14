package database;

import Objects.Checkpoint;
import Objects.Travel;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thibault on 09/12/2016.
 */
public class Test {

    public static void main(String[] args) {

        SQLDatabase database = new SQLDatabase("database/test.db");
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy/HH/mm/ss");
        try {
            int id1;
            int id2;
            Travel t1 = new Travel(1,date,512);
            Travel t2 = new Travel(1,dateformat.parse("17/07/2016/15/12/23"),650);
            Checkpoint checkpoint1 = new Checkpoint(1,(float)15.7,(float)42.8,129);
            Checkpoint checkpoint2 = new Checkpoint(2,(float)15.7,(float)47.8,189);

            database.deleteAll();

            database.create_database();
            id1 = database.addTravel(t1,450);
            id2 = database.addTravel(t2,450);
            database.addTravel(t2,450);

            if(id1>0 && id2>0){
                checkpoint1.setTravel_id(id1);
                checkpoint2.setTravel_id(id2);
                database.addCheckpoint(checkpoint1,120);
                database.addCheckpoint(checkpoint2,230);
            }
            System.out.println("displaying travels");
            database.displayTravel();
            System.out.println("displaying checkpoints");
            database.displayCheckpoint();
            int median1 = database.getAverageTravel();
            int median2 = database.getAverageCheckpoints(1);
            System.out.println("average travel "+ median1 + "average checkpoint" + median2);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
