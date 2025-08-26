package com.everrefine.elms.infrastructure.mapper;

import java.util.Optional;
import java.util.UUID;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LessonMapper {

  Optional<UUID> findFirstLessonIdByCourseId(UUID courseId);
}
