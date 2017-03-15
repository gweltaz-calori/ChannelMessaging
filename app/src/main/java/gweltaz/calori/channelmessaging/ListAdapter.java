package gweltaz.calori.channelmessaging;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calorig on 20/01/2017.
 */
public class ListAdapter extends ArrayAdapter<Channel>
{


    public ListAdapter(Context context, List<Channel> channels ){
        super(context, 0, channels);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Channel channel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
        }
        // Lookup view for data population
        TextView channeltext = (TextView) convertView.findViewById(R.id.textViewChannel);
        TextView nb = (TextView) convertView.findViewById(R.id.textviewNb);
        // Populate the data into the template view using the data object
        channeltext.setText(channel.getName());
        nb.setText("Nombres d'utilisateurs connectés : "+channel.getConnectedusers());
        // Return the completed view to render on screen


        return convertView;
    }


}
