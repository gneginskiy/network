package http.server.tcp.handler.impl;

import http.MimeTypeExtractor;
import http.server.tcp.handler.RequestHandler;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.StringTokenizer;

public class SimpleHttpRequestHandler implements RequestHandler {
    private String wwwRootPath;

    public SimpleHttpRequestHandler(String wwwRootPath) {
        this.wwwRootPath = wwwRootPath;
    }

    @Override
    @SneakyThrows
    public void handle(InputStream inputStream, OutputStream outputStream) {
        String requestMessage = getRequestMessage(inputStream);

        PrintWriter out = new PrintWriter(outputStream);
        String fileName = getFileName(requestMessage);
        byte[] responseData = getFileContent(fileName, wwwRootPath);
        writeHeader(requestMessage, out, fileName, responseData);
        outputStream.write(responseData);
        outputStream.flush();
    }

    private void writeHeader(String requestMessage, PrintWriter out, String fileName, byte[] responseData) {
        String contentMimeType = MimeTypeExtractor.getMimeType(fileName, requestMessage);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-type: " + contentMimeType);
        out.println("Content-length: " + responseData.length);
        out.println();
        out.flush();
    }

    private static String getFileName(String requestMessage) {
        StringTokenizer st = new StringTokenizer(requestMessage);
        st.nextToken();
        return st.nextToken().substring(1);
    }

    private static byte[] getFileContent(String fileName, String wwwRootPath) {
        try {
            return FileUtils.readFileToByteArray(new File(wwwRootPath + fileName));
        } catch (IOException e) {
            return "404".getBytes();
        }
    }

    @SneakyThrows
    private static String getRequestMessage(InputStream inputStream) {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while (StringUtils.isNotEmpty(line = reader.readLine())) {
            sb.append(line).append("\r\n");
        }
        return sb.toString();
    }
}
