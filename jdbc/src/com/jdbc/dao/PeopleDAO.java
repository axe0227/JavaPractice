package com.jdbc.dao;

import com.jdbc.bean.People;

import java.sql.Connection;
import java.util.List;


public interface PeopleDAO {

    void insert(Connection conn, People p);

    void deleteById(Connection conn, String id);

    void update(Connection conn, People p);

    People getPeopleById(Connection conn, String id);

    List<People> getAll(Connection conn);

    Long getCount(Connection conn);

}
