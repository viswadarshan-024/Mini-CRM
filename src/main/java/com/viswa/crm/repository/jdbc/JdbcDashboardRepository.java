package com.viswa.crm.repository.jdbc;

import com.viswa.crm.model.DealStatus;
import com.viswa.crm.repository.DashboardRepository;
import com.viswa.crm.repository.RecentProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcDashboardRepository implements DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String COUNT_COMPANIES =
            "SELECT COUNT(*) FROM companies";

    private static final String COUNT_CONTACTS =
            "SELECT COUNT(*) FROM contacts";

    private static final String COUNT_DEALS =
            "SELECT COUNT(*) FROM deals";

    private static final String COUNT_PENDING_ACTIVITIES =
            "SELECT COUNT(*) FROM activities WHERE status = 'PENDING'";

    private static final String COUNT_DEALS_BY_STATUS =
            "SELECT status, COUNT(*) AS cnt FROM deals GROUP BY status";

    private static final String RECENT_DEALS =
            "SELECT id, title, created_at " +
                    "FROM deals ORDER BY created_at DESC LIMIT ?";

    private static final String RECENT_CONTACTS =
            "SELECT id, name AS title, created_at " +
                    "FROM contacts ORDER BY created_at DESC LIMIT ?";

    @Override
    public long countCompanies() {
        return jdbcTemplate.queryForObject(COUNT_COMPANIES, Long.class);
    }

    @Override
    public long countContacts() {
        return jdbcTemplate.queryForObject(COUNT_CONTACTS, Long.class);
    }

    @Override
    public long countDeals() {
        return jdbcTemplate.queryForObject(COUNT_DEALS, Long.class);
    }

    @Override
    public long countPendingActivities() {
        return jdbcTemplate.queryForObject(
                COUNT_PENDING_ACTIVITIES,
                Long.class
        );
    }

    @Override
    public Map<DealStatus, Long> countDealsByStatus() {

        Map<DealStatus, Long> result = new EnumMap<>(DealStatus.class);

        jdbcTemplate.query(COUNT_DEALS_BY_STATUS, rs -> {
            DealStatus status =
                    DealStatus.valueOf(rs.getString("status"));
            long count = rs.getLong("cnt");
            result.put(status, count);
        });

        return result;
    }

    @Override
    public List<RecentProjection> findRecentDeals(int limit) {
        return jdbcTemplate.query(
                RECENT_DEALS,
                recentRowMapper(),
                limit
        );
    }

    @Override
    public List<RecentProjection> findRecentContacts(int limit) {
        return jdbcTemplate.query(
                RECENT_CONTACTS,
                recentRowMapper(),
                limit
        );
    }
    private RowMapper<RecentProjection> recentRowMapper() {
        return (rs, rowNum) ->
                new RecentProjectionImpl(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
    }

}
