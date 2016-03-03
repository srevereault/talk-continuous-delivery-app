package startup.service;

import startup.business.Message;

import java.util.List;

/**
 * Created by poussma on 03/03/16.
 */
public interface MessageService {

    long getLatestMessageId();

    List<Message> getMessagesFromId(long id);

    void send(Message message);
}
