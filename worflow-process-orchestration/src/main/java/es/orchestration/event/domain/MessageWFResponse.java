package es.orchestration.event.domain;

import es.orchestration.event.domain.types.ActivityType;
import es.orchestration.event.domain.types.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageWFResponse {
	private WorkflowEvent worflowEvent;
	private ActivityType activityType;
	private EventType eventType;
	private boolean isResponse;
}
