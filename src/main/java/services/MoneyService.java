package services;

import model.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MoneyService {
    private Map<Long, Account> accounts;
    private volatile long currentAccId;

    public MoneyService() {
        accounts = new ConcurrentHashMap<>();
        currentAccId = 0;
    }

    public synchronized long createAccount() {
        accounts.put(++currentAccId, new Account());
        return currentAccId;
    }

    public long getAmount(String account) {
        Account acc = accounts.get(Long.valueOf(account));
        if (acc == null) {
            return 0;
        }
        synchronized (acc) {
            return acc.getAmount();
        }
    }

    public void replenishAccount(String account, String amount) {
        Account acc = accounts.get(Long.valueOf(account));
        if (acc == null) {
            throw new IllegalArgumentException("No such account=" + account);
        }
        synchronized (acc) {
            acc.increaseAmmount(Long.valueOf(amount));
        }

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
                Account arcAcc = accounts.get(srcAccount);
                if (arcAcc.getAmount() - amount < 0) {
                    throw new IllegalArgumentException("Not enough money!");
                }
                arcAcc.decreaseAmmount(amount);
                accounts.get(destAccount).increaseAmmount(amount);
            }

        }
    }
}
