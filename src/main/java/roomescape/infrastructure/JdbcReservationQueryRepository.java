package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.Theme;
import roomescape.domain.dto.AvailableTimeDto;
import roomescape.infrastructure.rowmapper.ThemeRowMapper;

@Repository
public class JdbcReservationQueryRepository implements ReservationQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AvailableTimeDto> findAvailableReservationTimes(LocalDate date, long themeId) {
        String sql = """
                select rt.id, rt.start_at,
                case
                when rt.id = r.time_id then true
                else false
                end as is_booked
                from reservation_time as rt left join reservation r on r.date = ? and r.theme_id = ?;
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AvailableTimeDto(
                rs.getLong("id"),
                rs.getTime("start_at").toLocalTime(),
                rs.getBoolean("is_booked")
        ), date, themeId);
    }

    @Override
    public List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                select t.id, t.name, t.description, t.thumbnail, count(r.id) as reservation_count
                from theme as t left join reservation as r on t.id = r.theme_id
                where r.date between ? and ?
                group by t.id
                order by reservation_count desc
                limit ?
                """;

        return jdbcTemplate.query(sql, ThemeRowMapper::mapRow, startDate, endDate, limit);
    }
}