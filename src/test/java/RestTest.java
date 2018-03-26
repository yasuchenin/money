import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rest.RestServer;

public class RestTest {
    private RestServer restServer;

    @Before
    public void setUp() {
        restServer = new RestServer();
        restServer.start();
    }

    @After
    public void tearDown() {
        restServer.stop();
    }

    @Test
    public void name() {
        ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
        resteasyClient.target("http://localhost:8080/accounts").request().put(null);
        resteasyClient.target("http://localhost:8080/accounts").request().put(null);

        resteasyClient.target("http://localhost:8080/accounts/1/replenish?amount=1000")
                .request().post(null);
        resteasyClient.target("http://localhost:8080/accounts/1/transfer?destAccount=2&amount=100")
                .request().post(null);

        String firstAccAmount = resteasyClient.target("http://localhost:8080/accounts/1/amount")
                .request().get(String.class);

        System.out.println("first acc amount=" + firstAccAmount);
        Assert.assertEquals("900", firstAccAmount);


    }
}
