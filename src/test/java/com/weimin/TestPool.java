package com.weimin;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class TestPool {

    @Test
    public void testC3p0() throws PropertyVetoException, SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/jdbc?serverTimezone=UTC");
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");

        //

        dataSource.setInitialPoolSize(10);

        System.out.println(dataSource);

        DataSources.destroy(dataSource);
    }

    @Test
    public void testC3p01() throws PropertyVetoException, SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource("helloC3p0");

        dataSource.setInitialPoolSize(10);

        System.out.println(dataSource);

        Connection connection = dataSource.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from customers");
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println(resultSet.getObject(1));
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        DataSources.destroy(dataSource);

    }

    @Test
    public void testDBCP() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setPassword("root");
        dataSource.setUsername("root");
        dataSource.setUrl("jdbc:mysql://localhost:3306/jdbc?serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");


        dataSource.setInitialSize(10);

        System.out.println(dataSource);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testDBCP1() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = TestPool.class.getClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(inputStream);
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

        System.out.println(dataSource);

        Connection connection = dataSource.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from customers");
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println(resultSet.getObject(1));
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        DataSources.destroy(dataSource);
    }

    @Test
    public void testDruid() throws Exception {
        Properties properties = new Properties();
        InputStream inputStream = TestPool.class.getClassLoader().getResourceAsStream("druid.properties");
        properties.load(inputStream);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        System.out.println(dataSource);

        Connection connection = dataSource.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select count(*) from customers");
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println(resultSet.getObject(1));
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        DataSources.destroy(dataSource);
    }
}
