import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rest.RestServer;

public class RestTest {
    private RestServer restServer;

    @Before
    public void setUp() throws Exception {
        restServer = new RestServer();
        restServer.start();
    }

    @After
    public void tearDown() throws Exception {
        restServer.stop();
    }

    @Test
    public void name() {
        new ResteasyClientBuilder().build().target("http://localhost:8080/qw").request().get();
    }
}
