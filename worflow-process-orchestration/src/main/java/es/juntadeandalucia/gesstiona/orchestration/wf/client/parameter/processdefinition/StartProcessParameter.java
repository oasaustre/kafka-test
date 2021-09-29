package es.juntadeandalucia.gesstiona.orchestration.wf.client.parameter.processdefinition;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.juntadeandalucia.gesstiona.orchestration.wf.client.parameter.message.VarParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class StartProcessParameter {
	private Map<String,VarParam> processVariables;
}
