package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.exceptions.StorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Comparator.comparing;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    protected static final Comparator<Resume> COMPARATOR =
            comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid").trim(),
                        rs.getString("full_name").trim()));
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        resumes.sort(COMPARATOR);
        LOG.info("getAllSorted - done");
        return resumes;
    }

    @Override
    public int getSize() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM resume")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LOG.info("returned size resume database");
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        LOG.info("returned size '0' resume database");
        return 0;
    }

    @Override
    public void save(Resume resume) {
        try (Connection conn = connectionFactory.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT uuid FROM resume WHERE uuid = ?")) {
                ps.setString(1, resume.getUuid());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    LOG.log(Level.WARNING, "Resume '" + resume.getUuid() + "' already exist");
                    throw new ExistStorageException(resume.getUuid());
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
                LOG.info("saved: " + resume);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume r WHERE r.uuid = ?")) {
            ps.setString(1, uuid);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
                throw new NotExistStorageException(uuid);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        LOG.info("deleted: " + uuid);
    }

    @Override
    public Resume get(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                LOG.log(Level.WARNING,"Resume '" + uuid + "' not exist");
                throw new NotExistStorageException(uuid);
            }
            LOG.info("got: " + uuid);
            return new Resume(uuid, rs.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void clear() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        LOG.info("cleared");
    }

    @Override
    public void update(Resume resume) {
        try (Connection conn = connectionFactory.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT uuid FROM resume WHERE uuid = ?")){
                ps.setString(1, resume.getUuid());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    LOG.log(Level.WARNING, "Resume '" + resume.getUuid() + "' not exist");
                    throw new NotExistStorageException(resume.getUuid());
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume r SET ? WHERE r.uuid = ?")){
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
        LOG.info("updated: " + resume);
    }
}