package es.camunda.workflow.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.camunda.workflow.event.domain.WorkflowEvent;


@FeignClient(name = "processOrchestrateClient",url = "${server.orchestrate.url}")
public interface ProcessOrchestrateClient {

	@PostMapping(value = "/api/process/receive")
	public ResponseEntity<WorkflowEvent> receiveActionWF(@RequestBody WorkflowEvent workflowEvent);
	
}
