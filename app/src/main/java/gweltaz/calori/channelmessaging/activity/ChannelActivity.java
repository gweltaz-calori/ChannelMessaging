package gweltaz.calori.channelmessaging.activity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gweltaz.calori.channelmessaging.Downloader;
import gweltaz.calori.channelmessaging.Friends;
import gweltaz.calori.channelmessaging.LoginActivity;
import gweltaz.calori.channelmessaging.Message;
import gweltaz.calori.channelmessaging.MessageListAdapter;
import gweltaz.calori.channelmessaging.Messages;
import gweltaz.calori.channelmessaging.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.PrivateMessageListAdapter;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.UploadFileToServer;
import gweltaz.calori.channelmessaging.UserDatasource;

public class ChannelActivity extends AppCompatActivity implements OnDownloadCompleteListener {
    private Handler h;
    private int delay = 1000;
    private int channelId;
    private ListView mListview;
    private List<Message> messages = new ArrayList<>();
    private FloatingActionButton send,sendphoto;
    EditText input;
    private final int PICTURE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        channelId = getIntent().getIntExtra("channelid", 0);
        mListview = (ListView) findViewById(R.id.listviewmessages);
        sendphoto =(FloatingActionButton) findViewById(R.id.sendphoto);
        h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {

                HashMap<String, String> map = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                map.put("accesstoken", settings.getString("accesstoken", ""));
                map.put("channelid", Integer.toString(channelId));
                Downloader downloader = new Downloader();
                downloader.setMap(map);
                downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=getmessages");
                downloader.setOnDownloadCompleteListener(ChannelActivity.this);

                downloader.execute();
                h.postDelayed(this, delay);
            }
        }, delay);
        send = (FloatingActionButton) findViewById(R.id.viewfriendsbutton);
        input = (EditText) findViewById(R.id.messageinput);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                map.put("accesstoken", settings.getString("accesstoken", ""));
                map.put("channelid", Integer.toString(channelId));
                map.put("message", input.getText().toString());
                Downloader downloader = new Downloader();
                downloader.setMap(map);
                downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=sendmessage");
                downloader.execute();
                input.setText("");
            }
        });
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChannelActivity.this);
                builder.setTitle("Ajouter un ami");
                builder.setMessage("Voulez vous vraiment ajouter cette utilisateur à votre liste d'amis");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        UserDatasource userDatasource = new UserDatasource(ChannelActivity.this);
                        userDatasource.open();
                        System.out.println(userDatasource.toString());
                        Message m = (Message) mListview.getItemAtPosition(position);
                        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);

                        if(!m.getUsername().equals(settings.getString("username", "")))
                        {
                            Friends f = userDatasource.createFriend(m.getImageUrl(),m.getUserID(),m.getUsername());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Impossible de vous ajouter",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        sendphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Création de l’appel
                File f = new File(Environment.getExternalStorageDirectory()+"/Chat/images/test.jpg");
                f.mkdirs();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)); //Emplacement de l’image stockée

                startActivityForResult(intent, PICTURE_REQUEST_CODE);

            }
        });

    }

    @Override
    public void onDownloadComplete(String content) {

        Gson gson = new Gson();
        Messages container = gson.fromJson(content, Messages.class);

        Collections.reverse(container.getMessages());
        if(!messages.equals(container.getMessages()))
        {
            final MessageListAdapter myListAdapter =new MessageListAdapter(getApplicationContext(), container.getMessages());
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

                Toast.makeText(getApplicationContext(),data.getDataString(),Toast.LENGTH_SHORT).show();
                List<NameValuePair> liste = new ArrayList<>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);


                liste.add(new BasicNameValuePair("accesstoken", settings.getString("accesstoken", "")));
                liste.add(new BasicNameValuePair("channelid",Integer.toString(channelId)));
                new UploadFileToServer(getApplicationContext(), data.getData().getPath(), liste, new UploadFileToServer.OnUploadFileListener() {
                    @Override
                    public void onResponse(String result)
                    {
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(IOException error) {

                    }
                });
                Toast.makeText(getApplicationContext(),"bravo",Toast.LENGTH_SHORT).show();
        }
    }
    //decodes image and scales it to reduce memory consumption
    private void resizeFile(File f, Context context) throws IOException {
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
