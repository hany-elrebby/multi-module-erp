package com.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentAuditRepository extends JpaRepository<DocumentAudit, Long> {
}
