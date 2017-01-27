package gweltaz.calori.channelmessaging;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by calorig on 27/01/2017.
 */
public class FriendGridAdapter extends ArrayAdapter<Friends>
{
    public FriendGridAdapter(Context context, List<Friends> friends ){
        super(context, 0, friends);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Friends friend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_grid_layout, parent, false);
        }

        TextView nameperson = (TextView) convertView.findViewById(R.id.nameperson);
        CircleImageView imageperson = (CircleImageView) convertView.findViewById(R.id.imageperson);
        String chemin = friend.getImageUrl().substring(friend.getImageUrl().lastIndexOf('/'));

        String path = Environment.getExternalStorageDirectory()+chemin;

        File imgFile = new File(path);
        System.out.println(imgFile.getPath());
        if(imgFile.exists())
        {
            System.out.println("le fichier existe");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageperson.setImageBitmap(myBitmap);
        }
        else
        {
            new DownloadImageTask(imageperson,friend.getImageUrl())
                    .execute(friend.getImageUrl());
            System.out.println("le fichier existe pas");

        }


        nameperson.setText(friend.getUsername());



        return convertView;
    }
}
