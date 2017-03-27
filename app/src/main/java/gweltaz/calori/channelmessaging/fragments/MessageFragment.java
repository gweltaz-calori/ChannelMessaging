package gweltaz.calori.channelmessaging.fragments;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

import gweltaz.calori.channelmessaging.tasks.Downloader;
import gweltaz.calori.channelmessaging.model.Friends;
import gweltaz.calori.channelmessaging.activity.LoginActivity;
import gweltaz.calori.channelmessaging.model.Message;
import gweltaz.calori.channelmessaging.adapter.MessageListAdapter;
import gweltaz.calori.channelmessaging.model.Messages;
import gweltaz.calori.channelmessaging.interfaces.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.tasks.UploadFileToServer;
import gweltaz.calori.channelmessaging.dbhelper.UserDatasource;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements OnDownloadCompleteListener {

    private Handler h;
    private int delay = 1000;

    public int getChannelId() {
        return channelId;
    }



    private int channelId;
    private ListView mListview;
    private List<Message> messages = new ArrayList<>();
    private FloatingActionButton send,sendphoto,soundbutton;
    EditText input;
    private final int PICTURE_REQUEST_CODE = 1;
    private View mView;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        channelId =  1;
        System.out.println(channelId);
        mListview = (ListView) rootView.findViewById(R.id.listviewmessages);
        sendphoto =(FloatingActionButton) rootView.findViewById(R.id.sendphoto);

        send = (FloatingActionButton) rootView.findViewById(R.id.viewfriendsbutton);
        input = (EditText) rootView.findViewById(R.id.messageinput);
        soundbutton = (FloatingActionButton) rootView.findViewById(R.id.Soundbutton);
        this.mView = rootView;

        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


            h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {

                    if(getActivity() !=null)
                    {
                        HashMap<String, String> map = new HashMap<String, String>();
                        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);

                        map.put("accesstoken", settings.getString("accesstoken", ""));
                        map.put("channelid", Integer.toString(channelId));
                        Downloader downloader = new Downloader();
                        downloader.setMap(map);
                        downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=getmessages");
                        downloader.setOnDownloadCompleteListener(MessageFragment.this);

                        downloader.execute();
                        h.postDelayed(this, delay);
                    }

                }
            }, delay);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<String, String>();
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Ajouter un ami");
                builder.setMessage("Voulez vous vraiment ajouter cette utilisateur à votre liste d'amis");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        UserDatasource userDatasource = new UserDatasource(getActivity());
                        userDatasource.open();
                        System.out.println(userDatasource.toString());
                        Message m = (Message) mListview.getItemAtPosition(position);
                        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);

                        if(!m.getUsername().equals(settings.getString("username", "")))
                        {
                            Friends f = userDatasource.createFriend(m.getImageUrl(),m.getUserID(),m.getUsername());
                        }
                        else
                        {
                            Toast.makeText(getActivity().getApplicationContext(),"Impossible de vous ajouter",Toast.LENGTH_SHORT).show();
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

                File test = new File(Environment.getExternalStorageDirectory()+"/img.jpg");
                try {
                    test.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Android a depuis Android Nougat besoin d'un provider pour donner l'accès à un répertoire pour une autre app, cf : http://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
                Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", test);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Création de l’appelà l’application appareil photo pour récupérer une image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //Emplacement de l’image stockée
                startActivityForResult(intent, PICTURE_REQUEST_CODE);
            }
        });
        soundbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }
    public void showDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt("channelId", channelId);
        DialogFragment soundFragment = new SoundRecordDialog();
        soundFragment.setArguments(bundle);
        soundFragment.show(getFragmentManager(),"");

    }
    @Override
    public void onDownloadComplete(String content) {

        try
        {
            Gson gson = new Gson();
            Messages container = gson.fromJson(content, Messages.class);

            Collections.reverse(container.getMessages());
            if(!messages.equals(container.getMessages()))
            {
                if(getActivity() !=null)
                {
                    System.out.println(container.getMessages());
                    final MessageListAdapter myListAdapter =new MessageListAdapter(getActivity().getApplicationContext(), container.getMessages());
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
        }catch (Exception e)
        {

            Snackbar mySnackbar = Snackbar.make(mView,
                    "Aucune connexion", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case PICTURE_REQUEST_CODE :

                Toast.makeText(getActivity().getApplicationContext(),data.getDataString(),Toast.LENGTH_SHORT).show();
                List<NameValuePair> liste = new ArrayList<>();
                SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);


                liste.add(new BasicNameValuePair("accesstoken", settings.getString("accesstoken", "")));
                liste.add(new BasicNameValuePair("channelid",Integer.toString(channelId)));
                File test = new File(Environment.getExternalStorageDirectory()+"/img.jpg");

                try {
                    resizeFile(test,getActivity().getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new UploadFileToServer(getActivity(), test.getPath(), liste, new UploadFileToServer.OnUploadFileListener() {
                    @Override
                    public void onResponse(String result)
                    {
                        Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(IOException error) {
                        System.out.println(error);
                        Snackbar mySnackbar = Snackbar.make(mView,
                                "L'upload de la photo a échoué", Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }
                }).execute();

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
    public void changeChannelId(int id)
    {
        channelId = id;
    }
}
