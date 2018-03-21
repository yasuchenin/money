package services;

import model.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MoneyTransferService {
    private Map<Long, Account> accounts;

    public MoneyTransferService() {
        accounts = new ConcurrentHashMap<>();
    }

    public void transferMoney(long srcAccount, long destAccount, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Error, invalid amount");
        }
        if (srcAccount == destAccount) {
            throw new IllegalArgumentException("Error, same account numbers");
        }

        Account first, second;
        if (srcAccount > destAccount) {
            first = accounts.get(srcAccount);
            second = accounts.get(destAccount);
        } else {
            first = accounts.get(destAccount);
            second = accounts.get(srcAccount);
        }
        if (first == null || second == null) {
            throw new IllegalArgumentException("Error, wrong account number");
        }

        synchronized (first) {
            synchronized (second) {
                accounts.get(srcAccount).decreaseAmmount(amount);
                accounts.get(destAccount).increaseAmmount(amount);
            }

        }
    }
}
