package es.service.event.bpm;

import es.service.event.domain.MessageEvent;
import es.service.event.domain.WorkflowExecution;

public interface IProcedureBPM {
	MessageEvent execute(WorkflowExecution wfEvent);
}
