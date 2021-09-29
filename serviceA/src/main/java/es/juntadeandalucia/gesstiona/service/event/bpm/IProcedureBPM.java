package es.juntadeandalucia.gesstiona.service.event.bpm;

import es.juntadeandalucia.gesstiona.service.event.domain.MessageEvent;
import es.juntadeandalucia.gesstiona.service.event.domain.WorkflowExecution;

public interface IProcedureBPM {
	MessageEvent execute(WorkflowExecution wfEvent);
}
