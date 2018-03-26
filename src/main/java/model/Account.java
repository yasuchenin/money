package model;

/**
 * not thread-safe
 */
public class Account {
    private long amount;

    public Account() {
        this.amount = 0;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void decreaseAmmount(long decAmmount) {
        amount -= decAmmount;
    }

    public void increaseAmmount(long incAmmount) {
        amount += incAmmount;
    }
}
