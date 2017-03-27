package gweltaz.calori.channelmessaging.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gweltaz.calori.channelmessaging.interfaces.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.adapter.PrivateMessageListAdapter;
import gweltaz.calori.channelmessaging.model.PrivateMesssageContainer;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.model.PrivateMessage;
import gweltaz.calori.channelmessaging.tasks.Downloader;

public class PrivateMessageActivity extends AppCompatActivity implements OnDownloadCompleteListener {

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

    }

    @Override
    public void onDownloadComplete(String content) {
        try
        {
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
        }catch (Exception e)
        {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.private_activity_message),
                    "Aucune connexion", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }

    }


}
