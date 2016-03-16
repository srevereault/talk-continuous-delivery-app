package startup.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import startup.business.Message;
import startup.dao.MessageDao;

/**
 * Created by poussma on 16/03/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageServiceImplTest {


    @InjectMocks
    private MessageServiceImpl toTest = new MessageServiceImpl();

    @Mock
    private MessageDao mockedDao;


    @Test(expected = IllegalArgumentException.class)
    public void testSendNullMessage() throws Exception {
        toTest.send(null);
    }

    @Test
    public void testSend() throws Exception {
        Message message = new Message();
        toTest.send(message);
        Mockito.verify(mockedDao).save(Matchers.any(Message.class));
    }
}