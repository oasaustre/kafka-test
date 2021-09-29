package es.juntadeandalucia.gesstiona.orchestration.event.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkflowExecution {
	
	private String processInstanceId;
	private String currentActivityId;
	private String processBusinessKey;

}
