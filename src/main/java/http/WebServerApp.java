package http;

import http.server.tcp.TcpServer;
import http.server.tcp.handler.impl.simplehttp.SimpleHttpRequestHandler;
import lombok.SneakyThrows;

public class WebServerApp {
    private static final String WWW_ROOT = "/Users/grigory_neginsky/";

    @SneakyThrows
    public static void main(String... args)  {
        TcpServer.builder()
                .port(8080)
                .requestHandler(new SimpleHttpRequestHandler(WWW_ROOT))
                .build()
                .start();
    }

}
