package com.everrefine.elms.infrastructure.repository;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.domain.model.news.News;
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
  public Optional<News> findNewsById(UUID id) {
    return newsMapper.findNewsById(id);
  }

  @Override
  public List<News> findNews(PagerForRequest pagerForRequest) {
    return newsMapper.findNews(pagerForRequest);
  }

  @Override
  public int countNews() {
    return newsMapper.countNews();
  }


  @Override
  public void createNews(NewsCreateCommand newsCreateCommand) {
  }

  @Override
  public void deleteNewsById(UUID id) {
  }
}