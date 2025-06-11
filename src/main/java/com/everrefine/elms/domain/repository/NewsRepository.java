package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.model.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewsRepository {

  List<News> findNewsByIds(List<UUID> newsIds);

  List<News> findNews(PagerForRequest pagerForRequest);

  int countNews(NewsSearchCondition newsSearchCondition);

  void createNews(News news);

  void deleteNewsById(UUID id);

  void updateNews(NewsForUpdateRequest news);

  List<UUID> findNewsIdsBySearchConditions(NewsSearchCondition newsSearchCondition);

  Optional<News> findNewsById(UUID id);

}
