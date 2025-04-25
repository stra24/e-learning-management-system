package com.everrefine.elms.domain.repository;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewsRepository {

  Optional<News> findNewsById(UUID id);

  List<News> findNews(PagerRequest pagerRequest);

  int countNews();
}
