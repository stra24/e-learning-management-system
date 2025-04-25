package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.pager.PagerRequest;
import com.everrefine.elms.domain.model.pager.PagerResponse;
import com.everrefine.elms.domain.repository.NewsRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsApplicationServiceImpl implements NewsApplicationService {

  private final NewsRepository newsRepository;

  @Override
  public NewsDto findNewsById(String newsId) {
    UUID uuid = UUID.fromString(newsId);
    News news = newsRepository.findNewsById(uuid)
        .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));
    return new NewsDto(news);
  }

  @Override
  public NewsPageDto findNews(int pageNum, int pageSize) {
    PagerRequest pagerRequest = new PagerRequest(pageNum, pageSize);
    List<News> news = newsRepository.findNews(pagerRequest);
    int totalSize = newsRepository.countNews();
    PagerResponse pagerResponse = new PagerResponse(pageNum, pageSize, totalSize);
    return new NewsPageDto(news, pagerResponse);
  }
}