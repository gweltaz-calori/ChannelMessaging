package gweltaz.calori.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class MessageListAdapter extends ArrayAdapter<Message>
{


    public MessageListAdapter(Context context, List<Message> messages ){
        super(context, 0, messages);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_layout, parent, false);
        }

        TextView titlemessage = (TextView) convertView.findViewById(R.id.titlemessage);
        TextView personmessage = (TextView) convertView.findViewById(R.id.personmessage);
        CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatarmessage);
        String chemin = message.getImageUrl().substring(message.getImageUrl().lastIndexOf('/'));

        String path = Environment.getExternalStorageDirectory()+chemin;

        File imgFile = new File(path);
        System.out.println(imgFile.getPath());
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


        titlemessage.setText(message.getMessage());
        personmessage.setText(""+message.getUserID());


        return convertView;
    }


}
