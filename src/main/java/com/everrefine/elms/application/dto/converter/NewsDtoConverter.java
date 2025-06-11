package com.everrefine.elms.application.dto.converter;

import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.domain.model.news.News;

public class NewsDtoConverter {
    public static NewsDto toDto(News news) {
        return new NewsDto(
                news.getId(),
                news.getTitle().getValue(),
                news.getContent().getValue(),
                news.getCreatedAt().toLocalDate(),
                news.getUpdatedAt().toLocalDate()
        );
    }
}
