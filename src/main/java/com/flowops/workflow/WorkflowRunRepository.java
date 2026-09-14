package com.flowops.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface WorkflowRunRepository extends JpaRepository<WorkflowRun, UUID> {
    List<WorkflowRun> findByTenantId(UUID tenantId);
    List<WorkflowRun> findByWorkflowId(UUID workflowId);
}