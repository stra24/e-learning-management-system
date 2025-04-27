package com.everrefine.elms.application.dto;

import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.pager.PagerForResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class CoursePageDto {

  /**
   * コースDTOリスト
   */
  private final List<CourseDto> courseDtos;
  /**
   * ページ番号
   */
  private final int pageNum;

  /**
   * 1ページ当たりの件数
   */
  private final int pageSize;

  /**
   * 総データ件数
   */
  private final int totalSize;

  public CoursePageDto(List<Course> courses, PagerForResponse pagerForResponse) {
    courseDtos = courses.stream().map(CourseDto::new).toList();
    pageNum = pagerForResponse.getPageNum();
    pageSize = pagerForResponse.getPageSize();
    totalSize = pagerForResponse.getTotalSize();
  }
}