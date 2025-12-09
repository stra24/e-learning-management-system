package com.everrefine.elms.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.everrefine.elms.application.dto.LessonDto;
import com.everrefine.elms.application.service.LessonApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Mock
  private LessonApplicationService lessonApplicationService;

  @InjectMocks
  private LessonController lessonController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void 正常系_レッスン作成リクエストでバリデーションが通過すること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    
    LessonDto mockLessonDto = new LessonDto(
        1, 2, 1, new BigDecimal("1"), "テストレッスン", "テスト説明", 
        "https://example.com/video.mp4", LocalDateTime.now(), LocalDateTime.now()
    );
    when(lessonApplicationService.createLesson(any())).thenReturn(mockLessonDto);
    
    mockMvc.perform(
            post("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons",
                courseId, lessonGroupId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of(
                        "title", "テストレッスン",
                        "description", "テスト説明",
                        "videoUrl", "https://example.com/video.mp4"
                    )
                )))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("テストレッスン"));
    
    verify(lessonApplicationService).createLesson(any());
  }

  @Test
  void 異常系_レッスン作成時にtitleが空の場合400が返ること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    
    mockMvc.perform(
            post("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons",
                courseId, lessonGroupId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of(
                        "title", "",
                        "description", "テスト説明",
                        "videoUrl", "https://example.com/video.mp4"
                    )
                )))
        .andExpect(status().isBadRequest());
    
    verify(lessonApplicationService, never()).createLesson(any());
  }

  @Test
  void 異常系_レッスン作成時にtitleがnullの場合400が返ること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    
    mockMvc.perform(
            post("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons",
                courseId, lessonGroupId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of(
                        "description", "テスト説明",
                        "videoUrl", "https://example.com/video.mp4"
                    )
                )))
        .andExpect(status().isBadRequest());
    
    verify(lessonApplicationService, never()).createLesson(any());
  }

  @Test
  void 正常系_レッスン更新リクエストでバリデーションが通過すること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    Integer lessonId = 3;
    
    LessonDto mockLessonDto = new LessonDto(
        3, 2, 1, new BigDecimal("1"), "更新テストレッスン", "更新説明", 
        "https://example.com/new-video.mp4", LocalDateTime.now(), LocalDateTime.now()
    );
    when(lessonApplicationService.updateLesson(any())).thenReturn(mockLessonDto);
    
    mockMvc.perform(
            put("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
                courseId, lessonGroupId, lessonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of(
                        "title", "更新テストレッスン",
                        "description", "更新説明",
                        "videoUrl", "https://example.com/new-video.mp4"
                    )
                )))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.title").value("更新テストレッスン"));
    
    verify(lessonApplicationService).updateLesson(any());
  }

  @Test
  void 異常系_レッスン更新時にtitleが空の場合400が返ること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    Integer lessonId = 3;
    
    mockMvc.perform(
            put("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
                courseId, lessonGroupId, lessonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of(
                        "title", "",
                        "description", "更新説明",
                        "videoUrl", "https://example.com/new-video.mp4"
                    )
                )))
        .andExpect(status().isBadRequest());
    
    verify(lessonApplicationService, never()).updateLesson(any());
  }

  @Test
  void 異常系_レッスン作成時にJSON形式が不正の場合400が返ること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    String invalidJson = "{\"title\":\"テスト\", \"description\":\"テスト説明\", \"videoUrl\"}";
    
    mockMvc.perform(
            post("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons",
                courseId, lessonGroupId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());
    
    verify(lessonApplicationService, never()).createLesson(any());
  }

  @Test
  void 正常系_レッスン作成時にdescriptionとvideoUrlがnullでも成功すること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    
    LessonDto mockLessonDto = new LessonDto(
        1, 2, 1, new BigDecimal("1"), "テストレッスン", null, null, 
        LocalDateTime.now(), LocalDateTime.now()
    );
    when(lessonApplicationService.createLesson(any())).thenReturn(mockLessonDto);
    
    mockMvc.perform(
            post("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons",
                courseId, lessonGroupId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of("title", "テストレッスン")
                )))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("テストレッスン"));
    
    verify(lessonApplicationService).createLesson(any());
  }

  @Test
  void 正常系_レッスン更新時にdescriptionとvideoUrlがnullでも成功すること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    Integer lessonId = 3;
    
    LessonDto mockLessonDto = new LessonDto(
        3, 2, 1, new BigDecimal("1"), "更新テストレッスン", null, null, 
        LocalDateTime.now(), LocalDateTime.now()
    );
    when(lessonApplicationService.updateLesson(any())).thenReturn(mockLessonDto);
    
    mockMvc.perform(
            put("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
                courseId, lessonGroupId, lessonId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    Map.of("title", "更新テストレッスン")
                )))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.title").value("更新テストレッスン"));
    
    verify(lessonApplicationService).updateLesson(any());
  }

  @Test
  void 異常系_レッスン作成時にContentTypeが不正の場合415が返ること() throws Exception {
    Integer courseId = 1;
    Integer lessonGroupId = 2;
    
    mockMvc.perform(
            post("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons",
                courseId, lessonGroupId)
                .contentType(MediaType.TEXT_PLAIN)
                .content("{\"title\":\"テストレッスン\"}"))
        .andExpect(status().isUnsupportedMediaType());
    
    verify(lessonApplicationService, never()).createLesson(any());
  }
}
