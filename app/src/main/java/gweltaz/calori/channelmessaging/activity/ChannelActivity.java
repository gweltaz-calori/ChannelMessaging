package gweltaz.calori.channelmessaging.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gweltaz.calori.channelmessaging.Downloader;
import gweltaz.calori.channelmessaging.Friends;
import gweltaz.calori.channelmessaging.LoginActivity;
import gweltaz.calori.channelmessaging.Message;
import gweltaz.calori.channelmessaging.MessageListAdapter;
import gweltaz.calori.channelmessaging.Messages;
import gweltaz.calori.channelmessaging.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.PrivateMessageListAdapter;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.UploadFileToServer;
import gweltaz.calori.channelmessaging.UserDatasource;
import gweltaz.calori.channelmessaging.fragments.MessageFragment;

public class ChannelActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        MessageFragment messageFragment = (MessageFragment)getSupportFragmentManager().findFragmentById(R.id.message_fragment);

    }







}
