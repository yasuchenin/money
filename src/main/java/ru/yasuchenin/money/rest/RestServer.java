package ru.yasuchenin.money.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yasuchenin.money.services.MoneyService;
import spark.Spark;

import static spark.Spark.*;

public class RestServer {
    private static final Logger log = LoggerFactory.getLogger(RestServer.class);
    private MoneyService moneyService;

    public RestServer() {
        moneyService = new MoneyService();
    }

    public void start() {
        port(8080);
        post("/accounts/:srcAccount/transfer", (req, res) -> {
            String srcAccount = req.params(":srcAccount");
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

        post("/accounts/:account/replenish", (req, res) -> {
            String srcAccount = req.params(":account");
            String amount = req.queryParams("amount");
            log.info("Replenish account={} by amount={}", srcAccount, amount);
            moneyService.replenishAccount(srcAccount, amount);
            return "ok";
        });

        get("/accounts/:account/amount", (req, res) -> {
            String srcAccount = req.params(":account");
            log.info("Getting amount for account={}", srcAccount);
            return moneyService.getAmount(srcAccount);
        });

        put("/accounts", (req, res) -> {
            long accId = moneyService.createAccount();
            log.info("Account id={} was created", accId);
            return accId;
        });
    }

    public void stop() {
        Spark.stop();
    }

}
