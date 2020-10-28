import java.util.Random;

public class Main {

    private static Account account1 = new Account(1000000, "1");
    private static Account account2 = new Account(1000000, "2");
    private static Account account3 = new Account(1000000, "3");
    private static Account account4 = new Account(1000000, "4");
    private static Account account5 = new Account(1000000, "5");
    private static Account account6 = new Account(1000000, "6");
    private static Bank bank = new Bank();

    public static void main(String[] args) throws InterruptedException {
        bank.setAccounts("b1", account1);
        bank.setAccounts("b2", account2);
        bank.setAccounts("b3", account3);
        bank.setAccounts("b4", account4);
        bank.setAccounts("b5", account5);
        bank.setAccounts("b6", account6);

        bank.getAccounts().forEach((key, value) -> System.out.println(key + " : " + value.getMoney() + " " + value.getAccNumber()));
        System.out.println(bank.getTotalBalance());
        int i1 = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < i1; i++) {
            new Thread(() -> {
                try {
                    for (int q = 0; q < 10000; q++) {
//                        bank.getAccounts().get("b1").setBlock();
//                        bank.transfer("b1", "b2", 50001);
//                        bank.transfer("b2", "b1", 50000);

                        String[] s = {"b1", "b2", "b3", "b4", "b5", "b6"};
                        int rnd1 = new Random().nextInt(s.length);
                        int rnd2 = new Random().nextInt(s.length);
                        int amo = new Random().nextInt(60000);
                        if (rnd1 != rnd2) {
                            bank.transfer(s[rnd1], s[rnd2], amo);
                            bank.getTotalBalance();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }

        for (int w = 0; w < 5; w++) {
            bank.getAccounts().forEach((key, value) -> System.out.println(key + " : " + value.getMoney() + " " + value.getAccNumber()));
            System.out.println(bank.getTotalBalance());
            Thread.sleep(5000);
        }
    }
}
