package com.mococo.delivery.adapters.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mococo.delivery.domain.model.AIReport;
import com.mococo.delivery.domain.repository.AIReportRepository;

public interface JpaAIReportRepository extends AIReportRepository, JpaRepository<AIReport, UUID> {
}
