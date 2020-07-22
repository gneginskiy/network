import http.WebServerApp;
import lombok.SneakyThrows;
import org.testng.annotations.*;

public class WebServerAppTest {
    private Thread serverThread;

    @BeforeClass
    public void setUp() {
        serverThread = new Thread(WebServerApp::main) {{
            start();
        }};
    }

    @AfterClass
    public void tearDown() {
        serverThread.stop();
    }


    @Test(dataProvider = "webserverWithNconnectionsProvider")
    public void testWebserverWithNconnections(int nRequests) {
        waitAmoment(5);
        WebServerAppClient.testForNconnections(nRequests);
    }

    @SneakyThrows
    @Test(enabled = false) //todo: fix. hangs while waiting for the process
    public void testWebserverWithNconnections1() {
        String curlCommand = getCurlCommand(100);
        Process exec = Runtime.getRuntime()
                .exec(new String[]{"/bin/sh", "-c", curlCommand});
        exec.waitFor();
    }

    @DataProvider
    public static Object[][] webserverWithNconnectionsProvider() {
        return new Object[][]{
                {1},
                {100},
                {500},
                {1000},
                {2000},
                {5000},
                {10000},
                {20000},
        };
    }

    private static String getCurlCommand(int nRequests) {
        return "for ((i=1;i<=" + nRequests + ";i++)); do curl --silent \"http://localhost:8080/indeps.htm\"; done";
    }

    @SneakyThrows
    private void waitAmoment(int seconds) {
        Thread.sleep(seconds * 1000);
    }
}
