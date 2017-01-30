package gweltaz.calori.channelmessaging;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class PrivateMessageListAdapter extends ArrayAdapter<PrivateMessage>
{

    private List<PrivateMessage> messages;
    private Context context;
    public PrivateMessageListAdapter(Context context, List<PrivateMessage> messages )
    {
        super(context, 0, messages);
        this.messages = messages;
        this.context = context;
        System.out.println("Constructor -------------------------------------------");
        for(PrivateMessage message : messages)
        {
            System.out.println(message.toString()+"\n");
        }
        System.out.println(" -------------------------------------------");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PrivateMessage message = getItem(position);
        System.out.println("Current item -------------------------------------------");
        System.out.println(message.toString()+"\n");
        System.out.println(" -------------------------------------------");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_layout, parent, false);
        }

        TextView titlemessage = (TextView) convertView.findViewById(R.id.titlemessage);
        TextView personmessage = (TextView) convertView.findViewById(R.id.personmessage);
        CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatarmessage);
        String chemin = message.getImageUrl().substring(message.getImageUrl().lastIndexOf('/'));

        String path = Environment.getExternalStorageDirectory()+chemin;

        File imgFile = new File(path);

        if(imgFile.exists())
        {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            avatar.setImageBitmap(myBitmap);
        }
        else
        {
            new DownloadImageTask(avatar,message.getImageUrl())
                    .execute(message.getImageUrl());


        }

        if(message.getSendbyme()==0 && message.getEverRead().equals("0"))
        {
            titlemessage.setTypeface(Typeface.DEFAULT_BOLD);
            titlemessage.setTextColor(Color.parseColor("#000000"));
        }

        titlemessage.setText(message.getMessage());
        personmessage.setText(""+message.getUsername());


        return convertView;
    }


}
