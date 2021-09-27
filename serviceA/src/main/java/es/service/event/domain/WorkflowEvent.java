package es.service.event.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkflowEvent {
	
	private String processInstanceId;
	private String currentActivityId;
	private String processBusinessKey;

}
