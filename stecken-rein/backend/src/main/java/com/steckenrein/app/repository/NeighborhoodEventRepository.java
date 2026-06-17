package com.steckenrein.app.repository;

import com.steckenrein.app.entity.NeighborhoodEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NeighborhoodEventRepository extends JpaRepository<NeighborhoodEvent, Long> {
    List<NeighborhoodEvent> findAllByOrderByStartTimeAsc();
}