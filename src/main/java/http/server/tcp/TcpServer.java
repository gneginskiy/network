package http.server.tcp;

import http.server.tcp.handler.RequestHandler;
import http.server.util.ConcurrencyUtil;
import lombok.Builder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
public class TcpServer {
    private static final int DEFAULT_PORT = 8080;
    @Builder.Default private int port = DEFAULT_PORT;
    @Builder.Default private int nThreads = ConcurrencyUtil.getNcores();
    private RequestHandler requestHandler;

    public void start() throws IOException {
        ServerSocket serverConnect = new ServerSocket(port);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        while (true) {
            Socket connection = serverConnect.accept();
            //todo: sync multithreaded vs async
            executorService.execute(() -> {
                try (connection) {
                    requestHandler.handle(
                            connection.getInputStream(),
                            connection.getOutputStream()
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
