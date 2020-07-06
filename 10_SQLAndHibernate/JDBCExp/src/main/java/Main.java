import org.w3c.dom.ls.LSOutput;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox?useSSL=false&serverTimezone=UTC";
        String user ="root";
        String pass = "147258";

        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT course_name, (count(*)/ (MAX(MONTH(subscription_date)) -  MIN(MONTH(subscription_date)))) AS purchases_month FROM  PurchaseList GROUP BY course_name");

            System.out.printf("%15s %53s\n", "Курсы", "| Средние число покупок в месяц |");
            System.out.println("---------------------------------------------------------------------");

            while (set.next()){
                System.out.printf("%-35s | %17s %13s\n", set.getString("course_name"), set.getString("purchases_month"), "|");
            }
            System.out.println("---------------------------------------------------------------------");

            set.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
