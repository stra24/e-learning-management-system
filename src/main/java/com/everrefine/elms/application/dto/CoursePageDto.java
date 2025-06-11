package com.everrefine.elms.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
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
}