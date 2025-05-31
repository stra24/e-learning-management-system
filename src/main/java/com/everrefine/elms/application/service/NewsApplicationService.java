package com.everrefine.elms.application.service;

import com.everrefine.elms.application.command.NewsCreateCommand;
import com.everrefine.elms.application.command.NewsSearchCommand;
import com.everrefine.elms.application.command.NewsUpdateCommand;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;
import com.everrefine.elms.presentation.request.NewsSearchRequest;

public interface NewsApplicationService {

  NewsDto findNewsById(String NewsId);

  NewsPageDto findNews(int pageNum, int pageSize);

  void createNews(NewsCreateCommand newsCreateCommand);

  void deleteNewsById(String newsId);

  void updateNews(NewsUpdateCommand newsUpdateCommand);

  NewsPageDto findSearchNews(NewsSearchCommand newsSearchCommand);
}