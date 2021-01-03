import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static final long runStart = System.currentTimeMillis();
    private static long sumTimeGenerate = 0;
    private static long sumTimeWrite = 0;
    private static final int REGIONS = 142;
    private static final int processors = Runtime.getRuntime().availableProcessors();
    private static final List<Future<List<Long>>> listTime = new ArrayList<>();

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(processors);
        for (int proc = 1; proc <= REGIONS; proc++) {
            Future<List<Long>> future = service.submit(new Loader(proc));
            listTime.add(future);
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            sum();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.print("Average time to generate a single file: " + sumTimeGenerate / REGIONS + " ms\n" +
                "Average time to write a single file to disk: " + sumTimeWrite / REGIONS + " ms\n" +
                "Approximate running time in single-threaded mode: " + (sumTimeWrite + sumTimeGenerate) + " ms\n" +
                "Program running time: " + (System.currentTimeMillis() - runStart) + " ms\n" +
                "Used threads:" + processors);
    }

    public static void sum() throws ExecutionException, InterruptedException {
        for (Future<List<Long>> sum : listTime) {
            sumTimeGenerate += sum.get().get(0);
            sumTimeWrite += sum.get().get(1);
        }
    }
}