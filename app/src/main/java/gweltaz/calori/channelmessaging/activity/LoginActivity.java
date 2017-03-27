package gweltaz.calori.channelmessaging.activity;

import android.Manifest;
import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Random;

import android.os.Handler;

import gweltaz.calori.channelmessaging.interfaces.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.model.Connect;
import gweltaz.calori.channelmessaging.tasks.Downloader;

public class LoginActivity extends AppCompatActivity implements OnDownloadCompleteListener {

    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;

    public static final String PREFS_NAME = "MyPrefsFile";
    private Button buttonvalider,mapsButton;
    private EditText identifiant,password;
    private ImageView mIvLogo;
    private TextView messageLogin,messageLoginToMove,randomTextview;
    private Handler mHandlerTada;
    private int mShortDelay;
    private AVLoadingIndicatorView avi;
    private static final String[] explainStringArray = {
            "Ah",
            "Marc",
            "Salut à tous les gamers"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        askPermission();
        randomTextview = (TextView) findViewById(R.id.messageRandom);
        buttonvalider = (Button) findViewById(R.id.button_valider);
        identifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        password = (EditText) findViewById(R.id.editTextPassword);
        mapsButton = (Button) findViewById(R.id.mapsButton);
        mIvLogo = (ImageView) findViewById(R.id.mIvLogo);
        messageLogin = (TextView) findViewById(R.id.messageLogin);
        messageLoginToMove = (TextView) findViewById(R.id.messageLoginToMove);
        identifiant.setText("gcalo");
        password.setText("gweltazcalori");
        avi =(AVLoadingIndicatorView) findViewById(R.id.avi);


        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout((LinearLayout) findViewById(R.id.loginMainLayout))
                .setTransitionDuration(4000)
                .start();
        buttonvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connect();
            }
        });
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,GPSActivity.class);
                startActivity(intent);
            }
        });
        mHandlerTada = new Handler(); // android.os.handler
        mShortDelay = 4000; //milliseconds

        mHandlerTada.postDelayed(new Runnable(){
            public void run(){
                // Your code here
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(5)
                        .playOn(mIvLogo);
                mHandlerTada.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);
        System.out.println(explainStringArray[new Random().nextInt(explainStringArray.length)]);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                YoYo.with(Techniques.SlideOutRight).duration(750).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        randomTextview.setText(explainStringArray[new Random().nextInt(explainStringArray.length)]);
                        YoYo.with(Techniques.SlideInLeft).duration(750).playOn(randomTextview);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(randomTextview);
                mHandlerTada.postDelayed(this, 2000);
            }
        }, 2000);

    }

    @Override
    public void onDownloadComplete(String content) {

        try
        {
            buttonvalider.setVisibility(View.VISIBLE);
            avi.hide();
            Gson gson = new Gson();
            final Connect connect = gson.fromJson(content, Connect.class);
            System.out.println(connect.getResponse());
            if(connect.getResponse().equals("Ok"))
            {

                Animation animSlideLeft = AnimationUtils.loadAnimation(this, R.anim.slide_left);
                messageLoginToMove.startAnimation(animSlideLeft);
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        SharedPreferences settings =
                                getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("accesstoken", connect.getAccesstoken());
                        editor.putString("username", identifiant.getText().toString());
                        editor.commit();
                        Toast.makeText(getApplicationContext(),"Connecté",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ChannelListActivity.class);
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, Pair.create((View)mIvLogo, "logo"),Pair.create((View)messageLogin, "messagetransition")).toBundle());
                        new Handler().postDelayed(new Runnable(){
                            public void run(){
                                messageLoginToMove.clearAnimation();
                            }
                        }, 500);

                    }
                }, 500);




            }

        }catch (Exception e)
        {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.loginMainLayout),
                    "Aucune connexion", Snackbar.LENGTH_SHORT);
            mySnackbar.setAction("Réssayer", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connect();
                }
            });
            mySnackbar.show();

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
    public void connect()
    {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("username",identifiant.getText().toString());
        map.put("password",password.getText().toString());
        Downloader downloader = new Downloader();
        downloader.setMap(map);
        downloader.setUrl("http://www.raphaelbischof.fr/messaging/?function=connect");
        downloader.setOnDownloadCompleteListener(LoginActivity.this);

        downloader.execute();
        buttonvalider.setVisibility(View.INVISIBLE);
        avi.show();
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
