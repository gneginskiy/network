import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    public static final String RELATIVE_PATH = "/Users/grigory_neginsky/";

    public static void main(String[] args) throws IOException {
        ServerSocket serverConnect = new ServerSocket(8080);
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        while (true) {
            executorService.submit(()->{
                try {
                    Socket clientConnection = serverConnect.accept();
                    InputStream inputStream = clientConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String message = "";
                    String requestMessage = getRequestMessage(br);
                    message += requestMessage;
                    message += "<br>bird is a WORD<br>";
                    String fileName = getFileName(requestMessage);
                    message = message + getFileContent(fileName);
                    PrintWriter out = new PrintWriter(clientConnection.getOutputStream());
                    String contentMimeType = "text/html";
                    out.println("HTTP/1.1 200 privet");
                    out.println("Content-type: " + contentMimeType);
                    out.println("Content-length: " + message.getBytes().length);
                    out.println();
                    out.print(message);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static String getFileName(String requestMessage) {
        StringTokenizer st = new StringTokenizer(requestMessage);
        st.nextToken();
        return st.nextToken().replace("/", "");
    }

    private static String getRequestMessage(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while (StringUtils.isNotEmpty(line = br.readLine())) {
            sb.append(line).append("<br>");
        }
        return sb.toString();
    }

    private static String getFileContent(String fileName) throws IOException {
        try {
            return FileUtils.readFileToString(new File(RELATIVE_PATH + fileName), "UTF-8");
        } catch (IOException e) {
            return "404";
        }
    }
}
