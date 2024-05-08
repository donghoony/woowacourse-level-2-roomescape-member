package roomescape.infrastructure.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.member.Member;

@JdbcTest
@Import(JdbcMemberRepository.class)
class JdbcMemberRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcMemberRepository jdbcMemberRepository;

    @DisplayName("회원을 저장한다.")
    @Test
    void saveTest() {
        Member member = new Member("name", "email@test.com", "password");
        jdbcMemberRepository.save(member);
        int totalRowCount = getTotalRowCount();
        assertThat(totalRowCount).isEqualTo(1);
    }

    @DisplayName("이메일로 회원이 존재하는지 확인한다.")
    @Test
    void existsByEmailTest() {
        String sql = "insert into member (name, email, password) values ('name', 'email@test.com', 'password')";
        jdbcTemplate.execute(sql);
        boolean existsByEmail = jdbcMemberRepository.existsByEmail("email@test.com");
        assertThat(existsByEmail).isTrue();
    }

    @Test
    @DisplayName("id로 회원을 조회한다.")
    void findByIdTest() {
        String sql = "insert into member (id, name, email, password) values (1, 'name', 'email@test.com', 'password')";
        jdbcTemplate.execute(sql);
        Optional<Member> actual = jdbcMemberRepository.findById(1);
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    void findByEmailTest() {
        String sql = "insert into member (name, email, password) values ('name', 'email@test.com', 'password')";
        jdbcTemplate.execute(sql);
        Optional<Member> actual = jdbcMemberRepository.findByEmail("email@test.com");
        assertThat(actual).isPresent();
    }


    private int getTotalRowCount() {
        String sql = "select count(*) from member";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
