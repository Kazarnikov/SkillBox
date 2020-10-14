public class Main {

   public static long start = System.currentTimeMillis();

    public static void main(String[] args) {
        int processors = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < processors; i++) {
            new ImageResizer().start();
        }
        System.out.println("Main Thread time " + (System.currentTimeMillis() - start) +" ms\n"+
                "Total Thread " + processors);
    }
}
