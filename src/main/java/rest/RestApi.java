package rest;

import static spark.Spark.get;

public class RestApi {
    public static void initWs() {
        get("/", (req, res) -> "Hello, Mashka!");
    }
}
