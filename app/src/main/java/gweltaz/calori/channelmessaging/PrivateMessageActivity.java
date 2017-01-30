package gweltaz.calori.channelmessaging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PrivateMessageActivity extends AppCompatActivity implements OnDownloadCompleteListener {
    private final int PICTURE_REQUEST_CODE = 1;
    private Handler h;
    private int delay = 1000;
    private int userid;
    private ListView mListview;
    private List<PrivateMessage> messages = new ArrayList<>();
    private FloatingActionButton send,sendphoto;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);

        userid = getIntent().getIntExtra("userid", 0);
        mListview = (ListView) findViewById(R.id.listviewprivatemessages);

        h = new Handler();
        h.post(new Runnable() {
            public void run() {

                HashMap<String, String> map = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                map.put("accesstoken", settings.getString("accesstoken", ""));
                map.put("userid", Integer.toString(userid) );
                Downloader downloader = new Downloader();
                downloader.setMap(map);
                downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=getmessages");
                downloader.setOnDownloadCompleteListener(PrivateMessageActivity.this);

                downloader.execute();
                h.postDelayed(this, delay);
            }
        });
        send = (FloatingActionButton) findViewById(R.id.sendprivatemessage);
        sendphoto =(FloatingActionButton) findViewById(R.id.sendphoto);
        input = (EditText) findViewById(R.id.privatemessageinput);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                map.put("accesstoken", settings.getString("accesstoken", ""));
                map.put("userid", Integer.toString(userid));
                map.put("message", input.getText().toString());
                Downloader downloader = new Downloader();
                downloader.setMap(map);
                downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=sendmessage");
                downloader.execute();
                input.setText("");
            }
        });
        sendphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Création de l’appel
                File f = new File(Environment.getExternalStorageDirectory()+"/Chat/images");
                f.mkdirs();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)); //Emplacement de l’image stockée

                startActivityForResult(intent, PICTURE_REQUEST_CODE);

            }
        });
    }

    @Override
    public void onDownloadComplete(String content) {

        Gson gson = new Gson();
        PrivateMesssageContainer container = gson.fromJson(content, PrivateMesssageContainer.class);

        Collections.reverse(container.getMessages());
        if(!messages.equals(container.getMessages()))
        {
            final PrivateMessageListAdapter myListAdapter =new PrivateMessageListAdapter(getApplicationContext(), container.getMessages());
            mListview.setAdapter(myListAdapter);
            mListview.post(new Runnable() {
                @Override
                public void run() {
                    // Select the last row so it will scroll into view...
                    mListview.setSelection(myListAdapter.getCount() - 1);
                }
            });
            messages = container.getMessages();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case PICTURE_REQUEST_CODE :
                Toast.makeText(getApplicationContext(),"bravo",Toast.LENGTH_SHORT).show();
        }
    }
    //decodes image and scales it to reduce memory consumption
    private void resizeFile(File f,Context context) throws IOException {
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

        //The new size we want to scale to
        final int REQUIRED_SIZE=400;

        //Find the correct scale value. It should be the power of 2.
        int scale=1;
        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
            scale*=2;

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        int i = getCameraPhotoOrientation(context, Uri.fromFile(f),f.getAbsolutePath());
        if (o.outWidth>o.outHeight)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(i); // anti-clockwise by 90 degrees
            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);
        }
        try {
            f.delete();
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) throws IOException {
        int rotate = 0;
        context.getContentResolver().notifyChange(imageUri, null);
        File imageFile = new File(imagePath);
        ExifInterface exif = new ExifInterface(
                imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        return rotate;
    }
}
