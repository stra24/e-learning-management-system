package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.command.NewsUpdateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.application.dto.converter.NewsDtoConverter;
import com.everrefine.elms.domain.model.news.Content;
import com.everrefine.elms.domain.model.news.News;
import com.everrefine.elms.domain.model.news.NewsForUpdateRequest;
import com.everrefine.elms.domain.model.news.NewsSearchCondition;
import com.everrefine.elms.domain.model.news.Title;
import com.everrefine.elms.domain.repository.NewsRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsApplicationServiceImpl implements NewsApplicationService {

  private final NewsRepository newsRepository;

  @Override
  public NewsDto findNewsById(Integer newsId) {
    News news = newsRepository.findNewsById(newsId)
        .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsId));
    return NewsDtoConverter.toDto(news);
  }

  @Override
  public NewsPageDto findNews(NewsSearchCommand newsSearchCommand) {
    NewsSearchCondition newsSearchCondition = new NewsSearchCondition(
        newsSearchCommand.getPageNum(),
        newsSearchCommand.getPageSize(),
        newsSearchCommand.getTitle(),
        newsSearchCommand.getCreatedDateFrom(),
        newsSearchCommand.getCreatedDateTo()
    );
    List<Integer> newsIds = newsRepository.findNewsIdsBySearchConditions(newsSearchCondition);
    List<News> news = newsRepository.findNewsByIds(newsIds);
    List<NewsDto> newsDtos = news.stream()
        .map(NewsDtoConverter::toDto)
        .collect(Collectors.toList());
    int totalSize = newsRepository.countNews(newsSearchCondition);
    return new NewsPageDto(
        newsDtos,
        newsSearchCondition.getPagerForRequest().getPageNum(),
        newsSearchCondition.getPagerForRequest().getPageSize(),
        totalSize
    );
  }

  @Override
  public void createNews(NewsCreateCommand newsCreateCommand) {
    News news = News.create(newsCreateCommand.getTitle(), newsCreateCommand.getContent());
    newsRepository.createNews(news);
  }

  @Override
  public void updateNews(NewsUpdateCommand newsUpdateCommand) {
    News currentNews = newsRepository.findNewsById(newsUpdateCommand.getId())
        .orElseThrow(() -> new RuntimeException("News not found with ID: " + newsUpdateCommand.getId()));
    News updatedNews = currentNews.update(newsUpdateCommand.getTitle(), newsUpdateCommand.getContent());
    newsRepository.updateNews(updatedNews);
  }

  @Override
  public void deleteNewsById(Integer newsId) {
    newsRepository.findNewsById(newsId).ifPresent(news -> newsRepository.deleteNewsById(newsId));
  }
}