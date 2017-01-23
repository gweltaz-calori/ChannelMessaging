package gweltaz.calori.channelmessaging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class ListFriendsActivity extends AppCompatActivity {

    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friends);
        gridView = (GridView) findViewById(R.id.gridview);
        UserDatasource userDatasource = new UserDatasource(ListFriendsActivity.this);
        userDatasource.all(m.getImageUrl(),m.getUserID(),"toto");
    }
}
