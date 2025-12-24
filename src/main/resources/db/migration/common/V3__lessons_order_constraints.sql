ALTER TABLE lessons
  ALTER COLUMN lesson_order TYPE NUMERIC(20, 10);

ALTER TABLE lessons
  ADD CONSTRAINT chk_lesson_order_positive
  CHECK (lesson_order > 0);

CREATE INDEX idx_lessons_group_order
  ON lessons(lesson_group_id, lesson_order);
