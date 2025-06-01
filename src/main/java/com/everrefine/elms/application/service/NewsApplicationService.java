package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.command.NewsUpdateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import java.util.List;

public interface NewsApplicationService {

  List<NewsDto> findNewsByIds(List<String> newsIdsList);

  NewsPageDto findNews(int pageNum, int pageSize);

  void createNews(NewsCreateCommand newsCreateCommand);

  void deleteNewsById(String newsId);

  void updateNews(NewsUpdateCommand newsUpdateCommand);

  List<NewsPageDto> findSearchNews(NewsSearchCommand newsSearchCommands);

  NewsDto findNewsById(String id);
}