package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NewsMapper {

  List<News> findNewsByIds(@Param("ids") List<UUID> ids);

  List<News> findNews(PagerForRequest pagerForRequest);

  int countNews(NewsSearchCondition newsSearchCondition);

  void createNews(News news);

  void deleteNewsById(UUID id);

  void updateNews(NewsForUpdateRequest news);

  List<UUID> findNewsIdsBySearchConditions(NewsSearchCondition newsSearchCondition);

  Optional<News> findNewsById(UUID id);
}
