package roomescape.infrastructure.reservation.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import roomescape.domain.reservation.PlayerName;
import roomescape.domain.reservation.Reservation;

public class ReservationRowMapper {

    private ReservationRowMapper() {
    }

    public static Reservation mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String date = rs.getString("date");
        return new Reservation(
                id,
                new PlayerName(name),
                LocalDate.parse(date),
                ReservationTimeRowMapper.mapRow(rs),
                ThemeRowMapper.mapRow(rs),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    public static Reservation joinedMapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("reservation_id");
        String name = rs.getString("reservation_name");
        String date = rs.getString("date");
        return new Reservation(
                id,
                new PlayerName(name),
                LocalDate.parse(date),
                ReservationTimeRowMapper.joinedMapRow(rs),
                ThemeRowMapper.joinedMapRow(rs),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
