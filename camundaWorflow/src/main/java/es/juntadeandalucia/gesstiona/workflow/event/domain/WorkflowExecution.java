package es.juntadeandalucia.gesstiona.workflow.event.domain;

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
	
	
  /*  + "activtyName='" + execution.getCurrentActivityName() + "'"
            + ", activtyId=" + execution.getCurrentActivityId()
            + ", processDefinitionId=" + execution.getProcessDefinitionId()
            + ", processInstanceId=" + execution.getProcessInstanceId()
            + ", businessKey=" + execution.getProcessBusinessKey()
            + ", executionId=" + execution.getId()
            + ", variables=" + execution.getVariables()
            + " \n\n");*/
}
