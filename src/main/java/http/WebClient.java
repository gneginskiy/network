package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WebClient {
    //how is it better than this:
    //for ((i=1;i<=20000;i++)); do   curl -s "http://localhost:8080/indeps.htm"; done
    //???

    public static final String REQ = "GET /indeps.htm HTTP/1.1\nHost: localhost:8080\n\n";

    public static void main(String[] args) throws IOException, InterruptedException {
            testForNconnections(100);
    }

    private static void testForNconnections( int max) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        AtomicInteger counter = new AtomicInteger();
        for (int j = 0; j < max; j++) {
            executor.submit(() -> {
                sendMessage();
                counter.incrementAndGet();
            });
        }
        executor.shutdown();
        while (!executor.awaitTermination(24L, TimeUnit.HOURS)) {
            System.out.println("Not yet. Still waiting for termination");
        }
        System.out.println(counter + " OK");
    }

    private static void sendMessage() {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(REQ.getBytes());
            byte[] bytes = in.readAllBytes();
            System.out.println(new String(bytes));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
