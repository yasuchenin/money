package ru.yasuchenin.money.test;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.yasuchenin.money.rest.RestServer;

public class RestTest {
    private RestServer restServer;
    private ResteasyClient resteasyClient;

    @Before
    public void setUp() {
        restServer = new RestServer();
        restServer.start();
        resteasyClient = new ResteasyClientBuilder().build();
    }

    @After
    public void tearDown() {
        restServer.stop();
        try {
            Thread.sleep(2000); //because there is no "stop" handler in Spark
        } catch (InterruptedException ignore) {
        }

    }

    @Test
    public void testReplenish() {
        String id1 = resteasyClient.target("http://localhost:8080/accounts").request().put(null, String.class);
        String firstAccAmount = resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/amount")
                .request().get(String.class);
        Assert.assertEquals("0", firstAccAmount);

        resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/replenish?amount=100")
                .request().post(null);
        firstAccAmount = resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/amount")
                .request().get(String.class);
        Assert.assertEquals("100", firstAccAmount);

    }

    @Test
    public void testInvalidTransfer() {
        String id1 = resteasyClient.target("http://localhost:8080/accounts").request().put(null, String.class);
        String id2 = resteasyClient.target("http://localhost:8080/accounts").request().put(null, String.class);

        resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/replenish?amount=100")
                .request().post(null);
        String res = resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/transfer?destAccount=" + id2 + "&amount=500")
                .request().post(null, String.class);

        Assert.assertEquals("error", res);
    }

    @Test
    public void testTransfer() {
        String id1 = resteasyClient.target("http://localhost:8080/accounts").request().put(null, String.class);
        String id2 = resteasyClient.target("http://localhost:8080/accounts").request().put(null, String.class);

        resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/replenish?amount=1000")
                .request().post(null);
        resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/transfer?destAccount=" + id2 + "&amount=100")
                .request().post(null);

        String firstAccAmount = resteasyClient.target("http://localhost:8080/accounts/" + id1 + "/amount")
                .request().get(String.class);

        System.out.println("first acc amount=" + firstAccAmount);
        Assert.assertEquals("900", firstAccAmount);


    }
}
