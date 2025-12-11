package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.Activity;
import com.viswa.crm.model.ActivityStatus;
import com.viswa.crm.model.ActivityType;
import com.viswa.crm.model.Deal;
import com.viswa.crm.repository.ActivityRepository;
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
public class JdbcActivityRepository implements ActivityRepository {

    private final JdbcTemplate jdbcTemplate;

//    Query
    private static final String FIND_BY_ID =
            "SELECT id, deal_id, type, status, description, due_date, created_at " +
                    "FROM activities WHERE id = ?";

    private static final String FIND_BY_DEAL =
            "SELECT id, deal_id, type, status, description, due_date, created_at " +
                    "FROM activities WHERE deal_id = ? " +
                    "ORDER BY due_date ASC";

    private static final String FIND_BY_STATUS =
            "SELECT id, deal_id, type, status, description, due_date, created_at " +
                    "FROM activities WHERE status = ? " +
                    "ORDER BY due_date ASC";

    private static final String INSERT =
            "INSERT INTO activities (deal_id, type, status, description, due_date, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE activities SET type = ?, status = ?, description = ?, due_date = ? " +
                    "WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM activities WHERE id = ?";

    private static final String EXISTS =
            "SELECT COUNT(1) FROM activities WHERE id = ?";

    private static final String FIND_RECENT =
            "SELECT id, deal_id, type, status, description, due_date, created_at " +
                    "FROM activities " +
                    "ORDER BY due_date DESC " +
                    "LIMIT ?";

    private static final String EXISTS_BY_DEAL =
            "SELECT COUNT(1) FROM activities WHERE deal_id = ?";

    @Override
    public Optional<Activity> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID, activityRowMapper(), id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Activity> findByDealId(Long dealId) {
        return jdbcTemplate.query(
                FIND_BY_DEAL,
                activityRowMapper(),
                dealId
        );
    }

    @Override
    public List<Activity> findByStatus(ActivityStatus status) {
        return jdbcTemplate.query(
                FIND_BY_STATUS,
                activityRowMapper(),
                status.name()
        );
    }

    @Override
    public Long save(Activity activity) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    INSERT,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, activity.getDeal().getId());
            ps.setString(2, activity.getActivityType().name());
            ps.setString(3, activity.getStatus().name());
            ps.setString(4, activity.getDescription());

            if (activity.getDueDate() != null) {
                ps.setDate(5, java.sql.Date.valueOf(activity.getDueDate()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setTimestamp(6,
                    java.sql.Timestamp.valueOf(activity.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number id = (Number) keyHolder.getKeys().get("id");
        return id.longValue();
    }

    @Override
    public void update(Activity activity) {

        jdbcTemplate.update(
                UPDATE,
                activity.getActivityType().name(),
                activity.getStatus().name(),
                activity.getDescription(),
                activity.getDueDate() != null
                        ? java.sql.Date.valueOf(activity.getDueDate())
                        : null,
                activity.getId()
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

    private RowMapper<Activity> activityRowMapper() {
        return (rs, rowNum) -> mapActivity(rs);
    }

    // Mapping from resultset to activity
    private Activity mapActivity(ResultSet rs) throws SQLException {

        Deal deal = new Deal();
        deal.setId(rs.getLong("deal_id"));

        Activity activity = new Activity();
        activity.setId(rs.getLong("id"));
        activity.setDeal(deal);
        activity.setActivityType(
                ActivityType.valueOf(rs.getString("type"))
        );
        activity.setStatus(
                ActivityStatus.valueOf(rs.getString("status"))
        );
        activity.setDescription(rs.getString("description"));

        if (rs.getDate("due_date") != null) {
            activity.setDueDate(rs.getDate("due_date").toLocalDate());
        }

        activity.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
        );

        return activity;
    }

    public List<Activity> findRecentActivities(int limit) {
        return jdbcTemplate.query(FIND_RECENT,
                activityRowMapper(),
                limit
        );
    }

    @Override
    public boolean existsByDealId(Long dealId) {
        Integer count = jdbcTemplate.queryForObject(
                EXISTS_BY_DEAL,
                Integer.class,
                dealId
        );
        return count > 0;
    }

}