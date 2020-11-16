
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {
    // private static String url = "https://lenta.ru/";
    // private static String domain = "//lenta.ru/";
    private static String url = "https://skillbox.ru/";
    private static String domain = "//skillbox.ru/";
    private static int nLevel = 3;

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        RecursiveLinkSite mapSite = new RecursiveLinkSite(url, domain, nLevel);
        long stats = System.currentTimeMillis();
        pool.execute(mapSite);
        System.out.println("Scanning started...");

        do {
            System.out.println("Parallelism: " + pool.getParallelism());
            System.out.println("Active Threads: " + pool.getActiveThreadCount());
            System.out.println("Task Count: " + pool.getQueuedTaskCount());
            System.out.println("Steal Count: " + pool.getStealCount());
            System.out.println("-------------------");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!mapSite.isDone());

        System.out.println("The site scanning has ended. Timeout: "
                + (System.currentTimeMillis() - stats) / 1000 + " s.");

        Set<String> results = mapSite.join();

        //   results.forEach(System.out::println);
        System.out.println("Writing to a file...");
        try (PrintWriter writer = new PrintWriter("data/URLs.txt")) {
            writer.write(url + "\n");
            results
                    .stream()
                    .sorted()
                    .forEach(el -> {
                        writer.write(tabUrl(el));
                    });
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Done");
        System.out.println("Total URLs: " + results.size());
    }


    static String tabUrl(String url) {
        int count = (int) url
                .chars()
                .filter(ch -> ch == '/')
                .count();
        return "\t".repeat(count - 3) + url + "\n";
    }


}

