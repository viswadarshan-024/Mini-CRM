package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.Role;
import com.viswa.crm.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcRoleRepository implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_ID =
            "SELECT id, role_name, created_at FROM roles WHERE id = ?";

    private static final String FIND_BY_NAME =
            "SELECT id, role_name, created_at FROM roles WHERE role_name = ?";

    @Override
    public Optional<Role> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID, roleRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Role> findByName(String roleName) {
        return jdbcTemplate.query(FIND_BY_NAME, roleRowMapper(), roleName)
                .stream()
                .findFirst();
    }

    private RowMapper<Role> roleRowMapper() {
        return (rs, rowNum) -> mapRole(rs);
    }

    private Role mapRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setRoleName(rs.getString("role_name"));
        role.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return role;
    }
}
