package cr.cartago.paraiso.cachi.loaiza.developpersoftware.managementgym.database;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;


public class HttpJsonParser {

    private static InputStream is = null;
    private static JSONArray jsonArray;
    private static String json = "";
    private HttpURLConnection urlConnection = null;

    //original name: makeHttpRequest
    public JSONArray getJson(String url, Map<String, String> params) {

        jsonArray = null;

        try {
            Uri.Builder builder = new Uri.Builder();
            URL urlObj;
            String encodedParams = "";
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            if (builder.build().getEncodedQuery() != null) {
                encodedParams = builder.build().getEncodedQuery();

            }
            url = url + "?" + encodedParams;
            urlObj = new URL(url);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setRequestMethod("GET");

            //Connect to the server
            urlConnection.connect();
            //Read the response
            is = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;

            //Parse the response
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            //Convert the response to JSON Object
            jsonArray = new JSONArray(json);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {

            jsonArray = new JSONArray();

        } catch (Exception e) {
            Log.e("Exception", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jsonArray;

    }

    //Method: POST, UPDATE, DELETE
    public void sendJson(String urlAdress, Map<String, String> params) throws IOException, JSONException {

        URL url = new URL(urlAdress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept","application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        JSONObject jsonParam = new JSONObject();

        for (Map.Entry<String, String> entry : params.entrySet())
        {
            jsonParam.put(entry.getKey(), entry.getValue());
        }

        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

        os.writeBytes(jsonParam.toString());

        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
        Log.i("MSG" , conn.getResponseMessage());

        conn.disconnect();

    }



}