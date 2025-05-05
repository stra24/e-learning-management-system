package com.everrefine.elms.domain.repository;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewsRepository {

  Optional<News> findNewsById(UUID id);

  List<News> findNews(PagerForRequest pagerForRequest);

  int countNews();

  void createNews(NewsCreateCommand newsCreateCommand);

  void deleteNewsById(UUID id);
}
