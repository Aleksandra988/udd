package com.udd.repository;

import com.udd.lucene.model.EducationLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<EducationLevel, Long> {
}
