package com.flowops.workflow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record WorkflowRequest(
        @NotNull(message = "tenantId is required")
        UUID tenantId,

        @NotBlank(message = "name is required")
        String name,

        String description
) {
}