package gweltaz.calori.channelmessaging;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;

    public static final String PREFS_NAME = "MyPrefsFile";
    private Button buttonvalider,mapsButton;
    private EditText identifiant,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        askPermission();
        buttonvalider = (Button) findViewById(R.id.button_valider);
        identifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        password = (EditText) findViewById(R.id.editTextPassword);
        mapsButton = (Button) findViewById(R.id.mapsButton);
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
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,GPSActivity.class);
                startActivity(intent);
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
            editor.putString("username", identifiant.getText().toString());
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
    public void askPermission()
    {



        ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);





    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {




                } else
                {

                    finish();

                }
                return;
            }



        }
    }
}
