package com.everrefine.elms.application.dto.converter;

import com.everrefine.elms.application.dto.CourseDto;
import com.everrefine.elms.application.dto.NewsDto;
import com.everrefine.elms.domain.model.course.Course;
import com.everrefine.elms.domain.model.news.News;

public class CourseDtoConverter {
    public static CourseDto toDto(Course course) {
        return new CourseDto(
                course.getId(),
                (course.getThumbnailUrl() != null)
                        ? course.getThumbnailUrl().getValue()
                        : null,
                course.getTitle().getValue(),
                (course.getDescription() != null)
                        ? course.getDescription().getValue()
                        : null
        );
    }
}
