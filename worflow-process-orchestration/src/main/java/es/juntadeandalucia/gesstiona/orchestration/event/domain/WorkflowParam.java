package es.juntadeandalucia.gesstiona.orchestration.event.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WorkflowParam {

	private String key;
	
}
