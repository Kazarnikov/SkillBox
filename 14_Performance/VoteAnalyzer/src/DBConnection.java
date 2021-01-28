import java.sql.*;

public class DBConnection {
    private static Connection connection;

    private static final String dbName = "learn";
    private static final String dbUser = "myRoot";
    private static final String dbPass = "147258369";
    private static final String url = "jdbc:mysql://localhost:3306/" + dbName +
            "?user=" + dbUser +
            "&password=" + dbPass;
    private static final int lengthBuffer = 2_500_000;
    private static final StringBuilder insetQuery = new StringBuilder();

    public static Connection getConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url);
                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");
                connection.createStatement().execute("CREATE TABLE voter_count(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "name TINYTEXT NOT NULL, " +
                        "birthDate DATE NOT NULL, " +
                        "`count` INT NOT NULL, " +
                        "PRIMARY KEY(id), " +
                        "UNIQUE KEY name_date(name(50), birthDate))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void executeMultiInsert() throws SQLException {
        String sql = "INSERT INTO voter_count (name, birthDate, `count`) " +
                "VALUES" + insetQuery.toString() +
                "ON DUPLICATE KEY UPDATE `count` = `count` + 1";
        DBConnection.getConnection().createStatement().execute(sql);
        insetQuery.setLength(0);
    }

    public static void countVoter(String name, String birthDay) throws SQLException {
        birthDay = birthDay.replace('.', '-');
        insetQuery.append((insetQuery.length() == 0 ? " " : ", ") +
                "('" + name + "', '" + birthDay + "', 1)");
        if (insetQuery.length() > lengthBuffer){
            executeMultiInsert();
        }
    }

    public static void printVoterCounts() throws SQLException {
        String sql = "SELECT name, birthDate, `count` FROM voter_count WHERE `count` > 1";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.println("\t" + rs.getString("name") + " (" +
                    rs.getString("birthDate") + ") - " + rs.getInt("count"));
        }
        rs.close();
    }
}