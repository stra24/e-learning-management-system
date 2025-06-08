package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.command.NewsUpdateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.domain.model.news.Content;
import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.model.news.Title;
import com.everrefine.elms.domain.model.pager.PagerForRequest;
import com.everrefine.elms.domain.model.pager.PagerForResponse;
import com.everrefine.elms.domain.repository.NewsRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsApplicationServiceImpl implements NewsApplicationService {

  private final NewsRepository newsRepository;

  @Override
  public List<NewsDto> findNewsByIds(List<String> newsIds) {
    List<UUID> newsUUIDs = new ArrayList<>();
    newsIds.forEach(newsId -> {
      UUID uuid = UUID.fromString(newsId);
      newsUUIDs.add(uuid);
    });
    List<News> news = newsRepository.findNewsByIds(newsUUIDs);
    return news.stream().map(NewsDto::new).collect(Collectors.toList());
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
    int totalSize = newsRepository.countNews(
        new NewsSearchCondition(pageNum, pageSize, "", null, null));
    PagerForResponse pagerForResponse = new PagerForResponse(pageNum, pageSize, totalSize, "", null,
        null);
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
  public NewsPageDto findSearchNews(NewsSearchCommand newsSearchCommand) {
    NewsSearchCondition newsSearchCondition = new NewsSearchCondition(
        newsSearchCommand.getPageNum(),
        newsSearchCommand.getPageSize(),
        newsSearchCommand.getTitle(),
        newsSearchCommand.getCreatedDateFrom(),
        newsSearchCommand.getCreateDateTo()
    );
    List<UUID> newsIds = newsRepository.findNewsIdsBySearchConditions(newsSearchCondition);
    List<News> news = newsRepository.findNewsByIds(newsIds);
    List<NewsDto> newsDtos = news.stream()
        .map(NewsDto::new)
        .collect(Collectors.toList());
    int totalSize = newsRepository.countNews(newsSearchCondition);
//    PagerForResponse pagerForResponse = new PagerForResponse(
//        newsSearchCondition.getPagerForRequest().getPageNum(),
//        newsSearchCondition.getPagerForRequest().getPageSize(),
//        totalSize,
//        newsSearchCondition.getTitle(),
//        newsSearchCondition.getCreatedDateFrom(),
//        newsSearchCondition.getCreateDateTo()
//    );
    return new NewsPageDto(newsDtos,
        newsSearchCondition.getPagerForRequest().getPageNum(),
        newsSearchCondition.getPagerForRequest().getPageSize(),
        totalSize);
  }
}