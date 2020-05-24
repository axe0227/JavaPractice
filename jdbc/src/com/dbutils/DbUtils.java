package com.dbutils;

import com.jdbc.bean.People;
import com.jdbc.util.Utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DbUtils {

    @Test
    public void testInsert(){

        QueryRunner runner = new QueryRunner();

        Connection conn = Utils.getConnection();
        String sql = "insert into people(playerID, birthYear, birthDay)values(?,?,?)";

        try {
            int count = runner.update(conn, sql, "AxeLord", 1998, 27);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Utils.closeResource(conn, null);
        }
    }

    @Test
    public void testQuery() {

        QueryRunner runner = new QueryRunner();
        Connection conn = Utils.getConnection();

        String sql = "select * from people where playerID = ?";
        BeanHandler<People> handler =  new BeanHandler<>(People.class);

        try {
            People p = runner.query(conn, sql, handler, "AxeLord");
            System.out.println(p);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Utils.closeResource(conn, null);
        }
    }


}
