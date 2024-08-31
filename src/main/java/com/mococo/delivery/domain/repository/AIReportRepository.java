package com.mococo.delivery.domain.repository;

import java.util.UUID;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import com.mococo.delivery.domain.model.AIReport;

@NoRepositoryBean
public interface AIReportRepository extends Repository<AIReport, UUID> {
	AIReport save(AIReport aiReport);
}
