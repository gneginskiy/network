import com.beust.jcommander.Parameter;
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

    @SneakyThrows
    private void waitAmoment(int seconds) {
        Thread.sleep(seconds * 1000);
    }


}
