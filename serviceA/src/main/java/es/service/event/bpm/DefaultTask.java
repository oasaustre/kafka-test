package es.service.event.bpm;

import org.springframework.stereotype.Component;

import es.service.event.domain.MessageEvent;
import es.service.event.domain.WFMessage;
import es.service.event.domain.WorkflowExecution;

@Component
public class DefaultTask implements IProcedureBPM{

	@Override
	public MessageEvent execute(WorkflowExecution wfEvent) {
		MessageEvent message = new MessageEvent();
		
		
		message.setWfMessage(new WFMessage(wfEvent, null,null));

		
		return message;
	}

}
