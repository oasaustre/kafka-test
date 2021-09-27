package es.service.event.domain;

import es.service.event.domain.types.ActivityType;
import es.service.event.domain.types.EventType;
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
