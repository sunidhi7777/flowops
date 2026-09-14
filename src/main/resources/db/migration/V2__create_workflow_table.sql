CREATE TABLE workflow (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          tenant_id UUID NOT NULL REFERENCES tenant(id),
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_workflow_tenant_id ON workflow(tenant_id);