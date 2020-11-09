import java.util.Random;

public class Main {

    private static Bank bank = new Bank();
    private static final int NUMBER_ACC = 20;

    public static void main(String[] args) throws InterruptedException {

        String[] s = new String[NUMBER_ACC];
        for (int i = 0; i < NUMBER_ACC; i++) {
            s[i] = "b" + i;
            bank.setAccounts(s[i], new Account(100_000_000, i + ""));
        }

        // bank.getAccounts().forEach((key, value) -> System.out.println(key + " : " + value.getMoney() + " " + value.getAccNumber()));
        System.out.println(bank.getTotalBalance());
        int processors = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < processors; i++) {
            new Thread(() -> {
                try {
                    for (; ; ) {
                        int rnd1 = new Random().nextInt(s.length);
                        int rnd2 = new Random().nextInt(s.length);
                        int amo = new Random().nextInt(60000);
                        bank.transfer(s[rnd1], s[rnd2], amo);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }
//        for (int w = 0; w < 5; w++) {
//            bank.getAccounts().forEach((key, value) -> System.out.println(key + " : " + value.getMoney() + " " + value.getAccNumber()));
//            System.out.println(bank.getTotalBalance());
//            Thread.sleep(5000);
//        }
    }
}
