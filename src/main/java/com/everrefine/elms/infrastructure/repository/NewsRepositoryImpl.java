package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import com.everrefine.elms.domain.repository.NewsRepository;
import com.everrefine.elms.infrastructure.mapper.NewsMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {

  private final NewsMapper newsMapper;

  @Override
  public List<News> findNewsByIds(List<UUID> newsIds) {
    return newsMapper.findNewsByIds(newsIds);
  }

  @Override
  public List<News> findNews(PagerForRequest pagerForRequest) {
    return newsMapper.findNews(pagerForRequest);
  }

  @Override
  public int countNews(NewsSearchCondition newsSearchCondition) {
    return newsMapper.countNews(newsSearchCondition);
  }

  @Override
  public void createNews(News news) {
    newsMapper.createNews(news);
  }

  @Override
  public void deleteNewsById(UUID id) {
    newsMapper.deleteNewsById(id);
  }

  @Override
  public void updateNews(NewsForUpdateRequest news) {
    newsMapper.updateNews(news);
  }

  @Override
  public List<UUID> findNewsIdsBySearchConditions(
      NewsSearchCondition newsSearchCondition) {
    return newsMapper.findNewsIdsBySearchConditions(newsSearchCondition);
  }

  @Override
  public Optional<News> findNewsById(UUID id) {
    return newsMapper.findNewsById(id);
  }
}