package com.jdbc.dao;

import com.jdbc.bean.People;

import java.sql.Connection;
import java.util.List;

public class PeopleDAOimpl extends BaseDAO<People> implements PeopleDAO{


    @Override
    public void insert(Connection conn, People p) {
        String sql = "insert into people(playerID, birthYear, birthDay)values(?,?,?)";
        update(conn, sql, p.getPlayerID(), p.getBirthDay(), p.getBirthDay());
    }

    @Override
    public void deleteById(Connection conn, String id) {
        String sql = "delete from people wehre id = ?";
        update(conn, sql, id);
    }

    @Override
    public void update(Connection conn, People p) {
        String sql = "update people set playerId = ?, birthYear ?, birthDay = ?";
        update(conn, sql, p.getPlayerID(), p.getBirthYear(), p.getBirthDay());
    }

    @Override
    public People getPeopleById(Connection conn, String id) {
        String sql = "select playerID, birthYear, birthDa from people where playerID = ?";
        People p = getInstance(conn, sql, id);
        return p;
    }

    @Override
    public List<People> getAll(Connection conn) {
        String sql = "select playerID, birthYear, birthDay from people where birthYear = 1970 and birthDay = 20";
        return select(conn, sql);
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from people";
        return getValue(conn, sql);
    }
}
