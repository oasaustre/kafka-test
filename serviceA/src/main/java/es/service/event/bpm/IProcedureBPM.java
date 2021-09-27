package es.service.event.bpm;

import es.service.event.domain.MessageWFResponse;
import es.service.event.domain.WorkflowEvent;

public interface IProcedureBPM {
	MessageWFResponse execute(WorkflowEvent wfEvent);
}
