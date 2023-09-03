package com.basejava.webapp.sql;

public class SqlQueries {
    public static final String SQL_SELECT_ALL = "SELECT * FROM resume";
    public static final String SQL_SELECT_ALL_COUNT = "SELECT COUNT(*) FROM resume";
    public static final String SQL_INSERT = "INSERT INTO resume (uuid, full_name) VALUES (?,?)";
    public static final String SQL_DELETE_UUID = "DELETE FROM resume r WHERE r.uuid = ?";
    public static final String SQL_DELETE_ALL = "DELETE FROM resume";
    public static final String SQL_SELECT_UUID = "SELECT * FROM resume r WHERE r.uuid = ?";
    public static final String SQL_UPDATE = "UPDATE resume r SET full_name = ? WHERE r.uuid = ?";
}