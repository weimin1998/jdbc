package com.weimin;

import com.weimin.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestBatch {
    @Test
    public void testBatch() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into goods(name) values(?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            preparedStatement.setObject(1, "test" + i);
            preparedStatement.execute();
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000); // 1605

        JDBCUtils.close(connection, preparedStatement);
    }


    @Test
    public void testBatch1() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into goods(name) values(?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            preparedStatement.setObject(1, "test" + i);

            preparedStatement.addBatch();
            if (i % 50000 == 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
        }
        preparedStatement.executeBatch();
        preparedStatement.clearBatch();

        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000); // 1403s
        JDBCUtils.close(connection, preparedStatement);
    }

    @Test
    public void testBatch2() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);
        String sql = "insert into goods(name) values(?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            preparedStatement.setObject(1, "test" + i);

            preparedStatement.addBatch();
            if (i % 50000 == 0) {
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();
            }
        }
        preparedStatement.executeBatch();
        preparedStatement.clearBatch();

        connection.commit();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000); // 159s
        JDBCUtils.close(connection, preparedStatement);
    }
}
