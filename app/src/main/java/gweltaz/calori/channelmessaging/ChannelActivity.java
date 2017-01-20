package gweltaz.calori.channelmessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

public class ChannelActivity extends AppCompatActivity implements OnDownloadCompleteListener {
    private Handler h;
    private int delay = 1000;
    private int channelId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        channelId = getIntent().getIntExtra("channelid", 0);

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


    }

    @Override
    public void onDownloadComplete(String content) {
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
    }
}
