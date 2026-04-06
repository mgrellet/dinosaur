package com.dinosaur.infrastructure.out.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dinosaur.domain.model.DinosaurStatus;

@Repository
public interface DinosaurJpaRepository extends JpaRepository<DinosaurJpaEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    List<DinosaurJpaEntity> findByStatusIn(List<DinosaurStatus> statuses);

}
