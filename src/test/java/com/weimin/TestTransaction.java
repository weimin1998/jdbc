package com.weimin;

import com.weimin.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

public class TestTransaction {

    @Test
    public void test1() throws Exception {

        String sql = "update user_table set balance = ? where id = ?";
        Common.update(sql,1000,1);

        Common.update(sql,1000,2);
    }

    @Test
    public void test2() throws Exception {

        String sql = "update user_table set balance = ? where id = ?";
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);

        try {
            Common.updateTx(connection,sql,900,1);

            int a = 10/0;
            Common.updateTx(connection,sql,1100,2);

            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        }finally {
            // 还原
            connection.setAutoCommit(true);
            JDBCUtils.close(connection,null);
        }
    }
}
