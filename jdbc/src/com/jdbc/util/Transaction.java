package com.jdbc.util;
import com.jdbc.bean.People;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    @Test
    public void testUpdate()  {

        Connection conn = null;
        try {
            conn = Utils.getConnection();

            conn.setAutoCommit(false);

            String sql1 = "update people set birthYear = birthYear  - 10 where playerID = ?";
            update(conn, sql1,"aardsda01");

            //simulate network abnormality
//            System.out.println(10/0);

            String sql2 = "update people set birthYear = birthYear + 10 where playerID = ?";
            update(conn, sql2, "aaronha01");

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Utils.closeResource(conn, null);
        }

    }

    @Test
    public void  testSelect() throws  Exception{

        Connection conn = Utils.getConnection();

        System.out.println(conn.getTransactionIsolation());
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        conn.setAutoCommit(false);

        String sql = "select playerID, birthYear, birthDay where playerID = ?";

        List<People> list = select(conn, People.class, sql, "aaronha01");

        list.forEach(System.out::println);

    }



    public int update(Connection conn, String sql, Object... args) {

//        Connection conn = null;
        PreparedStatement ps = null;
        try {

            conn = Utils.getConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Utils.closeResource(null, ps);
        }
        return 0;
    }

    public <T> List<T> select(Connection conn, Class<T> clazz, String sql, Object... args) {

//        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Utils.getConnection();

            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();

            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {

                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {

                    Object columnValue = rs.getObject(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);

                    if (columnValue != null)
                        field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeResource(null, ps, rs);
        }

        return null;
    }
}
