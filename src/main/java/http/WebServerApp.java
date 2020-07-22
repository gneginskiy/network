package http;

import http.server.tcp.TcpServer;
import http.server.tcp.handler.impl.SimpleHttpRequestHandler;

import java.io.*;

public class WebServerApp {
    private static final String WWW_ROOT = "/Users/grigory_neginsky/";

    public static void main(String[] args) throws IOException {
        TcpServer.builder()
                .port(8080)
                .nThreads(4)
                .requestHandler(new SimpleHttpRequestHandler(WWW_ROOT))
                .build()
                .start();
    }

}
