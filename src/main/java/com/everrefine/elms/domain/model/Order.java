package com.everrefine.elms.domain.model;

import java.math.BigDecimal;
import lombok.Value;

/**
 * 順番を示す値オブジェクト。
 */
@Value
public class Order {
  public static BigDecimal FIRST_ORDER = new BigDecimal("1024");
  public static BigDecimal INTERVAL_ORDER = new BigDecimal("1024");
  BigDecimal value;

  public static Order getFirst() {
    return new Order(FIRST_ORDER);
  }

  public Order getNext() {
    return new Order(INTERVAL_ORDER.add(getValue()));
  }
}