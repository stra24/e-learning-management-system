package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import java.util.List;
import java.util.Optional;

public interface NewsRepository {

  List<News> findNewsByIds(List<Integer> newsIds);

  int countNews(NewsSearchCondition newsSearchCondition);

  void createNews(News news);

  void deleteNewsById(Integer id);

  void updateNews(News news);

  List<Integer> findNewsIdsBySearchConditions(NewsSearchCondition newsSearchCondition);

  Optional<News> findNewsById(Integer id);

}
