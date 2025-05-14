package com.everrefine.elms.infrastructure.mapper;

import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper {

  Optional<News> findNewsById(UUID id);

  List<News> findNews(PagerForRequest pagerForRequest);

  int countNews();

  void createNews(News news);

  void deleteNewsById(UUID id);

  void updateNews(NewsForUpdateRequest news);
}
