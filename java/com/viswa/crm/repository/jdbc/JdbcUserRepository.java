package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.Role;
import com.viswa.crm.model.User;
import com.viswa.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


// Singleton pattern (by default with spring)
// DAO pattern

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT =
            "SELECT u.id, u.username, u.email, u.password_hash, u.full_name, u.created_at, " +
                    "r.id AS role_id, r.role_name " +
                    "FROM users u " +
                    "JOIN roles r ON u.role_id = r.id ";

    private static final String FIND_BY_USERNAME =
            BASE_SELECT + " WHERE u.username = ?";

    private static final String FIND_BY_ID =
            BASE_SELECT + " WHERE u.id = ?";

    private static final String FIND_ALL =
            BASE_SELECT + " ORDER BY u.created_at DESC";

    private static final String INSERT =
            "INSERT INTO users (username, email, password_hash, full_name, role_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String EXISTS_BY_ID =
            "SELECT COUNT(1) FROM users WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM users WHERE id = ?";


    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query(FIND_BY_USERNAME, userRowMapper(), username)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID, userRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL, userRowMapper());
    }

    @Override
    public Long save(User user) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getFullName());
            ps.setLong(5, user.getRole().getId());
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(user.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("id");
        return id.longValue();    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> mapUser(rs);
    }

    private User mapUser(ResultSet rs) throws SQLException {

        Role role = new Role();
        role.setId(rs.getLong("role_id"));
        role.setRoleName(rs.getString("role_name"));

        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setFullName(rs.getString("full_name"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setRole(role);

        return user;
    }

    @Override
    public boolean existsById(Long userId) {

        Integer count = jdbcTemplate.queryForObject(
                EXISTS_BY_ID,
                Integer.class,
                userId
        );

        return count > 0;
    }

    @Override
    public void deleteById(Long userId) {
        jdbcTemplate.update(DELETE, userId);
    }

}
