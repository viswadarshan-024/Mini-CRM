package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.Company;
import com.viswa.crm.repository.CompanyRepository;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcCompanyRepository implements CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_ID =
            "SELECT id, name, email, phone, address, created_at " +
                    "FROM companies WHERE id = ?";

    private static final String FIND_BY_NAME =
            "SELECT id, name, email, phone, address, created_at " +
                    "FROM companies WHERE name = ?";

    private static final String FIND_ALL =
            "SELECT id, name, email, phone, address, created_at " +
                    "FROM companies ORDER BY created_at DESC";

    private static final String SEARCH =
            "SELECT id, name, email, phone, address, created_at " +
                    "FROM companies " +
                    "WHERE LOWER(name) LIKE LOWER(?) " +
                    "OR LOWER(email) LIKE LOWER(?) " +
                    "ORDER BY created_at DESC";

    private static final String INSERT =
            "INSERT INTO companies (name, email, phone, address, created_at) "
                    + "VALUES (?,?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE companies SET name = ?, email = ?, phone = ?, address = ? " +
                    "WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM companies WHERE id = ?";

    private static final String EXISTS_BY_ID =
            "SELECT COUNT(1) FROM companies WHERE id = ?";

    @Override
    public Optional<Company> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID, companyRowMapper(), id)
                .stream()
                .findFirst();
    }
    @Override
    public Optional<Company> findByName(String name) {
        return jdbcTemplate.query(FIND_BY_NAME, companyRowMapper(), name)
                .stream()
                .findFirst();
    }

    @Override
    public List<Company> findAll() {
        return jdbcTemplate.query(FIND_ALL, companyRowMapper());
    }

    @Override
    public List<Company> searchByKeyword(String keyword) {
        String like = "%" + keyword + "%";
        return jdbcTemplate.query(
                SEARCH,
                companyRowMapper(),
                like,
                like
        );
    }

    @Override
    public Long save(Company company) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, company.getName());
            ps.setString(2, company.getEmail());
            ps.setString(3, company.getPhone());
            ps.setString(4, company.getAddress());
            ps.setTimestamp(5,
                    java.sql.Timestamp.valueOf(company.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("id");
        return id.longValue();
    }

    @Override
    public void update(Company company) {

        jdbcTemplate.update(
                UPDATE,
                company.getName(),
                company.getEmail(),
                company.getPhone(),
                company.getAddress(),
                company.getId()
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public boolean existsById(Long id) {

        Integer count = jdbcTemplate.queryForObject(
                EXISTS_BY_ID,
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private RowMapper<Company> companyRowMapper() {
        return (rs, rowNum) -> mapCompany(rs);
    }

    private Company mapCompany(ResultSet rs) throws SQLException {

        Company company = new Company();
        company.setId(rs.getLong("id"));
        company.setName(rs.getString("name"));
        company.setEmail(rs.getString("email"));
        company.setPhone(rs.getString("phone"));
        company.setAddress(rs.getString("address"));
        company.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );

        return company;
    }
}
