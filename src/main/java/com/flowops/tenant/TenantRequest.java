package com.flowops.tenant;

import jakarta.validation.constraints.NotBlank;

public record TenantRequest(
        @NotBlank(message = "name is required")
        String name
) {
}