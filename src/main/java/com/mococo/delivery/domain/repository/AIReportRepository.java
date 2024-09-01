package com.mococo.delivery.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.AIReport;

@NoRepositoryBean
public interface AIReportRepository extends Repository<AIReport, UUID> {
	AIReport save(AIReport aiReport);


    Page<AIReport> findByQuestionContainingIgnoreCase(String searchQuery, Pageable pageable);


    Page<AIReport> findAll(Pageable pageable);
}
