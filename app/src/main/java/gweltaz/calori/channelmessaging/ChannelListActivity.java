package gweltaz.calori.channelmessaging;

import java.lang.reflect.Type;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import gweltaz.calori.channelmessaging.activity.ChannelActivity;
import gweltaz.calori.channelmessaging.fragments.ChannelListFragment;
import gweltaz.calori.channelmessaging.fragments.MessageFragment;

public class ChannelListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_channel_list);
        } else {
            setContentView(R.layout.channel_list_activuty_landscape);

        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment);
        ChannelListFragment channelListFragment = (ChannelListFragment)getSupportFragmentManager().findFragmentById(R.id.listchannel_fragment);
        Channel channel = (Channel) channelListFragment.mListview.getItemAtPosition(position);
        if(messageFragment == null|| !messageFragment.isInLayout()){

            Intent intent = new Intent(ChannelListActivity.this, ChannelActivity.class);
            intent.putExtra("channelid", channel.getChannelID());
            startActivity(intent);
        } else
        {

            messageFragment.changeChannelId(channel.getChannelID());

        }
    }
}
