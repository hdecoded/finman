package com.hdecoded.finman.repository;

import com.hdecoded.finman.entity.ExpenseEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    // Eagerly fetch the category so toDTO() can read it outside an open session
    // (e.g. from the scheduled summary task where OSIV does not apply).
    @EntityGraph(attributePaths = "category")
    List<ExpenseEntity> findByProfileIdAndCreatedDateBetween(
        Long profileId, LocalDateTime start, LocalDateTime end);

    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileID);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e where e.profile.id =   :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileID);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
        Long profileId,
        LocalDate startDate,
        LocalDate endDate,
        String keyword,
        Sort sort
    );

    // select * from tbl_expenses where profile_id = ?1 and date = ?2
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate,
        LocalDate endDate);
}
