package com.flowops.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class WorkflowRunProcessor {

    private static final Logger log = LoggerFactory.getLogger(WorkflowRunProcessor.class);

    private final WorkflowRunRepository workflowRunRepository;

    public WorkflowRunProcessor(WorkflowRunRepository workflowRunRepository) {
        this.workflowRunRepository = workflowRunRepository;
    }

    @KafkaListener(topics = "workflow-run-triggered", groupId = "flowops-workflow-processor")
    public void processRun(String runIdString) {
        UUID runId = UUID.fromString(runIdString);
        log.info("Received event to process WorkflowRun {}", runId);

        WorkflowRun run = workflowRunRepository.findById(runId)
                .orElseThrow(() -> new IllegalStateException("WorkflowRun not found: " + runId));

        run.markRunning();
        workflowRunRepository.save(run);
        log.info("WorkflowRun {} status set to RUNNING", runId);

        simulateWork();

        run.markCompleted();
        workflowRunRepository.save(run);
        log.info("WorkflowRun {} status set to COMPLETED", runId);
    }

    private void simulateWork() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}