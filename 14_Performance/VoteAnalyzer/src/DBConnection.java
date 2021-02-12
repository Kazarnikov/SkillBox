import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    private static final String dbName = "learn";
    private static final String dbUser = "myRoot";
    private static final String dbPass = "147258369";
    private static final String url = "jdbc:mysql://localhost:3306/" + dbName +
            "?user=" + dbUser +
            "&password=" + dbPass;
    private static final int lengthBuffer = 2_000_000;
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
                        "PRIMARY KEY(id), KEY(name(50))) ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void executeMultiInsert() throws SQLException {
        String sql = "INSERT INTO voter_count (name, birthDate) " +
                "VALUES" + insetQuery.toString();
        DBConnection.getConnection().createStatement().execute(sql);
        insetQuery.setLength(0);
    }

    public static void countVoter(String name, String birthDay) throws SQLException {
        birthDay = birthDay.replace('.', '-');
        insetQuery.append(insetQuery.length() == 0 ? " " : ", ")
                .append("('")
                .append(name)
                .append("', '")
                .append(birthDay)
                .append(" ')");
        if (insetQuery.length() > lengthBuffer) {
            executeMultiInsert();
        }
    }

    public static int customSelect() throws SQLException {
        String sql = "SELECT id FROM voter_count WHERE name = 'Белопухов Аникей'";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        if (!rs.next()) {
            return -1;
        } else {
            return rs.getInt("id");
        }
    }

    public static void printVoterCounts() throws SQLException {
        String sql = "SELECT name, birthDate, count(*) FROM voter_count " +
                "GROUP BY name, birthDate " +
                "HAVING count(*) > 1 " +
                "ORDER BY name;";
        ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
        while (rs.next()) {
            System.out.printf("\t%-25s (%s) - %d\n",
                    rs.getString("name"),
                    rs.getString("birthDate"),
                    rs.getInt("count(*)"));
        }
        rs.close();
    }
}