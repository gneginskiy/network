package http.server.tcp.handler;

import java.io.InputStream;
import java.io.OutputStream;

public interface RequestHandler {
    void handle(InputStream requestMessage, OutputStream outputStream);
}
