package com.weimin;

import com.weimin.pojo.Customer;
import com.weimin.pojo.Order;
import com.weimin.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestPreparedStatement {


    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into customers(name,email,birth) values(?,?,?)";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "VI");
            preparedStatement.setString(2, "VI@qq.com");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse("1998-08-21");
            preparedStatement.setDate(3, new java.sql.Date(date.getTime()));

            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(connection, preparedStatement);
        }
    }

    @Test
    public void testUpdate() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            String sql = "update customers set email = ? where name = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "vi@qq.com");
            preparedStatement.setString(2, "VI");
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(connection, preparedStatement);
        }
    }

    @Test
    public void testDelete() throws Exception {
        String sql = "delete from customers where id = ?";
        update(sql, 1);
    }

    // 通用
    public void update(String sql, Object... args) throws Exception {
        Connection connection = JDBCUtils.getConnection();

        PreparedStatement preparedStatement = null;
        try {

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(connection, preparedStatement);
        }
    }


    @Test
    public void testSelect() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        String sql = "select id, name, email,birth from customers where id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, 2);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            java.sql.Date birth = resultSet.getDate(4);

            System.out.println(id);
            System.out.println(name);
            System.out.println(email);
            System.out.println(birth);
        }

        JDBCUtils.close(connection, preparedStatement, resultSet);
    }

    @Test
    public void testSelectOne() throws Exception {
        String sql = "select id, name, email,birth from customers where id = ?";
        Customer customer = selectOne(sql, 3);
        System.out.println(customer);

        String sql1 = "select id, name, email,birth from customers where name = ?";
        System.out.println(selectOne(sql1, "VI"));
    }

    //
    public Customer selectOne(String sql, Object... args) throws Exception {
        Connection connection = JDBCUtils.getConnection();


        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }


        ResultSet resultSet = preparedStatement.executeQuery();


        Customer customer = null;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println(metaData);
        System.out.println("columnCount" + columnCount);

        if (resultSet.next()) {
            customer = new Customer();
            for (int i = 0; i < columnCount; i++) {

                // 获取表的列名
                String columnName = metaData.getColumnName(i + 1);
                System.out.println("columnName: " + columnName);
                Object value = resultSet.getObject(i + 1);

                Field field = Customer.class.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(customer, value);
            }
        }

        JDBCUtils.close(connection, preparedStatement, resultSet);

        return customer;
    }

    @Test
    public void insertOrder() throws Exception {
        String sql = "insert into `order`(order_name, order_date) values(?, ?)";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("1998-08-21");

        update(sql, "test name", new java.sql.Date(date.getTime()));
    }

    @Test
    public void testSelectOrder() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        String sql = "select order_id, order_name, order_date from `order` where order_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, 1);

        ResultSet resultSet = preparedStatement.executeQuery();
        Order order = null;
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            java.sql.Date date = resultSet.getDate(3);

            order = new Order();
            order.setId(id);
            order.setOrderName(name);
            order.setDate(date);

        }
        System.out.println(order);
    }

    public Order selectOneOrder(String sql, Object... args) throws Exception {
        Connection connection = JDBCUtils.getConnection();


        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        Order order = null;
        if (resultSet.next()) {
            order = new Order();
            for (int i = 0; i < columnCount; i++) {
                // String columnName = metaData.getColumnName(i + 1);

                // 获取别名
                // 没有别名，就是列名
                String columnName = metaData.getColumnLabel(i + 1);
                System.out.println("columnName: " + columnName);
                Object value = resultSet.getObject(i + 1);

                Field field = Order.class.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(order, value);

            }

        }
        JDBCUtils.close(connection, preparedStatement, resultSet);
        return order;
    }

    @Test
    public void testSelectOrderOne() throws Exception {
        String sql = "select order_id as id, order_name as orderName, order_date as date from `order` where order_id = ?";
        System.out.println(selectOneOrder(sql, 2));
    }
}
