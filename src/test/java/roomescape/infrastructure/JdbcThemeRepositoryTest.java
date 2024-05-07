package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("테마를 저장한다.")
    @Test
    void shouldSaveTheme() {
        Theme theme = new Theme("테마", "테마 설명", "url");
        themeRepository.create(theme);
        int rowCount = getTotalRowCount();
        assertThat(rowCount).isEqualTo(1);
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void shouldFindAllThemes() {
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values (?, ?, ?)",
                "테마", "테마 설명", "url");
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(1);
    }

    @DisplayName("id로 테마를 조회한다.")
    @Test
    void shouldFindThemeById() {
        Theme theme = new Theme("테마", "테마 설명", "url");
        jdbcTemplate.update("insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)",
                1L, "테마", "테마 설명", "url");
        Optional<Theme> foundTheme = themeRepository.findById(1L);
        assertThat(foundTheme).isPresent();
    }

    @DisplayName("id로 테마를 삭제한다.")
    @Test
    void shouldDeleteThemeById() {
        Theme theme = new Theme("테마", "테마 설명", "url");
        jdbcTemplate.update("insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)",
                1L, "테마", "테마 설명", "url");
        themeRepository.deleteById(1L);

        int rowCount = getTotalRowCount();
        assertThat(rowCount).isZero();
    }

    @DisplayName("주어진 날짜 사이에 예약된 갯수를 기준으로 테마를 반환한다.")
    @Test
    @Sql("/insert-reservations.sql")
    void shouldReturnPopularThemes() {
        LocalDate from = LocalDate.of(2024, 12, 24);
        LocalDate to = LocalDate.of(2024, 12, 28);
        int limit = 3;

        List<Long> themeIds = themeRepository.findPopularThemesDateBetween(from, to, limit)
                .stream()
                .map(Theme::getId)
                .toList();
        assertThat(themeIds).containsExactly(4L, 3L, 2L);
    }

    private int getTotalRowCount() {
        String sql = "select count(*) from theme";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
