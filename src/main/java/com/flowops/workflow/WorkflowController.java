package com.flowops.workflow;

import com.flowops.idempotency.IdempotencyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowRepository workflowRepository;
    private final WorkflowRunRepository workflowRunRepository;
    private final IdempotencyService idempotencyService;

    public WorkflowController(WorkflowRepository workflowRepository,
                              WorkflowRunRepository workflowRunRepository,
                              IdempotencyService idempotencyService) {
        this.workflowRepository = workflowRepository;
        this.workflowRunRepository = workflowRunRepository;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping
    public ResponseEntity<Workflow> createWorkflow(@Valid @RequestBody WorkflowRequest request) {
        Workflow workflow = new Workflow(request.tenantId(), request.name(), request.description());
        Workflow saved = workflowRepository.save(workflow);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Workflow> listWorkflows(@RequestParam UUID tenantId) {
        return workflowRepository.findByTenantId(tenantId);
    }

    @PostMapping("/{workflowId}/runs")
    public ResponseEntity<WorkflowRun> triggerRun(@PathVariable UUID workflowId,
                                                  @RequestParam UUID tenantId,
                                                  @RequestHeader("Idempotency-Key") String idempotencyKey) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow not found"));

        if (!workflow.getTenantId().equals(tenantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Workflow not found");
        }

        if (!idempotencyService.isFirstUse(idempotencyKey)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate request: this idempotency key was already used");
        }

        WorkflowRun run = new WorkflowRun(workflowId, tenantId);
        WorkflowRun saved = workflowRunRepository.save(run);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{workflowId}/runs")
    public List<WorkflowRun> listRuns(@PathVariable UUID workflowId) {
        return workflowRunRepository.findByWorkflowId(workflowId);
    }
}