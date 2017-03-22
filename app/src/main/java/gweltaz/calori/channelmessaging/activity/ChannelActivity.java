package gweltaz.calori.channelmessaging.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.fragments.MessageFragment;

public class ChannelActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment);

    }







}
