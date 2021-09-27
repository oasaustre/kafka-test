package es.camunda.workflow.flow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.camunda.workflow.event.domain.WorkflowEvent;
import es.camunda.workflow.event.producer.WorkflowProducer;
import lombok.extern.slf4j.Slf4j;
import es.camunda.workflow.client.ProcessOrchestrateClient;

@Component
@Slf4j
public class WorkflowDelegate implements JavaDelegate{

	/*@Autowired
	WorkflowProducer workflowProducer;*/
	
	@Autowired 
	ProcessOrchestrateClient processOrchestrateClient;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		WorkflowEvent workflowEvent = null;
		log.info("Start execute Service Task");
		workflowEvent = createWorkflowEvent(execution);
		
		processOrchestrateClient.receiveActionWF(workflowEvent);
		
		
		log.info("End execute Service Task");
		//Envio de manera asíncrona del evento a Kafka
		//workflowProducer.sendWorkflowEvent(workflowEvent);
		
		
		
	}
	
	
	private WorkflowEvent createWorkflowEvent(DelegateExecution execution) {
		
		WorkflowEvent workflowEvent = new WorkflowEvent();
		
		workflowEvent.setCurrentActivityId(execution.getCurrentActivityId());
		workflowEvent.setProcessBusinessKey(execution.getProcessBusinessKey());
		workflowEvent.setProcessInstanceId(execution.getProcessInstanceId());
		
		return workflowEvent;
		
	}

}
