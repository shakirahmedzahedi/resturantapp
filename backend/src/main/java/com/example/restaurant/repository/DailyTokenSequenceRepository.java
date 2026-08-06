package com.example.restaurant.repository;

import com.example.restaurant.domain.DailyTokenSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Optional;

public interface DailyTokenSequenceRepository extends JpaRepository<DailyTokenSequence, LocalDate> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from DailyTokenSequence s where s.businessDate = :date")
    Optional<DailyTokenSequence> findForUpdate(@Param("date") LocalDate date);
}
