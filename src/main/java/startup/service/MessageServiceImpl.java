package startup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import startup.business.Message;
import startup.dao.MessageDao;

import java.util.Date;
import java.util.List;

/**
 * Created by poussma on 03/03/16.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao dao;

    @Override
    public long getLatestMessageId() {
        return dao.getLatestMessageId();
    }

    @Override
    public List<Message> getMessagesFromId(long id) {
        return dao.findByIdGreaterThan(id);
    }

    @Override
    public void send(Message message) {
        message.setDate(new Date());
        dao.save(message);
    }
}
