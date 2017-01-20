package gweltaz.calori.channelmessaging;

import java.lang.reflect.Type;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class ChannelListActivity extends AppCompatActivity implements OnDownloadCompleteListener {

    private ListView mListview;
    private String[] listItems = {"item 1", "item 2 ", "list", "android", "item 3",
            "foobar", "bar", };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        mListview = (ListView) findViewById(R.id.listView);

        HashMap<String,String> map = new HashMap<String, String>();
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        map.put("accesstoken",settings.getString("accesstoken",""));
        Downloader downloader = new Downloader();
        downloader.setMap(map);
        downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=getchannels");
        downloader.setOnDownloadCompleteListener(ChannelListActivity.this);

        downloader.execute();
    }

    @Override
    public void onDownloadComplete(String content) {
        Gson gson = new Gson();
        ChannelContainer container = gson.fromJson(content, ChannelContainer.class);
        mListview.setAdapter(new ListAdapter(getApplicationContext(),container.getChannels()));


    }
}
