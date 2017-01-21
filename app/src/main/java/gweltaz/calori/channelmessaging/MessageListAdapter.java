package gweltaz.calori.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

        new DownloadImageTask((ImageView) convertView.findViewById(R.id.avatarmessage))
                .execute(message.getImageUrl());
        titlemessage.setText(message.getMessage());
        personmessage.setText(""+message.getUserID());


        return convertView;
    }


}
