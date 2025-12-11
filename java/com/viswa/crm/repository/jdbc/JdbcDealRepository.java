package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.Company;
import com.viswa.crm.model.Contact;
import com.viswa.crm.model.Deal;
import com.viswa.crm.model.DealStatus;
import com.viswa.crm.model.User;
import com.viswa.crm.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcDealRepository implements DealRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String BASE_SELECT =
            "SELECT d.id, d.title, d.amount, d.status, d.created_at, " +
                    "u.id AS user_id, u.username, u.full_name, " +
                    "c.id AS company_id, c.name AS company_name, " +
                    "ct.id AS contact_id, ct.name AS contact_name " +
                    "FROM deals d " +
                    "JOIN users u ON d.user_id = u.id " +
                    "JOIN companies c ON d.company_id = c.id " +
                    "LEFT JOIN contacts ct ON d.contact_id = ct.id ";

    private static final String FIND_BY_ID =
            BASE_SELECT + "WHERE d.id = ?";

    private static final String FIND_BY_USER =
            BASE_SELECT + "WHERE d.user_id = ? ORDER BY d.created_at DESC";

    private static final String FIND_BY_COMPANY =
            BASE_SELECT + "WHERE d.company_id = ? ORDER BY d.created_at DESC";

    private static final String SEARCH =
            BASE_SELECT +
                    "WHERE LOWER(d.title) LIKE LOWER(?) " +
                    "OR LOWER(c.name) LIKE LOWER(?) " +
                    "OR LOWER(ct.name) LIKE LOWER(?) " +
                    "ORDER BY d.created_at DESC";

    private static final String INSERT =
            "INSERT INTO deals (title, amount, status, user_id, company_id, contact_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE deals " +
            "SET title = ?, amount = ?, status = ?, user_id = ?, contact_id = ? " +
            "WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM deals WHERE id = ?";

    private static final String EXISTS =
            "SELECT COUNT(1) FROM deals WHERE id = ?";

    private static final String COUNT_BY_STATUS =
            "SELECT status, COUNT(*) AS cnt FROM deals GROUP BY status";

    private static final String FIND_BY_STATUS =
            BASE_SELECT + "WHERE d.status = ? ORDER BY d.created_at DESC";

    private static final String EXISTS_ACTIVE_BY_CONTACT =
            "SELECT COUNT(1) FROM deals " +
                    "WHERE contact_id = ? AND status <> 'CLOSED'";

    private static final String EXISTS_BY_COMPANY =
            "SELECT COUNT(1) FROM deals WHERE company_id = ?";

    private static final String EXISTS_BY_USER =
            "SELECT COUNT(1) FROM deals WHERE user_id = ?";

    @Override
    public Optional<Deal> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID, dealRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Deal> findByStatus(DealStatus status) {
        return jdbcTemplate.query(
                FIND_BY_STATUS,
                dealRowMapper(),
                status.name()
        );
    }


    @Override
    public List<Deal> findByUserId(Long userId) {
        return jdbcTemplate.query(
                FIND_BY_USER,
                dealRowMapper(),
                userId
        );
    }

    @Override
    public List<Deal> findByCompanyId(Long companyId) {
        return jdbcTemplate.query(
                FIND_BY_COMPANY,
                dealRowMapper(),
                companyId
        );
    }

    @Override
    public List<Deal> search(String keyword) {
        String like = "%" + keyword + "%";
        return jdbcTemplate.query(
                SEARCH,
                dealRowMapper(),
                like,
                like,
                like
        );
    }

    @Override
    public Long save(Deal deal) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, deal.getTitle());
            ps.setBigDecimal(2, deal.getAmount());
            ps.setString(3, deal.getStatus().name());
            ps.setLong(4, deal.getAssignedUser().getId());
            ps.setLong(5, deal.getCompany().getId());

            if (deal.getContact() != null) {
                ps.setLong(6, deal.getContact().getId());
            } else {
                ps.setNull(6, java.sql.Types.BIGINT);
            }

            ps.setTimestamp(7,
                    java.sql.Timestamp.valueOf(deal.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("id");
        return id.longValue();
//        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Deal deal) {

        jdbcTemplate.update(
                UPDATE,
                deal.getTitle(),
                deal.getAmount(),
                deal.getStatus().name(),
                deal.getAssignedUser().getId(),
                deal.getContact() != null ? deal.getContact().getId() : null,
                deal.getId()
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

    @Override
    public Map<DealStatus, Long> countByStatus() {

        Map<DealStatus, Long> result = new EnumMap<>(DealStatus.class);

        jdbcTemplate.query(COUNT_BY_STATUS, rs -> {
            DealStatus status = DealStatus.valueOf(rs.getString("status"));
            long count = rs.getLong("cnt");
            result.put(status, count);
        });

        return result;
    }

    private RowMapper<Deal> dealRowMapper() {
        return (rs, rowNum) -> mapDeal(rs);
    }

    private Deal mapDeal(ResultSet rs) throws SQLException {

        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setFullName(rs.getString("full_name"));

        Company company = new Company();
        company.setId(rs.getLong("company_id"));
        company.setName(rs.getString("company_name"));

        Contact contact = null;
        Long contactId = rs.getLong("contact_id");
        if (!rs.wasNull()) {
            contact = new Contact();
            contact.setId(contactId);
            contact.setName(rs.getString("contact_name"));
        }

        Deal deal = new Deal();
        deal.setId(rs.getLong("id"));
        deal.setTitle(rs.getString("title"));
        deal.setAmount(rs.getBigDecimal("amount"));
        deal.setStatus(DealStatus.valueOf(rs.getString("status")));
        deal.setAssignedUser(user);
        deal.setCompany(company);
        deal.setContact(contact);
        deal.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return deal;
    }

    @Override
    public List<Deal> findByIds(List<Long> ids) {

        if (ids.isEmpty()) {
            return List.of();
        }

        String inSql = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql =
                "SELECT id, title " +
                        "FROM deals WHERE id IN (" + inSql + ")";

        return jdbcTemplate.query(
                sql,
                ids.toArray(),
                (rs, rowNum) -> {
                    Deal deal = new Deal();
                    deal.setId(rs.getLong("id"));
                    deal.setTitle(rs.getString("title"));
                    return deal;
                }
        );
    }

    @Override
    public boolean existsActiveDealByContactId(Long contactId) {

        Integer count = jdbcTemplate.queryForObject(
                EXISTS_ACTIVE_BY_CONTACT,
                Integer.class,
                contactId
        );

        return count > 0;
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

    @Override
    public boolean existsByAssignedUserId(Long userId) {

        Integer count = jdbcTemplate.queryForObject(
                EXISTS_BY_USER,
                Integer.class,
                userId
        );

        return count > 0;
    }

}
