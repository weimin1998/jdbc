package com.weimin;

import com.weimin.utils.JDBCUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestBlob {

    @Test
    public void testInsertPhoto() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "girl");
            preparedStatement.setString(2, "girl@qq.com");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse("1998-09-21");
            preparedStatement.setDate(3, new java.sql.Date(date.getTime()));
            preparedStatement.setBlob(4, Files.newInputStream(Paths.get("G:\\javacode\\jdbc\\jdbc\\src\\main\\resources\\girl.jpg")));

            preparedStatement.execute();
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(connection, preparedStatement);
        }
    }


    @Test
    public void testSelectBlob() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        String sql = "select id, name, email,birth, photo from customers where id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, 4);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            java.sql.Date birth = resultSet.getDate(4);

            System.out.println(id);
            System.out.println(name);
            System.out.println(email);
            System.out.println(birth);


            byte[] bytes = resultSet.getBytes(5);
            FileOutputStream fileOutputStream = new FileOutputStream("out.jpg");
            fileOutputStream.write(bytes);

        }
        JDBCUtils.close(connection, preparedStatement, resultSet);
    }
}
