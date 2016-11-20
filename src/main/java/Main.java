//code for the server using Java Spark library for web application
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.*;
import static spark.Spark.*;

public class Main {

    public static String KEY = "AIzaSyCAyS6YwjjNKyUdiITmjhd1dKc0swsw9E0";
    public static String SNAP_ROAD_URL = "https://roads.googleapis.com/v1/snapToRoads";

    public static String json_test = "{\"value\":[{\"lg\":\"7.07532013\",\"lt\":\"43.62025872\"}]}";

    public static void main(String[] args) {
        JSONObject test = new JSONObject(json_test);
        try {
            getCheckpoint(test);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        staticFileLocation("/public");
        port(7777);
        post("/checkpoint", Main::getCoord);
    }

    private static String getCoord(Request request, Response response) {
        System.out.println(request.body());
        return "coucou ca va encule";
    }

    private static void getCheckpoint(JSONObject locations) throws UnirestException {
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
        System.out.println(url);
        HttpResponse<JsonNode> request = Unirest.get(url)
                .asJson();
        JSONObject myObj = request.getBody().getObject();
        System.out.println(myObj.toString());
    }
    // 7.07532013,43.62025872 43.62020459039435,7.074930474126736
}