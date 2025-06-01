package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.command.NewsUpdateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.domain.model.news.Content;
import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.Title;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import com.everrefine.elms.domain.model.pager.PagerForResponse;
import com.everrefine.elms.domain.repository.NewsRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsApplicationServiceImpl implements NewsApplicationService {

  private final NewsRepository newsRepository;

  @Override
  public List<NewsDto> findNewsByIds(List<String> newsIdsList) {
    List<UUID> newsUuidList = new ArrayList<>();
    newsIdsList.forEach(newsId -> {
      UUID uuid = UUID.fromString(newsId);
      newsUuidList.add(uuid);
    });
    List<News> newsList = newsRepository.findNewsByIds(newsUuidList)
        .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsIdsList));
    return new List<NewsDto>(newsList) ;
  }

  @Override
  public NewsDto findNewsById(String newsId) {
    UUID uuid = UUID.fromString(newsId);
    News news = newsRepository.findNewsById(uuid)
        .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));
    return new NewsDto(news);
  }

  @Override
  public NewsPageDto findNews(int pageNum, int pageSize) {
    PagerForRequest pagerForRequest = new PagerForRequest(pageNum, pageSize);
    List<News> news = newsRepository.findNews(pagerForRequest);
    int totalSize = newsRepository.countNews();
    PagerForResponse pagerForResponse = new PagerForResponse(pageNum, pageSize, totalSize);
    return new NewsPageDto(news, pagerForResponse);
  }

  @Override
  public void createNews(NewsCreateCommand newsCreateCommand) {
    LocalDateTime now = LocalDateTime.now();
    News news = new News(newsCreateCommand.getId(),
        new Title(newsCreateCommand.getTitle()),
        new Content(newsCreateCommand.getContent()),
        now,
        now);
    newsRepository.createNews(news);
  }

  @Override
  public void deleteNewsById(String newsId) {
    UUID uuid = UUID.fromString(newsId);
    newsRepository.findNewsById(uuid).ifPresent(news -> newsRepository.deleteNewsById(uuid));
  }

  @Override
  public void updateNews(NewsUpdateCommand newsUpdateCommand) {
    NewsDto newsDto = findNewsById(newsUpdateCommand.getId().toString());
    NewsForUpdateRequest news = new NewsForUpdateRequest(
        newsDto.getId(),
        new Title(newsUpdateCommand.getTitle()),
        new Content(newsUpdateCommand.getContent()));
    newsRepository.updateNews(news);
  }

  @Override
  public List<NewsPageDto> findSearchNews(NewsSearchCommand newsSearchCommand) {
    int totalSize = newsRepository.countNews();
    return null;
  }
}