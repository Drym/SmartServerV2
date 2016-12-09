//code for the server using Java Spark library for web application
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.SparkBase.setPort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static spark.Spark.*;

public class Main {

    public static String KEY = "AIzaSyCAyS6YwjjNKyUdiITmjhd1dKc0swsw9E0";
    public static String SNAP_ROAD_URL = "https://roads.googleapis.com/v1/snapToRoads";

    public static String json_test = "{\"value\":[{\"lt\":\"43.62025872\",\"lg\":\"7.07532013\"}]}";

    public static void main(String[] args) {

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
            id:1,
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
    private static String saveRecord(Request request, Response response){
        JSONObject resultat = new JSONObject(request.body());

        return "ok";
    }
}