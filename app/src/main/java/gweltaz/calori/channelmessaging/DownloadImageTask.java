package gweltaz.calori.channelmessaging;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    CircleImageView bmImage;
    String imageurl;

    public DownloadImageTask(CircleImageView bmImage,String imageurl) {
        this.bmImage = bmImage;
        this.imageurl = imageurl;
    }
    public DownloadImageTask(ImageView bmImage,String imageurl) {
        this.imageView = bmImage;
        this.imageurl = imageurl;
    }
    protected Bitmap doInBackground(String... urls) {
        Bitmap mIcon11 = null;
        String chemin = imageurl.substring(imageurl.lastIndexOf('/'));
        try {
            URL url = new URL(urls[0]);
            File file = new File(Environment.getExternalStorageDirectory()+chemin);
            file.createNewFile();

            URLConnection ucon = url.openConnection();
/* Define InputStreams to read from the URLConnection.*/
            InputStream is = ucon.getInputStream();
/* Read bytes to the Buffer until there is nothing more to read(-1) and
write on the fly in the file.*/
            FileOutputStream fos = new FileOutputStream(file);
            final int BUFFER_SIZE = 23 * 1024;
            BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
            byte[] baf = new byte[BUFFER_SIZE];
            int actual = 0;
            while (actual != -1) {
                fos.write(baf, 0, actual);
                actual = bis.read(baf, 0, BUFFER_SIZE);
            }
            fos.close();
            mIcon11 = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
//TODO HANDLER
        }
        return mIcon11;
    }


    protected void onPostExecute(Bitmap result)
    {
        if(bmImage != null)
        {
            bmImage.setImageBitmap(result);
        }
        else
        {
            imageView.setImageBitmap(result);
        }




    }
}
