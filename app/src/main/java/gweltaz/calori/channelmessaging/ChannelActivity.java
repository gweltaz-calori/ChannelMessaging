package gweltaz.calori.channelmessaging;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

public class ChannelActivity extends AppCompatActivity implements OnDownloadCompleteListener {
    private Handler h;
    private int delay = 1000;
    private int channelId;
    private ListView mListview;
    private FloatingActionButton send;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        channelId = getIntent().getIntExtra("channelid", 0);
        mListview = (ListView) findViewById(R.id.listviewmessages);

        h = new Handler();
        h.postDelayed(new Runnable(){
            public void run()
            {

                HashMap<String,String> map = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                map.put("accesstoken", settings.getString("accesstoken", ""));
                map.put("channelid",Integer.toString(channelId));
                Downloader downloader = new Downloader();
                downloader.setMap(map);
                downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=getmessages");
                downloader.setOnDownloadCompleteListener(ChannelActivity.this);

                downloader.execute();
                h.postDelayed(this, delay);
            }
        }, delay);
        send = (FloatingActionButton) findViewById(R.id.sendmessage);
        input = (EditText) findViewById(R.id.messageinput);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map = new HashMap<String, String>();
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                map.put("accesstoken", settings.getString("accesstoken", ""));
                map.put("channelid",Integer.toString(channelId));
                map.put("message",input.getText().toString());
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

        Gson gson = new Gson();
        Messages container = gson.fromJson(content, Messages.class);
        mListview.setAdapter(new MessageListAdapter(getApplicationContext(),container.getMessages()));
    }
}
