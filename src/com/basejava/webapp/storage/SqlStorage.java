package com.basejava.webapp.storage;

import com.basejava.webapp.exceptions.ExistStorageException;
import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.ConnectionFactory;
import com.basejava.webapp.sql.SqlHelper;
import com.basejava.webapp.sql.SqlQueries;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Comparator.comparing;

public class SqlStorage implements Storage {
    private static final Logger LOG = Logger.getLogger(SqlStorage.class.getName());
    private static final Comparator<Resume> COMPARATOR =
            comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public List<Resume> getAllSorted() {
        return SqlHelper.executeWithException(connectionFactory, conn -> {
            List<Resume> resumes = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_SELECT_ALL)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid").trim(),
                            rs.getString("full_name").trim()));
                }
            }
            resumes.sort(COMPARATOR);
            LOG.info("getAllSorted - done");
            return resumes;
        });
    }

    @Override
    public int getSize() {
        return SqlHelper.executeWithException(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_SELECT_ALL_COUNT)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    LOG.info("returned size resume database");
                    return rs.getInt(1);
                }
                LOG.info("returned size '0' resume database");
                return 0;
            }
        });
    }

    @Override
    public void save(Resume resume) {
        SqlHelper.executeWithException(connectionFactory, conn -> {
            if (checkExisting(resume)) {
                LOG.log(Level.WARNING, "Resume '" + resume.getUuid() + "' already exist");
                throw new ExistStorageException(resume.getUuid());
            }

            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_INSERT)) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
        });
        LOG.info("saved: " + resume);
    }

    @Override
    public void delete(String uuid) {
        SqlHelper.executeWithException(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_DELETE_UUID)) {
                ps.setString(1, uuid);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
                    throw new NotExistStorageException(uuid);
                }
            }
        });
        LOG.info("deleted: " + uuid);
    }

    @Override
    public Resume get(String uuid) {
        return SqlHelper.executeWithException(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_SELECT_UUID)) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    LOG.log(Level.WARNING, "Resume '" + uuid + "' not exist");
                    throw new NotExistStorageException(uuid);
                }
                LOG.info("got resume: " + uuid);
                return new Resume(uuid, rs.getString("full_name"));
            }
        });
    }

    @Override
    public void clear() {
        SqlHelper.executeWithException(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_DELETE_ALL)) {
                ps.execute();
            }
        });
        LOG.info("cleared");
    }

    @Override
    public void update(Resume resume) {
        SqlHelper.executeWithException(connectionFactory, conn -> {
            if (!checkExisting(resume)) {
                LOG.log(Level.WARNING, "Resume '" + resume.getUuid() + "' not exist");
                throw new NotExistStorageException(resume.getUuid());
            }

            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_UPDATE)) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                ps.executeUpdate();
            }
        });
        LOG.info("updated: " + resume);
    }

    private boolean checkExisting(Resume resume) {
        return SqlHelper.executeWithException(connectionFactory, conn -> {
            try (PreparedStatement ps = conn.prepareStatement(SqlQueries.SQL_SELECT_UUID)) {
                ps.setString(1, resume.getUuid());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        });
    }
}