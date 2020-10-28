import java.util.HashMap;
import java.util.Random;

public class Bank {
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public synchronized void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);

        if (fromAccount.getBlock() || toAccount.getBlock()) {
            return;
        }

        long fromMoney = fromAccount.getMoney();
        long toMoney = toAccount.getMoney();

        if (fromMoney < amount) {
            System.out.println("Недостаточно средств на счете: " + fromAccountNum + " для перевода.");
            return;
        }

        fromAccount.setMoney(fromMoney - amount);
        toAccount.setMoney(toMoney + amount);
        accounts.put(fromAccountNum, fromAccount);
        accounts.put(toAccountNum, toAccount);

        if (amount > 50000) {
            if (!isFraud(fromAccountNum, toAccountNum, amount)) {
                fromAccount.setBlock();
                toAccount.setBlock();
                System.out.println("block accounts №" + fromAccount.getAccNumber() + "   №" + toAccount.getAccNumber());
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public synchronized long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public synchronized long getTotalBalance() {
        return accounts.values().stream().mapToLong(Account::getMoney).sum();
    }

    public HashMap<String, Account> getAccounts() throws InterruptedException {
        return accounts;
    }

    public void setAccounts(String n, Account account) {
        this.accounts.put(n, account);
    }

    public Random getRandom() {
        return random;
    }

    public void setAccounts(HashMap<String,Account> accounts) {
        this.accounts = accounts;
    }
}
