package com.everrefine.elms.presentation;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.everrefine.elms.application.service.LessonApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

  private MockMvc mockMvc;

  @Mock
  private LessonApplicationService lessonApplicationService;

  @InjectMocks
  private LessonController lessonController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(lessonController).build();
  }

  @Test
  void 正常系_レッスン削除リクエストで204が返されサービスが呼ばれること() throws Exception {
    int courseId = 1;
    int lessonGroupId = 2;
    int lessonId = 3;

    mockMvc.perform(delete("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
            courseId, lessonGroupId, lessonId))
        .andExpect(status().isNoContent());

    verify(lessonApplicationService).deleteLessonById(lessonId);
  }

  @Test
  void 正常系_同じレッスンIDへの複数回DELETEでも常に204が返ること() throws Exception {
    int courseId = 1;
    int lessonGroupId = 2;
    int lessonId = 3;

    mockMvc.perform(delete("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
            courseId, lessonGroupId, lessonId))
        .andExpect(status().isNoContent());

    mockMvc.perform(delete("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
            courseId, lessonGroupId, lessonId))
        .andExpect(status().isNoContent());

    verify(lessonApplicationService, times(2)).deleteLessonById(lessonId);
  }

  @Test
  void 異常系_数値でないIDを指定した場合は400が返ること() throws Exception {
    int courseId = 1;
    int lessonGroupId = 2;

    mockMvc.perform(delete("/api/courses/{courseId}/lesson-groups/{lessonGroupId}/lessons/{lessonId}",
            courseId, lessonGroupId, "invalid"))
        .andExpect(status().isBadRequest());

    verify(lessonApplicationService, never()).deleteLessonById(anyInt());
  }
}
