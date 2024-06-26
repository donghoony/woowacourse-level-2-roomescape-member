package roomescape.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import roomescape.application.ThemeService;
import roomescape.application.dto.request.ThemeRequest;
import roomescape.application.dto.response.ThemeResponse;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest extends ControllerTest {
    @MockBean
    private ThemeService themeService;

    @DisplayName("테마를 생성한다.")
    @Test
    void shouldReturnCreatedTheme() throws Exception {
        ThemeRequest themeRequest = new ThemeRequest("테마", "테마 설명", "url");
        ThemeResponse themeResponse = new ThemeResponse(1L, "테마", "테마 설명", "url");
        given(themeService.create(any(ThemeRequest.class)))
                .willReturn(themeResponse);

        String themeRequestJson = objectMapper.writeValueAsString(themeRequest);
        String themeResponseJson = objectMapper.writeValueAsString(themeResponse);

        mvc.perform(post("/themes")
                        .contentType("application/json")
                        .content(themeRequestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(themeResponseJson));
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void shouldReturnAllThemes() throws Exception {
        List<ThemeResponse> themeResponses = List.of(new ThemeResponse(1L, "테마", "테마 설명", "url"));
        String themeResponseJson = objectMapper.writeValueAsString(themeResponses);

        given(themeService.findAll())
                .willReturn(themeResponses);

        mvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(content().json(themeResponseJson));
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void shouldDeleteTheme() throws Exception {
        Long themeId = 1L;
        mvc.perform(delete("/themes/" + themeId))
                .andExpect(status().isNoContent());
    }
}
