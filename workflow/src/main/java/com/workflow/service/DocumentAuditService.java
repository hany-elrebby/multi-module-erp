package com.workflow.service;

import com.workflow.DocumentAudit;
import com.workflow.DocumentAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentAuditService {
    private final DocumentAuditRepository documentAuditRepository;

    public void recordDocumentChange(String docType, Long docId,
                                     String oldStatus, String newStatus, String action) {

        DocumentAudit documentAudit = DocumentAudit.builder()
                .documentType(docType)
                .documentId(docId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .actionTaken(action)
                .changedBy(getCurrentUser())
                .changedAt(LocalDateTime.now())
                .build();

        documentAuditRepository.save(documentAudit);
    }

    public String getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "SYSTEM";
    }
}
