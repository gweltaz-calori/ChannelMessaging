package gweltaz.calori.channelmessaging;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrivateMessageActivity extends AppCompatActivity implements OnDownloadCompleteListener {
    private Handler h;
    private int delay = 1000;
    private int userid;
    private ListView mListview;
    private List<PrivateMessage> messages = new ArrayList<>();
    private FloatingActionButton send;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        userid = getIntent().getIntExtra("userid", 0);
        mListview = (ListView) findViewById(R.id.listviewprivatemessages);

        h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                System.out.println(userid);
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
        }, delay);
        send = (FloatingActionButton) findViewById(R.id.sendprivatemessage);
        input = (EditText) findViewById(R.id.privatemessageinput);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onDownloadComplete(String content) {
        Gson gson = new Gson();
        PrivateMesssageContainer container = gson.fromJson(content, PrivateMesssageContainer.class);


        if(!messages.equals(container.getMessages()))
        {
            mListview.setAdapter(new PrivateMessageListAdapter(getApplicationContext(), container.getMessages()));
            messages = container.getMessages();

        }
    }
}
