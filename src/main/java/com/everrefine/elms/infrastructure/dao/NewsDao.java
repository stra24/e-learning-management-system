package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.domain.model.news.News;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDao extends CrudRepository<News, UUID> {
  
  @Query("SELECT * FROM news WHERE id IN (:ids) ORDER BY created_at DESC")
  List<News> findByIdIn(@Param("ids") List<UUID> ids);

  @Query("""
    SELECT id FROM news
    WHERE (:newsTitle IS NULL OR :newsTitle = '' OR title LIKE CONCAT('%', :newsTitle, '%'))
      AND (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE))
      AND (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
    ORDER BY created_at DESC
    LIMIT :pageSize OFFSET :offset
    """)
  List<UUID> findNewsBySearchConditions(
      @Param("newsTitle") String newsTitle,
      @Param("createdDateFrom") String createdDateFrom,
      @Param("createdDateTo") String createdDateTo,
      @Param("pageSize") int pageSize,
      @Param("offset") int offset
  );

  @Query("""
    SELECT COUNT(*) FROM news
    WHERE (:newsTitle IS NULL OR :newsTitle = '' OR title LIKE CONCAT('%', :newsTitle, '%'))
      AND (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE))
      AND (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
    """)
  int countNewsBySearchConditions(
      @Param("newsTitle") String newsTitle,
      @Param("createdDateFrom") String createdDateFrom,
      @Param("createdDateTo") String createdDateTo
  );

  @Modifying
  @Query("UPDATE news SET title = :title, content = :content, updated_at = now() WHERE id = :id")
  int updateNewsFields(
      @Param("id") UUID id,
      @Param("title") String title,
      @Param("content") String content
  );
}
