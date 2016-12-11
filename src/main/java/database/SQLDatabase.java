package database;

import Objects.Checkpoint;
import Objects.Travel;
import Utils.MyMath;
import org.sqlite.SQLiteConfig;

import java.sql.*;

/**
 * Created by thibault on 09/12/2016.
 */
public class SQLDatabase {

    Connection c = null;

    public SQLDatabase(String path) {
        c = null;
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
                " LABEL             INT, " +
                " START     INT     NOT NULL, " +
                " DAY       INT     NOT NULL, " +
                " TIME      INT     NOT NULL)";
        String checkpoint = "CREATE TABLE if not exists CHECKPOINT " +
                "(ID INT    NOT NULL," +
                " TRAVEL         INT, " +
                " LABEL          INT, " +
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
    public int addTravel(Travel travel,int mediane) throws SQLException {
        int id = -1;
        Statement stmt = null;
        stmt = c.createStatement();
        c.setAutoCommit(false);
        String sql = "INSERT INTO TRAVEL (LABEL,START,DAY,TIME) " +
                "VALUES ("+ MyMath.getLabel(travel.getTime(),mediane)+", "+ travel.getStart() + ", " +travel.getDay() + ", " + travel.getTime()+" );";
        int res = stmt.executeUpdate(sql);
        if(res > 0){
            ResultSet rs = stmt.getGeneratedKeys();
            id = rs.getInt(1);
        }
        stmt.close();
        c.commit();
        return id;
    }

    public void addCheckpoint(Checkpoint chekpoint,int mediane) throws SQLException {
        Statement stmt = null;
        stmt = c.createStatement();
        c.setAutoCommit(false);
        String sql = "INSERT INTO CHECKPOINT (ID,TRAVEL,LABEL,LAT,LONG,TIME) " +
                "VALUES ("+chekpoint.getId() + ", "+ chekpoint.getTravel_id() +", " +MyMath.getLabel(chekpoint.getTime(),mediane) + ", "
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
            int cls = rs.getInt("LABEL");
            int start = rs.getInt("START");
            int day  = rs.getInt("DAY");
            int time = rs.getInt("TIME");

            System.out.println( "ID = " + id );
            System.out.println( "LABEL = " + cls );
            System.out.println( "START = " + start );
            System.out.println( "DAY = " + day);
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
            int cls = rs.getInt("LABEL");
            int travel_id = rs.getInt("TRAVEL");
            float lt  = rs.getInt("LAT");
            float lg  = rs.getInt("LONG");
            int time = rs.getInt("TIME");

            System.out.println( "ID = " + id );
            System.out.println( "LABEL = " + cls );
            System.out.println( "TRAVEL = " + travel_id );
            System.out.println( "lAT = " + lt);
            System.out.println( "LONG = " + lg);
            System.out.println( "TIME = " + time);
            System.out.println();
        }
        rs.close();
        stmt.close();
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
