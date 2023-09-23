import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

public class Test {
    public static void main(String[] args) {
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            Properties properties = new Properties();
            properties.setProperty("user","root");
            properties.setProperty("password","root");
            //properties.setProperty("serverTimezone","UTC");
            Connection connect = driver.connect("jdbc:mysql://localhost:3306/atguigudb?serverTimezone=UTC", properties);
            System.out.println(connect);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
