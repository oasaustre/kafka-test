package es.juntadeandalucia.gesstiona.orchestration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.juntadeandalucia.gesstiona.orchestration.event.domain.WorkflowExecution;
import es.juntadeandalucia.gesstiona.orchestration.event.mapper.FlowMapper;
import es.juntadeandalucia.gesstiona.orchestration.event.producer.WorkflowOrchestrationProducer;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value="/api/process")
public class ProcessOrchestrationController {
	
	@Autowired
	FlowMapper flowMapper;
	
	@Autowired
	WorkflowOrchestrationProducer producer;
	
	@PostMapping(value = "/receive")
	public ResponseEntity<WorkflowExecution> receiveActionWF(@RequestBody WorkflowExecution workflowExecution) throws JsonProcessingException{
		//TRatar respuesta y realizar mapeo
		String topicService = null;
		log.info("Inicio llamada API ProcessOrchestrationController");
		topicService = flowMapper.getServiceByTask(workflowExecution.getCurrentActivityId());
		log.info("Antes envio evento a Kafka de {}",topicService);
		sendEventService(topicService, workflowExecution);
		log.info("Despues envio evento a Kafka de {}",topicService);
		return ResponseEntity.status(HttpStatus.OK).body(workflowExecution);
	}
	
	private void sendEventService(String topicService,WorkflowExecution workflowEvent) throws JsonProcessingException {
		if(topicService.equalsIgnoreCase("serviceA-topic")) {
			producer.sendServiceAEvent(workflowEvent);
		} else if(topicService.equalsIgnoreCase("serviceB-topic")){
			
		}
	}
}
