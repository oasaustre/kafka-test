package es.juntadeandalucia.gesstiona.orchestration.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.juntadeandalucia.gesstiona.orchestration.event.domain.MessageEvent;
import es.juntadeandalucia.gesstiona.orchestration.event.domain.types.EventType;
import es.juntadeandalucia.gesstiona.orchestration.event.domain.types.GroupType;
import es.juntadeandalucia.gesstiona.orchestration.event.domain.types.OperationType;
import es.juntadeandalucia.gesstiona.orchestration.event.domain.types.ResourceType;
import es.juntadeandalucia.gesstiona.orchestration.wf.client.CamundaRest;
import es.juntadeandalucia.gesstiona.orchestration.wf.client.parameter.message.MessageParameter;
import es.juntadeandalucia.gesstiona.orchestration.wf.client.parameter.message.VarParam;
import es.juntadeandalucia.gesstiona.orchestration.wf.client.parameter.processdefinition.StartProcessParameter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcessOrchestrationConsumer {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	CamundaRest camundaRest;
	
	@KafkaListener(topics = {"wf-topic"})
	public void onMessage(ConsumerRecord<String,String> consumerRecord) throws JsonMappingException, JsonProcessingException {
		
		MessageEvent message = null;
		log.info("ConsumerRecord {}",consumerRecord);
		
		message = objectMapper.readValue(consumerRecord.value(), MessageEvent.class);
		
		
		if(message.getOperationType() == OperationType.WF && message.getWfMessage().getEventType() == EventType.MESSAGE_WITH_VARS) {
			MessageParameter messageParam = new MessageParameter();
			messageParam.setMessageName("InvoiceMessageEvent");
			messageParam.setProcessInstanceId(message.getWfMessage().getWorkflowExecution().getProcessInstanceId());
			messageParam.setProcessVariables(Map.of("invoiceId", new VarParam("valor","String")));
			camundaRest.message(messageParam);
		}else if(message.getOperationType() == OperationType.EXTERNAL && 
				message.getExternalMessage().getGroupType() == GroupType.PROCESS_DEFINITION &&
				message.getExternalMessage().getResourceType() == ResourceType.START_INSTANCE) {
			String key = message.getExternalMessage().getParams().getKey();
			StartProcessParameter startProcessParameter = new StartProcessParameter();
			camundaRest.startProcess(key, startProcessParameter);
			
		}
		
		log.info("Fin ProcessOrchestrationConsumer");
	}
}
