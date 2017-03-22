package gweltaz.calori.channelmessaging.model;

import java.util.ArrayList;
import java.util.List;

import gweltaz.calori.channelmessaging.model.PrivateMessage;

/**
 * Created by calorig on 27/01/2017.
 */
public class PrivateMesssageContainer
{
    private List<PrivateMessage> messages;


    public void addMessage(PrivateMessage message){
        this.messages.add(message);
    }

    public List<PrivateMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PrivateMessage> messages) {
        this.messages = messages;
    }

    public PrivateMesssageContainer() {
        this.messages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messages=" + messages +
                '}';
    }
}
