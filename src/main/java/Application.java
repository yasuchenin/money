import rest.RestServer;

public class Application {
    public static void main(String[] args) {
        RestServer restServer = new RestServer();
        restServer.start();
    }
}
