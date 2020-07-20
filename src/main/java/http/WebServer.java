package http;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private static final String RELATIVE_PATH = "/Users/grigory_neginsky/";

    public static void main(String[] args) throws IOException {
        ServerSocket serverConnect = new ServerSocket(8080);
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        while (true) {
            Socket clientConnection = serverConnect.accept();
            //todo autoclosable, refactor(generic server + handlers), load testing, etc
            //todo: sync multithreaded vs async
            executorService.execute(() -> {
                try {
                    InputStream inputStream = clientConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String requestMessage = getRequestMessage(br);
                    String fileName = getFileName(requestMessage);
                    byte[] responseData = getFileContent(fileName);
                    OutputStream outputStream = clientConnection.getOutputStream();
                    PrintWriter out = new PrintWriter(outputStream);
                    String contentMimeType = MimeTypeExtractor.getMimeType(fileName, requestMessage);
                    out.println("HTTP/1.1 200 privet");
                    out.println("Content-type: " + contentMimeType);
                    out.println("Content-length: " + responseData.length);
                    out.println();
                    out.flush();
                    outputStream.write(responseData);
                    outputStream.flush();
                    clientConnection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private static String getFileName(String requestMessage) {
        StringTokenizer st = new StringTokenizer(requestMessage);
        st.nextToken();
        return st.nextToken().substring(1);
    }

    private static String getRequestMessage(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        int lineNum = 0;
        while (StringUtils.isNotEmpty(line = br.readLine()) && lineNum < 2) {
            sb.append(line).append("\r\n");
            lineNum++;
        }
        return sb.toString();
    }

    private static byte[] getFileContent(String fileName) {
        try {
            return FileUtils.readFileToByteArray(new File(RELATIVE_PATH + fileName));
        } catch (IOException e) {
            return "404".getBytes();
        }
    }
}
