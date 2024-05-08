package roomescape.infrastructure.member.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.Password;
import roomescape.domain.reservation.PlayerName;

public class MemberRowMapper {

    private MemberRowMapper() {
    }

    public static Member mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        return new Member(
                id,
                new PlayerName(name),
                new Email(email),
                new Password(password)
        );
    }
}
