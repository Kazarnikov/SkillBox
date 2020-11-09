import junit.framework.TestCase;

public class BankTest extends TestCase {
    Account account1;
    Account account2;
    Account account3;
    Bank bank;

    @Override
    protected void setUp() throws Exception {
        bank = new Bank();

        account1 = new Account(100000, "1");
        account2 = new Account(100000, "2");
        account3 = new Account(100000, "3");

        bank.setAccounts("1", account1);
        bank.setAccounts("2", account2);
        bank.setAccounts("3", account3);
    }

    public void testIsFraud() throws InterruptedException {
        for (int i = 0; i < 12; i++) {
            new Thread(() -> {
                try {
                    bank.transfer("1", "2", 60000);
                    bank.transfer("2", "1", 60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(5000);
        assertTrue(account1.getBlock());
        assertTrue(account2.getBlock());
    }

    public void testTransfer() throws InterruptedException {
        System.out.println("Before transfer: " +
                "\naccount №1 " + bank.getAccounts().get("1").getMoney() +
                "\naccount №2 " + bank.getAccounts().get("2").getMoney() +
                "\naccount №3 " + bank.getAccounts().get("3").getMoney() +
                "\nTotal Balance " + bank.getTotalBalance());

        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                try {
                    bank.transfer("1", "2", 1000);
                    bank.transfer("2", "3", 2000);
                    bank.transfer("3", "1", 3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        System.out.println("Before transfer: " +
                "\naccount №1 " + bank.getAccounts().get("1").getMoney() +
                "\naccount №2 " + bank.getAccounts().get("2").getMoney() +
                "\naccount №3 " + bank.getAccounts().get("3").getMoney() +
                "\nTotal Balance " + bank.getTotalBalance());

        Thread.sleep(100);
        long actual1 = bank.getBalance("1");
        long expected1 = 108000;
        long actual2 = bank.getBalance("2");
        long expected2 = 96000;
        long actual3 = bank.getBalance("3");
        long expected3 = 96000;

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
        assertEquals(expected3, actual3);
    }

    @Override
    protected void tearDown() throws Exception {
    }
}