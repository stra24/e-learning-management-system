package com.everrefine.elms.domain.service;

import com.everrefine.elms.domain.model.Order;
import com.everrefine.elms.domain.repository.LessonRepository;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class LessonDomainServiceImpl implements LessonDomainService {

  private final LessonRepository lessonRepository;

  @Override
  public BigDecimal issueLessonOrder(UUID lessonGroupId) {
    return lessonRepository.findMaxLessonOrderByLessonGroupId(lessonGroupId)
        .map(maxOrder -> maxOrder.add(Order.INTERVAL_ORDER))
        .orElse(BigDecimal.ONE);
  }
}
