package es.juntadeandalucia.gesstiona.workflow.flow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.gesstiona.workflow.client.ProcessOrchestrateClient;
import es.juntadeandalucia.gesstiona.workflow.event.domain.WorkflowExecution;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WorkflowDelegate implements JavaDelegate{
	
	@Autowired 
	ProcessOrchestrateClient processOrchestrateClient;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		WorkflowExecution workflowEvent = null;
		log.info("Start execute Service Task");
		workflowEvent = createWorkflowEvent(execution);
		
		processOrchestrateClient.receiveActionWF(workflowEvent);
		
		
		log.info("End execute Service Task");
		//Envio de manera asíncrona del evento a Kafka
		//workflowProducer.sendWorkflowEvent(workflowEvent);
		
		
		
	}
	
	
	private WorkflowExecution createWorkflowEvent(DelegateExecution execution) {
		
		WorkflowExecution workflowEvent = new WorkflowExecution();
		
		workflowEvent.setCurrentActivityId(execution.getCurrentActivityId());
		workflowEvent.setProcessBusinessKey(execution.getProcessBusinessKey());
		workflowEvent.setProcessInstanceId(execution.getProcessInstanceId());
		
		return workflowEvent;
		
	}

}
