package database;

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
            Travel t1 = new Travel(1,date,512);
            Travel t2 = new Travel(1,dateformat.parse("17/07/2016/15/12/23"),650);
            database.create_database();
            database.deleteTravel();
            database.addTravel(t1,450);
            database.addTravel(t2,450);
            database.displayTravel();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
