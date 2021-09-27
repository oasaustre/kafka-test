package es.orchestration.wf.client.parameter.message;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class MessageParameter {
	
	private String messageName;
	private String businessKey;
	private String processInstanceId;
	private Map<String,VarParam> processVariables;
	

}
