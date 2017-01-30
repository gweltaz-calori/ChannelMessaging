package gweltaz.calori.channelmessaging.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
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
import gweltaz.calori.channelmessaging.UserDatasource;

public class ChannelActivity extends AppCompatActivity implements OnDownloadCompleteListener {
    private Handler h;
    private int delay = 1000;
    private int channelId;
    private ListView mListview;
    private List<Message> messages = new ArrayList<>();
    private FloatingActionButton send;
    EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        channelId = getIntent().getIntExtra("channelid", 0);
        mListview = (ListView) findViewById(R.id.listviewmessages);

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


}
