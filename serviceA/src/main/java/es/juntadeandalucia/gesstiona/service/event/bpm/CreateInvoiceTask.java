package es.juntadeandalucia.gesstiona.service.event.bpm;

import org.springframework.stereotype.Component;

import es.juntadeandalucia.gesstiona.service.event.domain.MessageEvent;
import es.juntadeandalucia.gesstiona.service.event.domain.WFMessage;
import es.juntadeandalucia.gesstiona.service.event.domain.WorkflowExecution;
import es.juntadeandalucia.gesstiona.service.event.domain.types.ActivityType;
import es.juntadeandalucia.gesstiona.service.event.domain.types.EventType;
import es.juntadeandalucia.gesstiona.service.event.domain.types.OperationType;

@Component
public class CreateInvoiceTask implements IProcedureBPM{

	@Override
	public MessageEvent execute(WorkflowExecution wfEvent) {
		MessageEvent message = new MessageEvent();
		
		
		message.setOperationType(OperationType.WF);
		message.setWfMessage(new WFMessage(wfEvent, ActivityType.SERVICE_TASK,EventType.MESSAGE_WITH_VARS));
				
		return message;
	}

}
