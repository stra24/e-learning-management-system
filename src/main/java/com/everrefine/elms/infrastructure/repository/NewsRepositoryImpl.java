package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.repository.NewsRepository;
import com.everrefine.elms.infrastructure.dao.NewsDao;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class NewsRepositoryImpl implements NewsRepository {

  private final NewsDao newsDao;

  @Override
  public List<News> findNewsByIds(List<UUID> newsIds) {
    if (newsIds.isEmpty()) {
      return Collections.emptyList();
    }
    return newsDao.findByIdIn(newsIds);
  }

  @Override
  public int countNews(NewsSearchCondition newsSearchCondition) {
    return newsDao.countNewsBySearchConditions(
        newsSearchCondition.getTitle(),
        newsSearchCondition.getCreatedDateFrom() != null ? newsSearchCondition.getCreatedDateFrom().toString() : null,
        newsSearchCondition.getCreatedDateTo() != null ? newsSearchCondition.getCreatedDateTo().toString() : null
    );
  }

  @Override
  public void createNews(News news) {
    newsDao.save(news);
  }

  @Override
  public void deleteNewsById(UUID id) {
    newsDao.deleteById(id);
  }

  @Override
  public void updateNews(NewsForUpdateRequest news) {
    newsDao.updateNewsFields(
        news.getId(),
        news.getTitle().getValue(),
        news.getContent().getValue()
    );
  }

  @Override
  public List<UUID> findNewsIdsBySearchConditions(
      NewsSearchCondition newsSearchCondition) {
    return newsDao.findNewsBySearchConditions(
        newsSearchCondition.getTitle(),
        newsSearchCondition.getCreatedDateFrom() != null ? newsSearchCondition.getCreatedDateFrom().toString() : null,
        newsSearchCondition.getCreatedDateTo() != null ? newsSearchCondition.getCreatedDateTo().toString() : null,
        newsSearchCondition.getPagerForRequest().getPageSize(),
        newsSearchCondition.getPagerForRequest().getOffset()
    );
  }

  @Override
  public Optional<News> findNewsById(UUID id) {
    return newsDao.findById(id);
  }
}