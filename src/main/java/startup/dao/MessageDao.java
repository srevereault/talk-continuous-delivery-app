package startup.dao;

import org.springframework.data.repository.CrudRepository;
import startup.business.Message;

import java.util.List;

/**
 * Created by poussma on 03/03/16.
 */
public interface MessageDao extends CrudRepository<Message, Long> {

    List<Message> findByIdGreaterThan(long id);

    Long getLatestMessageId();
}
