package database;

import Objects.Checkpoint;
import Objects.CheckpointRecord;
import Objects.Travel;
import Objects.TravelRecord;
import Utils.MyMath;
import Utils.SvmManager;
import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by thibault on 09/12/2016.
 */
public class SQLDatabase {

    Connection c = null;
    SvmManager svm;

    public SQLDatabase(String path) {
        c = null;
        svm = new SvmManager();
        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            c = DriverManager.getConnection("jdbc:sqlite:" + path,config.toProperties());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void create_database() throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        String travel = "CREATE TABLE if not exists TRAVEL " +
                "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                " START     INT     NOT NULL, " +
                " DAY       INT     NOT NULL, " +
                " TIME      INT     NOT NULL)";
        String checkpoint = "CREATE TABLE if not exists CHECKPOINT " +
                "(ID INT    NOT NULL," +
                " TRAVEL         INT, " +
                " LAT       REAL    NOT NULL, " +
                " LONG      REAL     NOT NULL, " +
                " TIME      INT     NOT NULL," +
                "FOREIGN KEY(TRAVEL) REFERENCES TRAVEL(ID))"
        ;
        stmt.executeUpdate(travel);
        stmt.executeUpdate(checkpoint);
        stmt.close();
    }

    /*
    Ajoute le trajet dans la table, la classe est calculée par rapport à la médiane, time/mediane en pourcentage
    [0-50] -> -3
    [50-70] -> -2
    [70-90] -> -1
    [90-110] -> 0
    [110-130] -> 1
    [130-150] -> 2
    [150-infinie] -> 3
     */
    public int addTravel(Travel travel) throws SQLException {
        int id = -1;
        Statement stmt = null;
        stmt = c.createStatement();
        c.setAutoCommit(false);
        String sql = "INSERT INTO TRAVEL (START,DAY,TIME) " +
                "VALUES ("+ travel.getStart() + ", " +travel.getDay() + ", " + travel.getTime()+" );";
        int res = stmt.executeUpdate(sql);
        if(res > 0){
            ResultSet rs = stmt.getGeneratedKeys();
            id = rs.getInt(1);
        }
        stmt.close();
        c.commit();
        return id;
    }

    public void addCheckpoint(Checkpoint chekpoint) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        c.setAutoCommit(false);
        String sql = "INSERT INTO CHECKPOINT (ID,TRAVEL,LAT,LONG,TIME) " +
                "VALUES ("+chekpoint.getId() + ", "+ chekpoint.getTravel_id() + ", "
                + chekpoint.getLatitude() + ", " + chekpoint.getLongitude()+ ", " + chekpoint.getTime()+" );";
        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();
    }

    public void displayTravel() throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM TRAVEL;" );
        while ( rs.next() ) {
            int id = rs.getInt("ID");
            int start = rs.getInt("START");
            int day  = rs.getInt("DAY");
            int time = rs.getInt("TIME");

            System.out.println( "ID = " + id );
            System.out.println( "START = " + start );
            System.out.println( "DAY = " + day);
            System.out.println( "TIME = " + time);
            System.out.println();
        }
        rs.close();
        stmt.close();
    }

    public void displayCheckpointbyId(int t_id) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM CHECKPOINT WHERE ID= "+ t_id + ";" );
        while ( rs.next() ) {
            int id = rs.getInt("ID");
            int travel_id = rs.getInt("TRAVEL");
            float lt  = rs.getInt("LAT");
            float lg  = rs.getInt("LONG");
            int time = rs.getInt("TIME");

            System.out.println( "ID = " + id );
            System.out.println( "TRAVEL = " + travel_id );
            System.out.println( "lAT = " + lt);
            System.out.println( "LONG = " + lg);
            System.out.println( "TIME = " + time);
            System.out.println();
        }
        rs.close();
        stmt.close();
    }

    public void displayCheckpoint() throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM CHECKPOINT;" );
        while ( rs.next() ) {
            int id = rs.getInt("ID");
            int travel_id = rs.getInt("TRAVEL");
            float lt  = rs.getInt("LAT");
            float lg  = rs.getInt("LONG");
            int time = rs.getInt("TIME");

            System.out.println( "ID = " + id );
            System.out.println( "TRAVEL = " + travel_id );
            System.out.println( "lAT = " + lt);
            System.out.println( "LONG = " + lg);
            System.out.println( "TIME = " + time);
            System.out.println();
        }
        rs.close();
        stmt.close();
    }
    public ArrayList<Checkpoint> getCheckpointsbyTravel(int id) throws SQLException {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT ID,LAT,LONG,TIME FROM CHECKPOINT WHERE TRAVEL=" +id+ ";" );
        while ( rs.next() ) {
            checkpoints.add(new Checkpoint(rs.getInt("ID"),rs.getInt("LAT"),rs.getInt("LONG"),rs.getInt("TIME")));
        }
        rs.close();
        stmt.close();
        return checkpoints;
    }


    /*
    update the training file and compute the model
     */
    public void writeRecords() throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT ID,START,DAY,TIME FROM TRAVEL;" );
        ArrayList<Checkpoint> checkpoints;
        //Hashmap containing a checkpoint and its average time
        ArrayList<CheckpointRecord> checkpointRecords;
        //List containing the average time for each checkpoint id
        ArrayList<Integer> average_checkpoint = new ArrayList<>();
        int average;
        int nb = getNumberOfCheckpoints();
        for(int i=0;i<nb;i++){
            System.out.println("average checkpoint" + i + " " + getAverageCheckpoints(i));
            average_checkpoint.add(getAverageCheckpoints(i));
        }
        int id;
        while ( rs.next() ) {
            id = rs.getInt("ID");
            checkpoints = getCheckpointsbyTravel(id);
            checkpointRecords = new ArrayList<>();
            for(Checkpoint k : checkpoints){
                checkpointRecords.add(new CheckpointRecord(k, average_checkpoint.get(k.getId())));
            }
            average = getAverageTravel();
            System.out.println(id + " size check" + checkpoints.size() + " average " + average);
            TravelRecord records = new TravelRecord(Travel.getStartHour(rs.getInt("START")),rs.getInt("DAY"),checkpointRecords,MyMath.getLabel(rs.getInt("TIME"),average));
            svm.write(records);
        }
        rs.close();
        stmt.close();
        svm.close();
    }

    public void train(){
        svm.train();
    }

    /*
        return the mediane of all the travels
     */
    public int getAverageTravel() throws SQLException {
        int result = -1;
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT AVG(x) FROM (SELECT TIME AS x FROM TRAVEL " +
                        "ORDER BY TIME LIMIT 2 - (SELECT COUNT(*) FROM TRAVEL) % 2 " + //if we got a even number
                        "OFFSET (SELECT (COUNT(*) - 1) / 2 FROM TRAVEL));");
        while ( rs.next() ) {
            result = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return result;
    }

    /*
        return the mediane of a checkpoint
     */
    public int getAverageCheckpoints(int id) throws SQLException {
        int result = -1;
        Statement stmt = c.createStatement();

        ResultSet rs = stmt.executeQuery( "SELECT AVG(x) FROM (SELECT TIME AS x FROM CHECKPOINT " +
                "WHERE ID = "+ id +
                " ORDER BY TIME LIMIT 2 - (SELECT COUNT(*) FROM CHECKPOINT WHERE ID=" + id +") % 2 " + //if we got a even number
                "OFFSET (SELECT (COUNT(*) - 1) / 2 FROM CHECKPOINT WHERE ID =" + id +"));");

        while ( rs.next() ) {
            result = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return result;
    }

    /*
        return the mean, la moyenne d'un checkpoint
     */
    public int getMeanCheckponts(int id) throws SQLException {
        int result = -1;
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT AVG(TIME) FROM CHECKPOINT "+
                "WHERE ID = "+ id + ";");
        while ( rs.next() ) {
            result = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return result;
    }

    /*
        return the mean, la moyenne des trajets
     */
    public int getMeanTravel() throws SQLException {
        int result = -1;
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT AVG(TIME) FROM TRAVEL");
        while ( rs.next() ) {
            result = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return result;
    }

    /*
        return the mean, la moyenne d'un trajet sur un jour
     */
    public int getMeanTravelbyDay(int day) throws SQLException {
        int result = -1;
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT AVG(TIME) FROM TRAVEL WHERE DAY =" + day + ";");
        while ( rs.next() ) {
            result = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return result;
    }

    //return the max id, care we start at 0 so the number equals max + 1
    public int getNumberOfCheckpoints() throws SQLException {
        int result;
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT MAX(ID) FROM CHECKPOINT ;");
        rs.next();
        result = rs.getInt(1);
        rs.close();
        stmt.close();
        return result + 1;
    }

    public String minTravelbyDay(int day) throws SQLException {
        String res = "";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT MIN(TIME) FROM TRAVEL WHERE DAY =" + day + ";");
        while ( rs.next() ) {
            res += rs.getInt(1) + " " + MyMath.days.get(rs.getInt(2));
        }
        rs.close();
        stmt.close();
        return res;
    }

    public void deleteAll() throws SQLException {
        deleteCheckpoint();
        deleteTravel();
    }
    public void deleteTravel() throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeUpdate( "DROP TABLE IF EXISTS TRAVEL;" );
        stmt.close();
    }

    public void deleteCheckpoint() throws SQLException {
        Statement stmt = c.createStatement();
        stmt.executeUpdate( "DROP TABLE IF EXISTS CHECKPOINT;" );
        stmt.close();
    }

}
