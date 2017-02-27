package gweltaz.calori.channelmessaging.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import gweltaz.calori.channelmessaging.Channel;
import gweltaz.calori.channelmessaging.ChannelContainer;
import gweltaz.calori.channelmessaging.ChannelListActivity;
import gweltaz.calori.channelmessaging.Downloader;
import gweltaz.calori.channelmessaging.ListAdapter;
import gweltaz.calori.channelmessaging.ListFriendsActivity;
import gweltaz.calori.channelmessaging.LoginActivity;
import gweltaz.calori.channelmessaging.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.activity.ChannelActivity;


public class ChannelListFragment extends Fragment implements OnDownloadCompleteListener {

    public ListView mListview;

    public ListView getmListview() {
        return mListview;
    }
    public ChannelListFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_channel_list, container, false);
        mListview = (ListView) rootview.findViewById(R.id.listView);

        HashMap<String,String> map = new HashMap<String, String>();
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        map.put("accesstoken", settings.getString("accesstoken", ""));
        Downloader downloader = new Downloader();
        downloader.setMap(map);
        downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=getchannels");
        downloader.setOnDownloadCompleteListener(this);

        downloader.execute();
        mListview.setOnItemClickListener((ChannelListActivity)getActivity());

        FloatingActionButton fab = (FloatingActionButton)rootview.findViewById(R.id.viewfriendsbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((ChannelListActivity)getActivity(),ListFriendsActivity.class);
                startActivity(intent);
            }
        });
        return rootview;
    }


    @Override
    public void onDownloadComplete(String content) {
        Gson gson = new Gson();
        ChannelContainer container = gson.fromJson(content, ChannelContainer.class);
        mListview.setAdapter(new ListAdapter(getActivity().getApplicationContext(),container.getChannels()));
    }
}
