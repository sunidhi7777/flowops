CREATE TABLE workflow_run (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              workflow_id UUID NOT NULL REFERENCES workflow(id),
                              tenant_id UUID NOT NULL REFERENCES tenant(id),
                              status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                              started_at TIMESTAMPTZ,
                              completed_at TIMESTAMPTZ,
                              created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                              updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_workflow_run_tenant_id ON workflow_run(tenant_id);
CREATE INDEX idx_workflow_run_workflow_id ON workflow_run(workflow_id);