package startup.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by poussma on 17/03/16.
 */
@Component
public class MonitoringFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringFilter.class);

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private InetAddress destination;

    private String server;

    private DatagramSocket socket;

    private ExecutorService executorService = new ThreadPoolExecutor(1, 10, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.server = System.getProperty("server.name", "local");
        try {
            // FIXME change the address
            destination = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("monitoring destination not found");
        }
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new IllegalArgumentException("cannot prepare socket");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startedAt = System.nanoTime();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            executorService.submit(() -> monitor(System.nanoTime() - startedAt));
        }
    }

    private void monitor(long took) {
        long tookMs = TimeUnit.NANOSECONDS.toMillis(took);
        LOGGER.info("{} took {}", server, tookMs);
        byte[] buffer = (server + " " + tookMs + "ms").getBytes(UTF8);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, destination, 8888);

        try {
            socket.send(packet);
        } catch (IOException e) {
            // swallowed
        }
    }

    @Override
    public void destroy() {
        this.executorService.shutdownNow();
    }
}
