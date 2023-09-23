package com.weimin;

import com.weimin.pojo.Customer;
import com.weimin.pojo.Order;
import com.weimin.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Common {

    @Test
    public void testCommonSelect() throws Exception {
        String sql = "select id, name, email,birth from customers where id = ?";
        System.out.println(selectOne(Customer.class, sql, 2));

        String sql1 = "select order_id as id, order_name as orderName, order_date as date from `order` where order_id = ?";
        System.out.println(selectOne(Order.class, sql1, 2));
    }

    @Test
    public void testSelectList() throws Exception {
        String sql = "select id, name, email,birth from customers where id > ?";
        System.out.println(select(Customer.class,sql,0));

        String sql1 = "select order_id as id, order_name as orderName, order_date as date from `order`";
        System.out.println(select(Order.class, sql1));
    }

    public static <T> T selectOne(Class<T> clazz, String sql, Object... args) throws Exception {
        Connection connection = JDBCUtils.getConnection();


        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        T t = null;
        if (resultSet.next()) {
            t = clazz.newInstance();
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnLabel(i + 1);
                Object value = resultSet.getObject(i + 1);

                Field field = clazz.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(t, value);
            }
        }
        JDBCUtils.close(connection, preparedStatement, resultSet);

        return t;
    }


    public static <T> List<T> select(Class<T> clazz, String sql, Object... args) throws Exception {
        Connection connection = JDBCUtils.getConnection();


        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }

        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<T> list = new ArrayList<>();
        while (resultSet.next()) {
            T t = clazz.newInstance();
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnLabel(i + 1);
                Object value = resultSet.getObject(i + 1);

                Field field = clazz.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(t, value);
            }
            list.add(t);
        }
        JDBCUtils.close(connection, preparedStatement, resultSet);

        return list;
    }

    public static void update(String sql, Object... args) throws Exception {
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

    public static void updateTx(Connection connection,String sql, Object... args) throws Exception {

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
            JDBCUtils.close(null, preparedStatement);
        }
    }
}
