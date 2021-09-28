package es.service.event.domain;

import es.service.event.domain.types.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageEvent {
	private OperationType operationType;
	private ExternalMessage externalMessage;
	private WFMessage wfMessage;

}
