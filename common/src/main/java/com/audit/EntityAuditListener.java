package com.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EntityAuditListener {

    private final AuditLogRepository auditLogRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private void saveAuditLog(Object entity, String action, Object oldEntity, Object newEntity) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setEntityName(entity.getClass().getSimpleName());
            auditLog.setAction(action);
            auditLog.setChangedBy(getCurrentUser());
            auditLog.setChangedAt(LocalDateTime.now());

            try {
                if (oldEntity != null)
                    auditLog.setOldValue(objectMapper.writeValueAsString(oldEntity));

                if (newEntity != null)
                    auditLog.setOldValue(objectMapper.writeValueAsString(newEntity));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            var idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(entity);
            auditLog.setEntityId(idValue != null ? Long.parseLong(idValue.toString()) : null);

            auditLogRepository.save(auditLog);
        } catch (Exception e) {

        }
    }

    private String getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() ? auth.getName() : "SYSTEM";
    }
}
