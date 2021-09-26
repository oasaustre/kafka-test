package es.camunda.workflow.flow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.camunda.workflow.event.domain.WorkflowEvent;
import es.camunda.workflow.event.producer.WorkflowProducer;

@Component
public class WorkflowDelegate implements JavaDelegate{

	@Autowired
	WorkflowProducer workflowProducer;
	
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		WorkflowEvent workflowEvent = null;
		
		workflowEvent = createWorkflowEvent(execution);
		
		//Envio de manera asíncrona del evento a Kafka
		workflowProducer.sendWorkflowEvent(workflowEvent);
		
		
		
	}
	
	
	private WorkflowEvent createWorkflowEvent(DelegateExecution execution) {
		
		WorkflowEvent workflowEvent = new WorkflowEvent();
		
		workflowEvent.setCurrentActivityId(execution.getCurrentActivityId());
		workflowEvent.setProcessBusinessKey(execution.getProcessBusinessKey());
		workflowEvent.setProcessInstanceId(execution.getProcessInstanceId());
		
		return workflowEvent;
		
	}

}
