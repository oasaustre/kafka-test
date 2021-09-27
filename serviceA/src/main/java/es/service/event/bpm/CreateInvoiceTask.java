package es.service.event.bpm;

import org.springframework.stereotype.Component;

import es.service.event.domain.MessageWFResponse;
import es.service.event.domain.WorkflowEvent;
import es.service.event.domain.types.ActivityType;
import es.service.event.domain.types.EventType;

@Component
public class CreateInvoiceTask implements IProcedureBPM{

	@Override
	public MessageWFResponse execute(WorkflowEvent wfEvent) {
		MessageWFResponse message = new MessageWFResponse();
		
		message.setWorflowEvent(wfEvent);
		message.setEventType(EventType.MESSAGE);
		message.setActivityType(ActivityType.SERVICE_TASK);
		message.setResponse(Boolean.TRUE);
		
		return message;
	}

}
