package com.everrefine.elms.infrastructure.dao;

import com.everrefine.elms.domain.model.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {

  @Query("""
      SELECT * 
      FROM users 
      WHERE email_address = :emailAddress
      """)
  Optional<User> findByEmailAddress(@Param("emailAddress") String emailAddress);

  @Query("""
      SELECT * 
      FROM users 
      WHERE id IN (:ids) 
      ORDER BY created_at DESC
      """)
  List<User> findByIdIn(@Param("ids") List<Integer> ids);

  @Query("""
      SELECT id FROM users 
      WHERE (:userId IS NULL OR :userId = '' OR CAST(id AS TEXT) LIKE CONCAT('%', :userId, '%'))
        AND (:userRole IS NULL OR :userRole = '' OR user_role = :userRole)
        AND (:realName IS NULL OR :realName = '' OR real_name LIKE CONCAT('%', :realName, '%'))
        AND (:userName IS NULL OR :userName = '' OR user_name LIKE CONCAT('%', :userName, '%'))
        AND (:emailAddress IS NULL OR :emailAddress = '' OR email_address LIKE CONCAT('%', :emailAddress, '%'))
        AND (:createdDateFrom IS NULL OR created_at >=  CAST(:createdDateFrom AS DATE))
        AND (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
      ORDER BY created_at DESC
      LIMIT :pageSize 
      OFFSET :offset
      """)
  List<Integer> findUserIdsBySearchConditions(
      @Param("userId") String userId,
      @Param("userRole") String userRole,
      @Param("realName") String realName,
      @Param("userName") String userName,
      @Param("emailAddress") String emailAddress,
      @Param("createdDateFrom") LocalDate createdDateFrom,
      @Param("createdDateTo") LocalDate createdDateTo,
      @Param("pageSize") int pageSize,
      @Param("offset") int offset
  );

  @Query("""
      SELECT COUNT(*) 
      FROM users
      WHERE (:userId IS NULL OR :userId = '' OR CAST(id AS TEXT) LIKE CONCAT('%', :userId, '%'))
        AND (:userRole IS NULL OR :userRole = '' OR user_role = :userRole)
        AND (:realName IS NULL OR :realName = '' OR real_name LIKE CONCAT('%', :realName, '%'))
        AND (:userName IS NULL OR :userName = '' OR user_name LIKE CONCAT('%', :userName, '%'))
        AND (:emailAddress IS NULL OR :emailAddress = '' OR email_address LIKE CONCAT('%', :emailAddress, '%'))
        AND (:createdDateFrom IS NULL OR created_at >= CAST(:createdDateFrom AS DATE))
        AND (:createdDateTo IS NULL OR created_at < CAST(:createdDateTo AS DATE) + INTERVAL '1 day')
      """)
  int countUsersBySearchConditions(
      @Param("userId") String userId,
      @Param("userRole") String userRole,
      @Param("realName") String realName,
      @Param("userName") String userName,
      @Param("emailAddress") String emailAddress,
      @Param("createdDateFrom") LocalDate createdDateFrom,
      @Param("createdDateTo") LocalDate createdDateTo
  );
}
