package Test;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by thibault on 26/01/2017.
 */
public class TestServer {

    public static String url_server = "http://localhost:7777/";
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) {

        String json = "{\"travel\":{\"start\":\"2017/01/24-09:13:25\",\"time\":31},\"values\":[{\"lt\":\"43.61742309999999\",\"lg\":\"7.070416099999999\",\"id\":0,\"time\":12}," +
                "{\"lt\":\"43.6168487651018\",\"lg\":\"7.069016361029568\",\"id\":1,\"time\":8}," +
                "{\"lt\":\"43.6163470572439\",\"lg\":\"7.065906048729904\",\"id\":2,\"time\":5}," +
                "{\"lt\":\"43.61699975678826\",\"lg\":\"7.067206935304585\",\"id\":3,\"time\":5}]}";
        try {
            JSONObject obj = buildJSONslow();
            //sendPost("record",json);
            sendPost("stats","");
            sendPost("predict",obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendPost(String route, String json) throws IOException {

        URL obj = new URL(url_server + route);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(json);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url_server);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    public static JSONObject buildJSONfast() {
        String json = "{\"travel\":\n" +
                "        {\"start\":\"2017/01/28-17:20:10\"},\"values\":[\n" +
                "        {\"lt\":\"43.617022\", \"lg\":\"7.070626\", \"id\":0,\"time\":60},\n" +
                "        {\"lt\":\"43.617274\", \"lg\":\"7.059448\", \"id\":1,\"time\":60},\n" +
                "        {\"lt\":\"43.616190\", \"lg\":\"7.065710\", \"id\":2,\"time\":60},\n" +
                "        {\"lt\":\"43.617607\", \"lg\":\"7.050966\", \"id\":3,\"time\":60},\n" +
                "        {\"lt\":\"43.613479\", \"lg\":\"7.045980\", \"id\":4,\"time\":60},\n" +
                "        {\"lt\":\"43.616459\", \"lg\":\"7.037392\", \"id\":5,\"time\":60},\n" +
                "        {\"lt\":\"43.612815\", \"lg\":\"7.027715\", \"id\":6,\"time\":60},\n" +
                "        {\"lt\":\"43.610839\", \"lg\":\"7.020811\", \"id\":7,\"time\":60},\n" +
                "        {\"lt\":\"43.608355\", \"lg\":\"7.009675\", \"id\":8,\"time\":60},\n" +
                "        {\"lt\":\"43.603305\", \"lg\":\"7.011713\", \"id\":9,\"time\":60},\n" +
                "        {\"lt\":\"43.602795\", \"lg\":\"7.003899\", \"id\":10,\"time\":60},\n" +
                "        {\"lt\":\"43.607728\", \"lg\":\"6.982483\", \"id\":11,\"time\":60},\n" +
                "        {\"lt\":\"43.611283\", \"lg\":\"6.967509\", \"id\":12,\"time\":60},\n" +
                "        {\"lt\":\"43.604510\", \"lg\":\"6.960655\", \"id\":13,\"time\":60},\n" +
                "        {\"lt\":\"43.598892\", \"lg\":\"6.956935\", \"id\":14,\"time\":60},\n" +
                "        {\"lt\":\"43.597569\", \"lg\":\"6.954510\", \"id\":15,\"time\":60},\n" +
                "        {\"lt\":\"43.592445\", \"lg\":\"6.956929\", \"id\":16,\"time\":60},\n" +
                "        {\"lt\":\"43.587787\", \"lg\":\"6.956874\", \"id\":17,\"time\":60},\n" +
                "        {\"lt\":\"43.586286\", \"lg\":\"6.953680\", \"id\":18,\"time\":60}\n" +
                "        ]}";
        JSONObject check = new JSONObject(json);
        return check;

    }

    public static JSONObject buildJSONslow() {
        String json = "{\"travel\":\n" +
                "        {\"start\":\"2017/01/29-17:20:10\"},\"values\":[\n" +
                "        {\"lt\":\"43.617022\", \"lg\":\"7.070626\", \"id\":0,\"time\":140},\n" +
                "        {\"lt\":\"43.617274\", \"lg\":\"7.059448\", \"id\":1,\"time\":120},\n" +
                "        {\"lt\":\"43.616190\", \"lg\":\"7.065710\", \"id\":2,\"time\":140},\n" +
                "        {\"lt\":\"43.617607\", \"lg\":\"7.050966\", \"id\":3,\"time\":140},\n" +
                "        {\"lt\":\"43.613479\", \"lg\":\"7.045980\", \"id\":4,\"time\":140},\n" +
                "        {\"lt\":\"43.616459\", \"lg\":\"7.037392\", \"id\":5,\"time\":140},\n" +
                "        {\"lt\":\"43.612815\", \"lg\":\"7.027715\", \"id\":6,\"time\":140},\n" +
                "        {\"lt\":\"43.610839\", \"lg\":\"7.020811\", \"id\":7,\"time\":140},\n" +
                "        {\"lt\":\"43.608355\", \"lg\":\"7.009675\", \"id\":8,\"time\":140},\n" +
                "        {\"lt\":\"43.603305\", \"lg\":\"7.011713\", \"id\":9,\"time\":140},\n" +
                "        {\"lt\":\"43.602795\", \"lg\":\"7.003899\", \"id\":10,\"time\":140},\n" +
                "        {\"lt\":\"43.607728\", \"lg\":\"6.982483\", \"id\":11,\"time\":140},\n" +
                "        {\"lt\":\"43.611283\", \"lg\":\"6.967509\", \"id\":12,\"time\":140},\n" +
                "        ]}";
        JSONObject check = new JSONObject(json);
        return check;

    }
}
