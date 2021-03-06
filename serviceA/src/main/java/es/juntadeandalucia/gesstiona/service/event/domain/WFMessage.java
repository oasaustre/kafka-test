package es.juntadeandalucia.gesstiona.service.event.domain;

import es.juntadeandalucia.gesstiona.service.event.domain.types.ActivityType;
import es.juntadeandalucia.gesstiona.service.event.domain.types.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WFMessage {
	private WorkflowExecution workflowExecution;
	private ActivityType activityType;
	private EventType eventType;
	

}
