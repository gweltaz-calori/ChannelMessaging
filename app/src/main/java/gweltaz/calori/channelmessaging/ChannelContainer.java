package gweltaz.calori.channelmessaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calorig on 20/01/2017.
 */
public class ChannelContainer
{
    private List<Channel> channels;

    public List<Channel> getChannels() {
        return channels;
    }
    public void addChannel(Channel channel){
        this.channels.add(channel);
    }
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public ChannelContainer() {

        this.channels = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ChannelContainer{" +
                "channels=" + channels +
                '}';
    }
}
