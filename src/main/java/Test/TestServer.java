package Test;


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
            sendPost("record",json);
            sendPost("stats","");
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
}
