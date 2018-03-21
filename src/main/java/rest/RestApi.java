package rest;

import services.MoneyTransferService;

import java.util.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestApi {
    private static final Logger log = Logger.getLogger(RestApi.class.getName());
    private MoneyTransferService moneyService;

    public RestApi() {
        moneyService = new MoneyTransferService();
        post("/accounts/transfer", (req, res) -> {
            String srcAccount = req.queryParams("srcAccount");
            String destAccount = req.queryParams("destAccount");
            String amount = req.queryParams("amount");
            try {
                moneyService.transferMoney(Long.valueOf(srcAccount), Long.valueOf(destAccount), Long.valueOf(amount));
            } catch (IllegalArgumentException ex) {
                log.info("Error transfer money!" + ex);
                return "error";
            }
            System.out.println(srcAccount.getClass() + "!" + destAccount.getClass());
            return "ok";
        });
    }

}
