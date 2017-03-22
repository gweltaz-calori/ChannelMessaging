package gweltaz.calori.channelmessaging.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.dbhelper.UserDatasource;
import gweltaz.calori.channelmessaging.adapter.FriendGridAdapter;
import gweltaz.calori.channelmessaging.model.Friends;

public class ListFriendsActivity extends AppCompatActivity {

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friends);
        gridView = (GridView) findViewById(R.id.gridview);
        UserDatasource userDatasource = new UserDatasource(ListFriendsActivity.this);
        userDatasource.open();
        List<Friends> liste = userDatasource.all();
        gridView.setAdapter(new FriendGridAdapter(getApplicationContext(),liste));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListFriendsActivity.this,PrivateMessageActivity.class);
                Friends currentfriend = (Friends) gridView.getItemAtPosition(position);
                intent.putExtra("userid", currentfriend.getId());
                startActivity(intent);
            }
        });

    }
}
