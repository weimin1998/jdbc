package com.weimin;

import com.weimin.pojo.Customer;
import com.weimin.utils.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TestDBUtils {
    @Test
    public void test1() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        int updated = queryRunner.update(connection, sql, "ikun", "ikun@qq.com", "1998-08-21");

        System.out.println(updated);

        JDBCUtils.close(connection, null);
    }

    @Test
    public void test2() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select id, name, email,birth from customers where id = ?";
        Customer customer = queryRunner.query(connection, sql, new BeanHandler<>(Customer.class), 5);

        System.out.println(customer);

        JDBCUtils.close(connection, null);
    }

    @Test
    public void test3() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select id, name, email,birth from customers";
        List<Customer> customers = queryRunner.query(connection, sql, new BeanListHandler<>(Customer.class));

        System.out.println(customers);

        JDBCUtils.close(connection, null);
    }

    @Test
    public void test4() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select id, name, email,birth from customers where id = ?";
        Map<String, Object> queried = queryRunner.query(connection, sql, new MapHandler(), 5);

        System.out.println(queried);

        JDBCUtils.close(connection, null);
    }

    @Test
    public void test5() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select id, name, email,birth from customers";
        List<Map<String, Object>> query = queryRunner.query(connection, sql, new MapListHandler());

        System.out.println(query);


        JDBCUtils.close(connection, null);
    }

    @Test
    public void test6() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select count(*) from customers";
        Object query = queryRunner.query(connection, sql, new ScalarHandler<>());

        System.out.println(query);

        JDBCUtils.close(connection, null);
    }

    @Test
    public void test7() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select max(id) from customers";
        Object query = queryRunner.query(connection, sql, new ScalarHandler<>());

        System.out.println(query);

        JDBCUtils.close(connection, null);
    }

    @Test
    public void test8() throws Exception {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = JDBCUtils.getConnection();
        String sql = "select id, name, email,birth from customers where id = ?";

        ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
            @Override
            public Customer handle(ResultSet rs) throws SQLException {
                Customer customer = null;
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                if (rs.next()) {
                    customer = new Customer();
                    for (int i = 0; i < columnCount; i++) {

                        String columnName = metaData.getColumnName(i + 1);
                        Object value = rs.getObject(i + 1);

                        try {
                            Field field = Customer.class.getDeclaredField(columnName);
                            field.setAccessible(true);
                            field.set(customer, value);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return customer;
            }
        };
        Customer customer = queryRunner.query(connection, sql, handler, 5);

        System.out.println(customer);
        JDBCUtils.close(connection, null);
    }

    @Test
    public void test9() {
        QueryRunner queryRunner = new QueryRunner();

        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        try {
            DbUtils.close(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test10() {
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DbUtils.closeQuietly(connection);

    }
}
