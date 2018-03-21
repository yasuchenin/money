package rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.MoneyTransferService;

import static spark.Spark.post;

public class RestApi {
    private static final Logger log = LoggerFactory.getLogger(RestApi.class);
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
                log.info("Error transfer money! srcAccount={}, destAcc={}, amount={}",
                        srcAccount, destAccount, amount, ex);
                return "error";
            }
            log.info("Successful money transfer. srcAccount={}, destAcc={}, amount={}",
                    srcAccount, destAccount, amount);
            return "ok";
        });
    }

}
