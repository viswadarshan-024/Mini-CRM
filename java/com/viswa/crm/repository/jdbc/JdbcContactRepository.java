package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.Company;
import com.viswa.crm.model.Contact;
import com.viswa.crm.repository.ContactRepository;
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

@Repository
@RequiredArgsConstructor
public class JdbcContactRepository implements ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_ID =
            "SELECT id, company_id, name, email, phone, job_title, created_at " +
                    "FROM contacts WHERE id = ?";

    private static final String FIND_BY_COMPANY =
            "SELECT id, company_id, name, email, phone, job_title, created_at " +
                    "FROM contacts WHERE company_id = ? " +
                    "ORDER BY created_at DESC";

    private static final String SEARCH =
            "SELECT id, company_id, name, email, phone, job_title, created_at " +
                    "FROM contacts " +
                    "WHERE LOWER(name) LIKE LOWER(?) " +
                    "OR LOWER(email) LIKE LOWER(?) " +
                    "OR LOWER(phone) LIKE LOWER(?) " +
                    "ORDER BY created_at DESC";

    private static final String INSERT =
            "INSERT INTO contacts (company_id, name, email, phone, job_title, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE contacts SET name = ?, email = ?, phone = ?, job_title = ? " +
                    "WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM contacts WHERE id = ?";

    private static final String EXISTS =
            "SELECT COUNT(1) FROM contacts WHERE id = ?";

    private static final String EXISTS_BY_COMPANY =
            "SELECT COUNT(1) FROM contacts WHERE company_id = ?";

    @Override
    public Optional<Contact> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID, contactRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Contact> findByCompanyId(Long companyId) {
        return jdbcTemplate.query(
                FIND_BY_COMPANY,
                contactRowMapper(),
                companyId
        );
    }

    @Override
    public List<Contact> searchByKeyword(String keyword) {
        String like = "%" + keyword + "%";
        return jdbcTemplate.query(
                SEARCH,
                contactRowMapper(),
                like,
                like,
                like
        );
    }

    @Override
    public Long save(Contact contact) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, contact.getCompany().getId());
            ps.setString(2, contact.getName());
            ps.setString(3, contact.getEmail());
            ps.setString(4, contact.getPhone());
            ps.setString(5, contact.getJobTitle());
            ps.setTimestamp(6,
                    java.sql.Timestamp.valueOf(contact.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("id");
        return id.longValue();
    }

    @Override
    public void update(Contact contact) {

        jdbcTemplate.update(
                UPDATE,
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getJobTitle(),
                contact.getId()
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public boolean existsById(Long id) {

        Integer count = jdbcTemplate.queryForObject(
                EXISTS,
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    private RowMapper<Contact> contactRowMapper() {
        return (rs, rowNum) -> mapContact(rs);
    }

    private Contact mapContact(ResultSet rs) throws SQLException {

        Company company = new Company();
        company.setId(rs.getLong("company_id"));

        Contact contact = new Contact();
        contact.setId(rs.getLong("id"));
        contact.setCompany(company);
        contact.setName(rs.getString("name"));
        contact.setEmail(rs.getString("email"));
        contact.setPhone(rs.getString("phone"));
        contact.setJobTitle(rs.getString("job_title"));
        contact.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );

        return contact;
    }

    @Override
    public boolean existsByCompanyId(Long companyId) {

        Integer count = jdbcTemplate.queryForObject(
                EXISTS_BY_COMPANY,
                Integer.class,
                companyId
        );

        return count > 0;
    }

}
