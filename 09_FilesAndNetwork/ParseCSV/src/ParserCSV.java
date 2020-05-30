import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserCSV {

    private static String dataFile = "res/movementList.csv";
    private static double incomeTotal = 0.0;
    private static double expenseTotal = 0.0;
    private static Map<String, Double> expenseList = new HashMap<>();

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                parseFail(line);
            }
            printData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseFail(String line) {
        String[] fragments = line.replaceAll("\"", "").split(",", 8);
        Matcher matcher = Pattern.compile("\\w+\\s?_?\\w+>?\\w+\\s?\\w?\\s{7,}").matcher(fragments[5]);
        matcher.find();
        String nameGroup = matcher.group(0).trim();
        incomeTotal += Double.parseDouble(fragments[6]);
        double expense = Double.parseDouble(fragments[7].replaceAll(",", "."));
        if (expense != 0) {
            expenseTotal += expense;
            calculate(nameGroup, expense);
        }
    }

    private static void calculate(String nameGroup, Double value) {
        if (expenseList.containsKey(nameGroup)) {
            double sum = expenseList.get(nameGroup) + value;
            expenseList.put(nameGroup, sum);
        } else {
            expenseList.put(nameGroup, value);
        }
    }

    private static void printData() {
        System.out.println("ОБЩЕЕ ПОСТУПЛЕНИЕ: " + incomeTotal);
        System.out.println("ОБЩИЙ РАСХОД: " + expenseTotal);
        System.out.println();
        System.out.println("\t == ГРУППЫ РАСХОДОВ И ИХ СУММА ==\n");
        expenseList.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.format("%-30s - %.2f %-10s\n", e.getKey(), e.getValue(), "руб"));
    }
}
