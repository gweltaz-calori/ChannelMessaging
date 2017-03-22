package gweltaz.calori.channelmessaging.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import gweltaz.calori.channelmessaging.interfaces.OnDownloadCompleteListener;

/**
 * Created by calorig on 15/03/2017.
 */
public class DownloadSoundTask extends AsyncTask<String, Void, String>
{


    String soundUrl;
    private ArrayList<OnDownloadCompleteListener> listeners = new ArrayList<OnDownloadCompleteListener> ();
    public void setOnDownloadCompleteListener (OnDownloadCompleteListener listener)
    {

        this.listeners.add(listener);
    }
    public DownloadSoundTask(String soundUrl) {

        this.soundUrl = soundUrl;
    }

    protected String doInBackground(String... urls) {

        String chemin = soundUrl.substring(soundUrl.lastIndexOf('/'));
        try {
            URL url = new URL( urls[0]);
            File file = new File(Environment.getExternalStorageDirectory()+chemin);
            file.createNewFile();
 /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();
 /* Define InputStreams to read from the URLConnection.*/
            InputStream is = ucon.getInputStream();
 /* Read bytes to the Buffer until there is nothing more to
read(-1) and write on the fly in the file.*/
            FileOutputStream fos = new FileOutputStream(file);
            final int BUFFER_SIZE = 23 * 1024;
            BufferedInputStream bis = new BufferedInputStream(is,
                    BUFFER_SIZE);
            byte[] baf = new byte[BUFFER_SIZE];
            int actual = 0;
            while (actual != -1) {
                fos.write(baf, 0, actual);
                actual = bis.read(baf, 0, BUFFER_SIZE);
            }
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            //TODO HANDLER
        }
        return null;
    }


    protected void onPostExecute(String result)
    {
        System.out.println(result);
        for(OnDownloadCompleteListener onelistener : listeners)
        {
            onelistener.onDownloadComplete(result);
        }

    }
}