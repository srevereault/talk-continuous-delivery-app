package startup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import startup.business.Message;
import startup.dao.MessageDao;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

/**
 * Created by poussma on 03/03/16.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao dao;

    @Value("${git.commit.id}")
    private String gitCommit;

    @PostConstruct
    public void ready() {
        // tell them !
        Message message = new Message();
        message.setOwner("system (" + System.getProperty("server.name", "local") + ")");
        message.setMessage("Now running " + gitCommit);
        send(message);
    }

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
        // sanity check
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        LocalDateTime now = LocalDateTime.now();
        message.setDate(now.format(DateTimeFormatter.ISO_DATE_TIME));

        //message.setDateAsDate(new Date(now.toEpochMilli()));
        dao.save(message);
    }
}
