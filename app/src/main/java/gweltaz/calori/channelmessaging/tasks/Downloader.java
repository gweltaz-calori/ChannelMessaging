package gweltaz.calori.channelmessaging.tasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import gweltaz.calori.channelmessaging.interfaces.OnDownloadCompleteListener;

/**
 * Created by calorig on 20/01/2017.
 */
public class Downloader extends AsyncTask<String,Void,String>
{
    private HashMap<String,String> map;
    private String url;
    private ArrayList<OnDownloadCompleteListener> listeners = new ArrayList<OnDownloadCompleteListener> ();
    public void setOnDownloadCompleteListener (OnDownloadCompleteListener listener)
    {

        this.listeners.add(listener);
    }
    public Downloader(HashMap map,String url)
    {
        this.map = map;
        this.url = url;
    }

    public Downloader() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    @Override
    protected String doInBackground(String... params)
    {

        return performPostCall(url,this.map);
    }
    @Override
    protected void onPostExecute(String result) {
        for(OnDownloadCompleteListener onelistener : listeners)
        {
             onelistener.onDownloadComplete(result);
        }



    }
    public String performPostCall(String requestURL, HashMap<String, String>
            postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private String getPostDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
