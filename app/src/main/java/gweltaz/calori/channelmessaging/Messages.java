package gweltaz.calori.channelmessaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwel7_000 on 21/01/2017.
 */

public class Messages
{
    private List<Message> messages;


    public void addMessage(Message message){
        this.messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Messages() {
        this.messages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messages=" + messages +
                '}';
    }
}
