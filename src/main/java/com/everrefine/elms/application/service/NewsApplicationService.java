package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.command.NewsUpdateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;

public interface NewsApplicationService {

  NewsDto findNewsById(String id);

  NewsPageDto findSearchNews(NewsSearchCommand newsSearchCommands);

  void createNews(NewsCreateCommand newsCreateCommand);

  void updateNews(NewsUpdateCommand newsUpdateCommand);

  void deleteNewsById(String newsId);
}