package es.juntadeandalucia.gesstiona.workflow.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.juntadeandalucia.gesstiona.workflow.event.domain.WorkflowExecution;


@FeignClient(name = "processOrchestrateClient",url = "${server.orchestrate.url}")
public interface ProcessOrchestrateClient {

	@PostMapping(value = "/api/process/receive")
	public ResponseEntity<WorkflowExecution> receiveActionWF(@RequestBody WorkflowExecution workflowEvent);
	
}
