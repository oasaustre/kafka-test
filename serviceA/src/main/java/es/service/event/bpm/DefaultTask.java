package es.service.event.bpm;

import org.springframework.stereotype.Component;

import es.service.event.domain.MessageWFResponse;
import es.service.event.domain.WorkflowEvent;

@Component
public class DefaultTask implements IProcedureBPM{

	@Override
	public MessageWFResponse execute(WorkflowEvent wfEvent) {
		MessageWFResponse message = new MessageWFResponse();
		
		message.setWorflowEvent(wfEvent);
		message.setResponse(Boolean.FALSE);
		
		return message;
	}

}
