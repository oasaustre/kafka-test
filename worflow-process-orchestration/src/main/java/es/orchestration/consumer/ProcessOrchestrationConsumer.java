package es.orchestration.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.orchestration.event.domain.MessageWFResponse;
import es.orchestration.event.domain.types.EventType;
import es.orchestration.wf.client.CamundaRest;
import es.orchestration.wf.client.parameter.message.MessageParameter;
import es.orchestration.wf.client.parameter.message.VarParam;
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
		
		MessageWFResponse message = null;
		log.info("ConsumerRecord {}",consumerRecord);
		
		message = objectMapper.readValue(consumerRecord.value(), MessageWFResponse.class);
		
		if(message.getEventType() == EventType.MESSAGE) {
			MessageParameter messageParam = new MessageParameter();
			messageParam.setMessageName("InvoiceMessageEvent");
			messageParam.setProcessInstanceId(message.getWorflowEvent().getProcessInstanceId());
			messageParam.setProcessVariables(Map.of("invoiceId", new VarParam("valor","String")));
			camundaRest.message(messageParam);
		}
	}
}
