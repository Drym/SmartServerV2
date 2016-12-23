//code for the server using Java Spark library for web application
import Objects.Checkpoint;
import Objects.Travel;
import Objects.CheckpointRecord;
import Objects.TravelRecord;
import Utils.MyMath;
import Utils.SvmManager;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import database.SQLDatabase;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.SparkBase.setPort;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.*;

public class Main {

    public static String KEY = "AIzaSyCAyS6YwjjNKyUdiITmjhd1dKc0swsw9E0";
    public static String SNAP_ROAD_URL = "https://roads.googleapis.com/v1/snapToRoads";
    public static String json_test = "{\"value\":[{\"lt\":\"43.62025872\",\"lg\":\"7.07532013\"}]}";

    public static SQLDatabase database;

    public static void main(String[] args) {

        try {
            SQLDatabase database = new SQLDatabase("database/test.db");
            database.deleteAll();
            database.create_database();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject test = new JSONObject(json_test);
        System.out.println("Test JSON : "+json_test);
        try {
            getCheckpoint(test);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        staticFileLocation("/public");
        setPort(7777);
        post("/checkpoint", Main::getCoord);
        post("/record", Main::saveRecord);
        post("/prediction", Main::predict);
    }

    private static String getCoord(Request request, Response response) {

        System.out.println("Received from SmartRoad app : "+request.body());

        JSONObject resultat = new JSONObject(request.body());
        try {
            resultat = getCheckpoint(resultat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String resultatFinal = parseJSON(resultat).toString();

        return resultatFinal;
        //return "coucou ca va encule";
    }

    private static JSONObject getCheckpoint(JSONObject locations) throws UnirestException {
        String path = "?path=";
        JSONArray tab = locations.getJSONArray("value");
        int len = tab.length();
        for(int i=0;i<len;i++){
            path += String.valueOf(tab.getJSONObject(i).get("lt"));
            path += "," + String.valueOf(tab.getJSONObject(i).get("lg"));
            if(i!=len - 1){
                path += "|";
            }
        }
        String url = SNAP_ROAD_URL + path +"&key=" + KEY;
        //System.out.println(url);
        HttpResponse<JsonNode> request = Unirest.get(url).asJson();
        JSONObject myObj = request.getBody().getObject();

        System.out.println("Google API resulat : "+myObj.toString());

        return myObj;
    }
    // 7.07532013,43.62025872 43.62020459039435,7.074930474126736

    private static JSONObject parseJSON(JSONObject resultat) {

        JSONObject resultatFinal = new JSONObject();
        Collection<JSONObject> items = new ArrayList<JSONObject>();

        JSONArray array = resultat.getJSONArray("snappedPoints");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonobject = array.getJSONObject(i);
            //System.out.println(jsonobject);

            JSONObject location = jsonobject.getJSONObject("location");
            //System.out.println(location);

            String lt =  location.get("latitude").toString();
            String lg =  location.get("longitude").toString();
            //System.out.println(lt + " "+ lg);

            JSONObject item1 = new JSONObject();
            item1.put("lt", lt);
            item1.put("lg", lg);
            items.add(item1);
        }

        resultatFinal.put("value", new JSONArray(items));
        System.out.println("Parsing of Google API result : "+resultatFinal);

        return resultatFinal;
    }


    /*
    JSONObject expected:
    {
        travel:{
            start:yyyy-MM-dd-HH:mm:ss,
            time:2400
        },
        values:[
            {
                id:0,
                lat:17,8,
                long:42,
                time:512,
             },...
        ]
    }


    */

    /*
        Store the result of a travel in to the database
     */
    private static String saveRecord(Request request, Response response){
        JSONObject resultat = new JSONObject(request.body());
        JSONObject t = resultat.getJSONObject("travel");
        JSONArray values = resultat.getJSONArray("values");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Travel travel = new Travel();
        int travel_id;
        ArrayList<Checkpoint> checkpoints = new ArrayList<Checkpoint>();
        try {
            Date date = dateformat.parse(t.getString("start"));
            travel = new Travel(date,t.getInt("time"));
            JSONObject checkpoint;
            for(int i=0;i<values.length();i++){
                checkpoint = values.getJSONObject(i);
                checkpoints.add(new Checkpoint(checkpoint.getInt("id"),(float)checkpoint.getDouble("lat"),(float)checkpoint.getDouble("long"),checkpoint.getInt("time")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            //we get the id once the travel have been added to datatabase
            travel_id = database.addTravel(travel);
            for(Checkpoint i:checkpoints){
                i.setTravel_id(travel_id);
                database.addCheckpoint(i);
            }
            System.out.println("adding checkpoints\n");
            database.displayCheckpointbyId(travel_id);
            //construit le training file
            database.writeRecords();
            database.train();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "ok";
    }

    /*
        make a prediction according the post parameter
        travel:{
            start:yyyy-MM-dd-HH:mm:ss,
        },
        values:[
            {
                id:0,
                lat:17,8,
                long:42,
                time:512,
             },...
        ]

     */
    private static String predict(Request request, Response response){
        JSONObject resultat = new JSONObject(request.body());
        JSONObject t = resultat.getJSONObject("travel");
        JSONArray values = resultat.getJSONArray("values");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        TravelRecord travel;
        int result = -1;
        ArrayList<CheckpointRecord> checkpoints = new ArrayList<CheckpointRecord>();
        try {
            Date date = dateformat.parse(t.getString("start"));
            JSONObject checkpoint;
            int id;
            for(int i=0;i<values.length();i++){
                checkpoint = values.getJSONObject(i);
                id = checkpoint.getInt("id");
                checkpoints.add(new CheckpointRecord(new Checkpoint(id,(float)checkpoint.getDouble("lat"),(float)checkpoint.getDouble("long"),checkpoint.getInt("time"))
                        , database.getAverage(id)));
            }
            travel = new TravelRecord(MyMath.getStart(date),MyMath.getDay(date),checkpoints);
            result = SvmManager.predict(travel);
            result = MyMath.getTimesFromLabel(result,database.getAverageTravel());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(result>=0){
            return String.valueOf(result);
        }
        else{
            return "oups un erreur s'est produite sorry";
        }
    }

    /*
        returning stats for the application, average travel, moyenne par jour ...
        example:
        {
            average_travel: 450,
            travels:[
                {
                    day:1,
                    average: 430,
                    best_hour: 15h30
                },
                {
                    day:2,
                    average: 470,
                    best_hour: 16h15
                }
                ...
            ]
            checkpoints: [
                {
                    id:1,
                    average: 120
                },
                {
                    id:2,
                    average: 180
                }
                ...
            ]


        }
     */
    private static String getStats(Request request, Response response){
        String res = "";
        return res;
    }
}