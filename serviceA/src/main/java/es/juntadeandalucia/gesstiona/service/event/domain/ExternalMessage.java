package es.juntadeandalucia.gesstiona.service.event.domain;

import es.juntadeandalucia.gesstiona.service.event.domain.types.GroupType;
import es.juntadeandalucia.gesstiona.service.event.domain.types.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExternalMessage {
	private GroupType groupType;
	private ResourceType resourceType;
	private WorkflowParam params;
}
