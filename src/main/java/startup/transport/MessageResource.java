package startup.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import startup.business.Message;
import startup.service.MessageService;

import java.util.List;

/**
 * Created by poussma on 03/03/16.
 */
@RestController
public class MessageResource {

    @Autowired
    private MessageService service;

    @RequestMapping(value = "/api/messages", method = RequestMethod.GET)
    public List<Message> getMessagesFrom(@RequestParam(name = "since", required = false, defaultValue = "0") long from) {
        return service.getMessagesFromId(from);
    }

    @RequestMapping(value = "/api/messages", method = RequestMethod.POST)
    public ResponseEntity<String> send(@RequestBody Message message) {
        service.send(message);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
