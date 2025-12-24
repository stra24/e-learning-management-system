package com.everrefine.elms.domain.service;

import com.everrefine.elms.domain.model.lesson.Lesson;
import com.everrefine.elms.domain.repository.LessonRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class LessonDomainServiceImpl implements LessonDomainService {

  private final LessonRepository lessonRepository;

  @Value("${lesson.order.interval:1024}")
  private BigDecimal orderInterval;

  @Value("${lesson.order.scale:10}")
  private int orderScale;

  @Value("${lesson.order.reorder-threshold:0.0000000001}")
  private BigDecimal reorderThreshold;

  @Override
  public BigDecimal issueLessonOrder(Integer lessonGroupId) {
    return lessonRepository.findMaxLessonOrderByLessonGroupId(lessonGroupId)
        .map(maxOrder -> maxOrder.add(orderInterval))
        .orElse(BigDecimal.ONE);
  }

  @Override
  public BigDecimal calculateNewOrder(BigDecimal precedingOrder, BigDecimal followingOrder) {
    if (precedingOrder == null && followingOrder == null) {
      throw new IllegalArgumentException("Both precedingOrder and followingOrder cannot be null");
    }

    if (precedingOrder == null) {
      return followingOrder.divide(BigDecimal.valueOf(2), orderScale, RoundingMode.HALF_UP);
    }

    if (followingOrder == null) {
      return precedingOrder.add(orderInterval);
    }

    return precedingOrder.add(followingOrder)
        .divide(BigDecimal.valueOf(2), orderScale, RoundingMode.HALF_UP);
  }

  @Override
  public boolean needsReordering(Integer lessonGroupId) {
    List<Lesson> lessons = lessonRepository.findLessonsByLessonGroupId(lessonGroupId);
    if (lessons.size() < 2) {
      return false;
    }

    BigDecimal minDiff = null;
    BigDecimal previous = lessons.get(0).getLessonOrder().getValue();
    for (int i = 1; i < lessons.size(); i++) {
      BigDecimal current = lessons.get(i).getLessonOrder().getValue();
      BigDecimal diff = current.subtract(previous).abs();
      if (minDiff == null || diff.compareTo(minDiff) < 0) {
        minDiff = diff;
      }
      previous = current;
    }

    return minDiff != null && minDiff.compareTo(reorderThreshold) <= 0;
  }

  @Override
  public List<BigDecimal> reorderAllLessons(Integer lessonGroupId) {
    List<Lesson> lessons = lessonRepository.findLessonsByLessonGroupId(lessonGroupId);
    if (lessons.isEmpty()) {
      return List.of();
    }

    List<BigDecimal> newOrders = new ArrayList<>(lessons.size());
    BigDecimal currentOrder = orderInterval;
    for (Lesson lesson : lessons) {
      Lesson updatedLesson = lesson.changeOrder(currentOrder);
      lessonRepository.updateLesson(updatedLesson);
      newOrders.add(currentOrder);
      currentOrder = currentOrder.add(orderInterval);
    }

    return newOrders;
  }
}
