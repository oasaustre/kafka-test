package es.service.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.service.event.bpm.IProcedureBPM;
import es.service.event.bpm.TaskFactory;
import es.service.event.domain.MessageEvent;
import es.service.event.domain.WorkflowExecution;
import es.service.event.domain.types.EventType;
import es.service.event.domain.types.OperationType;
import es.service.event.producer.ServiceEventProducer;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceEventConsumer {
	
	@Autowired
	TaskFactory taskFactory;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ServiceEventProducer serviceEventProducer;

	@KafkaListener(topics = {"serviceA-topic"})
	public void onMessage(ConsumerRecord<String,String> consumerRecord) throws JsonMappingException, JsonProcessingException {
		
		ApplicationContext context=null;
		IProcedureBPM procedureBPM = null;
		WorkflowExecution workflowExecution = null;
		MessageEvent message = null;
		
	       try {

	             Thread.sleep(5000);

	       } catch (InterruptedException e) {
	    	   	e.printStackTrace();

	       }
		
		log.info("Consumo del evento del ConsumerRecord {}",consumerRecord);
		
		workflowExecution = objectMapper.readValue(consumerRecord.value(), WorkflowExecution.class);
		
		procedureBPM = taskFactory.getTask(workflowExecution.getCurrentActivityId());
		
		message = procedureBPM.execute(workflowExecution);
		
		if(message.getOperationType() != null && 
				message.getOperationType() == OperationType.WF &&
				message.getWfMessage().getEventType() == EventType.MESSAGE_WITH_VARS) {
			
			serviceEventProducer.sendWfEvent(message);
			log.info("Envio del evento {}",message);	
			
		}
		
		log.info("Fin de ServiceEventConsumer");		
		
	}
}
