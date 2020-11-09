import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private final int SUMMA_CHECK = 50_000;
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud()
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
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {

        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);

        String fromNumber = fromAccount.getAccNumber();
        String toNumber = toAccount.getAccNumber();

        if (fromNumber.equals(toNumber)) {
            System.out.println(Thread.currentThread().getName() + " Sender account № " + fromAccount.getAccNumber()
                    + " must not match the recipient's accounts № " + toAccount.getAccNumber());
            return;
        }

        Account lock1 = fromNumber.compareTo(toNumber) < 0 ? fromAccount : toAccount;
        Account lock2 = fromNumber.compareTo(toNumber) < 0 ? toAccount : fromAccount;

        synchronized (lock1) {
            synchronized (lock2) {
                if (!(fromAccount.getBlock() || toAccount.getBlock())) {

                    long fromMoney = fromAccount.getMoney();
                    long toMoney = toAccount.getMoney();

                    if (fromMoney < amount) {
                        System.out.println(Thread.currentThread().getName() + " Insufficient funds in the account № "
                                + fromNumber + " for translation.");
                        return;
                    }

                    fromAccount.setMoney(fromMoney - amount);
                    toAccount.setMoney(toMoney + amount);
                    System.out.printf("%1$s: %2$d from %3$s to %4$s\nTotal Balance: %5$d\n\n",
                            Thread.currentThread().getName(), amount, fromNumber, toNumber, getTotalBalance());

                    if (amount > SUMMA_CHECK) {
                        if (!isFraud()) {
                            fromAccount.setBlock();
                            toAccount.setBlock();
                            System.out.println(Thread.currentThread().getName() + ": block accounts № "
                                    + fromNumber + "   № " + toNumber + "\n");
                        }
                    }
                }
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getTotalBalance() {
        return accounts.values().stream().mapToLong(Account::getMoney).sum();
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(String n, Account account) {
        this.accounts.put(n, account);
    }
}
