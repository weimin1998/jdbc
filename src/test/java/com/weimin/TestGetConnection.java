package com.weimin;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestGetConnection {

    @Test
    public void test1() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","root");
        Connection connect = driver.connect("jdbc:mysql://localhost:3306/atguigudb?serverTimezone=UTC", properties);
        System.out.println(connect);
    }

    @Test
    public void test2() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","root");
        Connection connect = driver.connect("jdbc:mysql://localhost:3306/atguigudb?serverTimezone=UTC", properties);
        System.out.println(connect);
    }

    @Test
    public void test3() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();
        DriverManager.registerDriver(driver);
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","root");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigudb?serverTimezone=UTC", properties);
        System.out.println(connection);
    }

    @Test
    public void test4() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","root");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigudb?serverTimezone=UTC", properties);
        System.out.println(connection);
    }

    @Test
    public void test5() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

        InputStream inputStream = TestGetConnection.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driver = properties.getProperty("driver");

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url,user,password);

        System.out.println(connection);

        System.out.println(connection.getMetaData());
        System.out.println(connection.getMetaData().getDatabaseProductName());

    }

}
