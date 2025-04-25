package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerRequest;
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
  public Optional<News> findNewsById(UUID id) {
    return newsMapper.findNewsById(id);
  }

  @Override
  public List<News> findNews(PagerRequest pagerRequest) {
    return newsMapper.findNews(pagerRequest);
  }

  @Override
  public int countNews() {
    return newsMapper.countNews();
  }
}