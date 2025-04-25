package com.everrefine.elms.application.service;

import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.application.dto.NewsPageDto;

public interface NewsApplicationService {

  NewsDto findNewsById(String NewsId);

  NewsPageDto findNews(int pageNum, int pageSize);
}
