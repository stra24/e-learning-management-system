package com.everrefine.elms.presentation;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.everrefine.elms.application.service.LessonApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LessonApplicationService lessonApplicationService;

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
