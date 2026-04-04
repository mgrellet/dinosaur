package com.dinosaur.infrastructure.out.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DinosaurJpaRepository extends JpaRepository<DinosaurJpaEntity, Long> {

}
