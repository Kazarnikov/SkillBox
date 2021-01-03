import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Loader implements Callable<List<Long>> {
    /**
     * 1 - All letters Cyrillic:    Run time 19327 ms, ~20.0 - 23.0 MB size file
     * 2 - One letters Cyrillic:    Run time 14153 ms, ~16.0 - 17.7 MB size file
     * 3 - All letters Latin:       Run time 11621 ms, ~15.5 - 17.3 MB size file
     */
    private final char[] letters = {'У', 'K', 'E', 'H', 'X', 'B', 'A', 'P', 'O', 'C', 'M', 'T'}; //2
    //        private final char[] letters = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'}; //1
    private final String regionCode;
    private final StringBuilder numberStr;
    private final List<Long> time;
    private long startGenerate;
    private long endGenerateStartWrite;
    private long endWrite;

    public Loader(int regionCode) {
        this.numberStr = new StringBuilder();
        this.time = new ArrayList<>();
        this.regionCode = padNumber(regionCode, 2).toString();
    }

    @Override
    public List<Long> call() {
        try {
            StringBuilder buffer = new StringBuilder();
            FileWriter writer = new FileWriter("res/numbers_region_" + regionCode + ".txt");
            startGenerate = System.currentTimeMillis();
            for (int number = 1; number < 1000; number++) {
                for (char firstLetter : letters) {
                    for (char secondLetter : letters) {
                        for (char thirdLetter : letters) {
                            buffer.append(firstLetter);
                            buffer.append(padNumber(number, 3));
                            buffer.append(secondLetter);
                            buffer.append(thirdLetter);
                            buffer.append(regionCode);
                            buffer.append("\n");
                        }
                    }
                }
            }
            endGenerateStartWrite = System.currentTimeMillis();
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
            endWrite = System.currentTimeMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
        time.add(endGenerateStartWrite - startGenerate);
        time.add(endWrite - endGenerateStartWrite);
        return time;
    }

    private StringBuilder padNumber(int number, int numberLength) {
        numberStr.setLength(0);
        numberStr.append(number);
        int padSize = numberLength - numberStr.length();
        for (int i = 0; i < padSize; i++) {
            numberStr.insert(0, '0');
        }
        return numberStr;
    }
}