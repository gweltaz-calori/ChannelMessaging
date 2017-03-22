package gweltaz.calori.channelmessaging.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import gweltaz.calori.channelmessaging.interfaces.OnDownloadCompleteListener;
import gweltaz.calori.channelmessaging.R;
import gweltaz.calori.channelmessaging.model.Message;
import gweltaz.calori.channelmessaging.tasks.DownloadImageTask;
import gweltaz.calori.channelmessaging.tasks.DownloadSoundTask;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class MessageListAdapter extends ArrayAdapter<Message>
{


    private static String mFileName = null;
    private MediaPlayer mPlayer = null;
    private static final String LOG_TAG = "AudioRecordTest";
    private Activity activity;

    public MessageListAdapter(Context context, List<Message> messages){
        super(context, 0, messages);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);


        if(message.getMessageImageUrl().equals(""))
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_layout, parent, false);

            TextView titlemessage = (TextView) convertView.findViewById(R.id.titlemessage);
            TextView personmessage = (TextView) convertView.findViewById(R.id.personmessage);
            CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatarmessage);
            String chemin = message.getImageUrl().substring(message.getImageUrl().lastIndexOf('/'));

            String path = Environment.getExternalStorageDirectory()+chemin;

            File imgFile = new File(path);



            if(imgFile.exists())
            {
                System.out.println("le fichier existe");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                avatar.setImageBitmap(myBitmap);
            }
            else
            {
                new DownloadImageTask(avatar,message.getImageUrl())
                        .execute(message.getImageUrl());
                System.out.println("le fichier existe pas");

            }
            if(!message.getSoundUrl().equals(""))
            {
                String cheminSound = message.getSoundUrl().substring(message.getSoundUrl().lastIndexOf('/'));
                String pathSound = Environment.getExternalStorageDirectory()+cheminSound;
                File fileSound = new File(pathSound);
                //mFileName = fileSound.getAbsolutePath();
                LinearLayout audioLinear = (LinearLayout) convertView.findViewById(R.id.linearAudioContainer);
                audioLinear.setOrientation(LinearLayout.VERTICAL);


                PlayButton mPlayButton = new PlayButton(getContext(),fileSound,message.getSoundUrl());

                audioLinear.addView(mPlayButton,
                        new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                0));


            }



            titlemessage.setText(message.getMessage());
            personmessage.setText(""+message.getUsername());
        }
        else
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_picture_layout, parent, false);


            ImageView picturemessage = (ImageView) convertView.findViewById(R.id.picturemessage);
            TextView personmessage = (TextView) convertView.findViewById(R.id.personmessage);
            CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatarmessage);

            String cheminAvatar = message.getImageUrl().substring(message.getImageUrl().lastIndexOf('/'));
            String pathAvatar = Environment.getExternalStorageDirectory()+cheminAvatar;
            File imgFile = new File(pathAvatar);

            String cheminPicture = message.getMessageImageUrl().substring(message.getMessageImageUrl().lastIndexOf('/'));
            String pathPicture = Environment.getExternalStorageDirectory()+cheminPicture;
            File imgFilePicture = new File(pathPicture);



            if(imgFile.exists())
            {
                System.out.println("le fichier existe");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                avatar.setImageBitmap(myBitmap);


            }
            else
            {
                new DownloadImageTask(avatar,message.getImageUrl())
                        .execute(message.getImageUrl());


            }
            if(imgFilePicture.exists())
            {


                Bitmap myBitmapPicture = BitmapFactory.decodeFile(imgFilePicture.getAbsolutePath());
                picturemessage.setImageBitmap(myBitmapPicture);
            }
            else
            {

                new DownloadImageTask(picturemessage,message.getMessageImageUrl())
                        .execute(message.getMessageImageUrl());
                System.out.println("le fichier existe pas");

            }



            personmessage.setText(""+message.getUsername());




        }
        return convertView;
    }




    class PlayButton extends Button {
        boolean mStartPlaying = true;
        File fileSound;
        String soundUrl;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v)
            {
                mFileName =fileSound.getAbsolutePath();
                if(!fileSound.exists())
                {
                    DownloadSoundTask downloadSoundTask =  new DownloadSoundTask(soundUrl);
                    downloadSoundTask.setOnDownloadCompleteListener(new OnDownloadCompleteListener() {
                        @Override
                        public void onDownloadComplete(String content)
                        {
                            mFileName =content;
                            PlaySong();
                        }
                    });
                    downloadSoundTask.execute(soundUrl);
                }
                else
                {
                    PlaySong();
                }

            }
        };

        public void PlaySong()
        {

            onPlay(mStartPlaying);
            if (mStartPlaying)
            {
                setText("Stop playing");
            } else {
                setText("Start playing");
            }
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    System.out.println("terminé ");
                    setText("Start playing");
                    mStartPlaying = !mStartPlaying;

                }
            });



            mStartPlaying = !mStartPlaying;
        }
        public PlayButton(Context ctx,File fileSound,String soundUrl)
        {
            super(ctx);
            this.fileSound = fileSound;
            this.soundUrl = soundUrl;

            setText("Start playing");
            setOnClickListener(clicker);
        }
    }
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }
    private void stopPlaying()
    {

        mPlayer.release();

    }


}
