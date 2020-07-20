import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer20 {
    private static final String RELATIVE_PATH = "/Users/grigory_neginsky/";

    public static void main(String[] args) throws IOException {
        ServerSocket serverConnect = new ServerSocket(8080);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        while (true) {
            Socket clientConnection = serverConnect.accept();
            clientConnection.setTcpNoDelay(true);
            executorService.execute(() -> {
                try {
                    InputStream inputStream = clientConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String requestMessage = getRequestMessage(br);
                    String fileName = getFileName(requestMessage);
                    byte[] message = getFileContent(fileName);
                    OutputStream outputStream = new BufferedOutputStream(clientConnection.getOutputStream());
                    PrintWriter out = new PrintWriter(outputStream);
                    String contentMimeType = getMimeType(fileName,requestMessage);
                    out.println("HTTP/1.1 200 privet");
                    out.println("Content-type: " + contentMimeType);
                    out.println("Content-length: " + message.length);
                    out.println();
                    out.flush();
                    outputStream.write(message, 0, message.length);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static String getMimeType(String fileName, String requestMessage) {
        return MimeTypeExtractor.getMimeType(fileName,requestMessage);
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
            sb.append(line).append("<br>");
            lineNum++;
        }
        return sb.toString();
    }

    private static byte[] getFileContent(String fileName) throws IOException {
        try {
            return FileUtils.readFileToByteArray(new File(RELATIVE_PATH + fileName));
        } catch (IOException e) {
            return "404".getBytes();
        }
    }
}
