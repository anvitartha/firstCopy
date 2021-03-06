package com.example.sekhar.webaccess;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Sekhar on 07/11/2015.
 */
public class PostData  extends AsyncTask<String, Void, String> {
    private Context lpostdatacontext;
            String lurl;
            DataToPost lDataToPost;
    public PostData(Context postdatacontext,String url,DataToPost DataToPost){
        this.lurl = url;
        this.lDataToPost= DataToPost;
        this.lpostdatacontext = postdatacontext;
    }

    @Override
    protected String doInBackground(String... Params) {
        return post(Params[0]);
    }

    public  String post( String url) {
        InputStream inputStream = null;
        String result = "";

        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost;
            HttpGet httpGet;
            // 2. make POST request to the given URL
            httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", lDataToPost.getName());
            jsonObject.accumulate("country", lDataToPost.getCountry());
            jsonObject.accumulate("twitter", lDataToPost.getTwitter());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Toast.makeText(lpostdatacontext, result , Toast.LENGTH_LONG).show();

        // 11. return result
        return result;

    }



    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


}