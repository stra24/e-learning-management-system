package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewsRepository {

  Optional<List<News>> findNewsByIds(List<UUID> newsIdsList);

  List<News> findNews(PagerForRequest pagerForRequest);

  int countNews();

  void createNews(News news);

  void deleteNewsById(UUID id);

  void updateNews(NewsForUpdateRequest news);

  Optional<List<UUID>> findNewsIdsBySearchConditions(NewsSearchCondition newsSearchCondition);

  Optional<News> findNewsById(UUID id);

}
