package gweltaz.calori.channelmessaging;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements OnDownloadCompleteListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private Button buttonvalider;
    private EditText identifiant,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonvalider = (Button) findViewById(R.id.button_valider);
        identifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        password = (EditText) findViewById(R.id.editTextPassword);
        identifiant.setText("gcalo");
        password.setText("gweltazcalori");
        buttonvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("username",identifiant.getText().toString());
                map.put("password",password.getText().toString());
                Downloader downloader = new Downloader();
                downloader.setMap(map);
                downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=connect");
                downloader.setOnDownloadCompleteListener(LoginActivity.this);

                downloader.execute();
            }
        });

    }

    @Override
    public void onDownloadComplete(String content) {

        Gson gson = new Gson();
        Connect connect = gson.fromJson(content, Connect.class);
        System.out.println(connect.getResponse());
        if(connect.getResponse().equals("Ok"))
        {
            SharedPreferences settings =
                    getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("accesstoken", connect.getAccesstoken());

            editor.commit();
            Toast.makeText(getApplicationContext(),"Connecté",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),ChannelListActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Identifiants incorrects",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
