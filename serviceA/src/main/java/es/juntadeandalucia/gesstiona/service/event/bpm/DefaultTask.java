package es.juntadeandalucia.gesstiona.service.event.bpm;

import org.springframework.stereotype.Component;

import es.juntadeandalucia.gesstiona.service.event.domain.MessageEvent;
import es.juntadeandalucia.gesstiona.service.event.domain.WFMessage;
import es.juntadeandalucia.gesstiona.service.event.domain.WorkflowExecution;

@Component
public class DefaultTask implements IProcedureBPM{

	@Override
	public MessageEvent execute(WorkflowExecution wfEvent) {
		MessageEvent message = new MessageEvent();
		
		
		message.setWfMessage(new WFMessage(wfEvent, null,null));

		
		return message;
	}

}
