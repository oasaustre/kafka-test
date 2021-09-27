package es.orchestration.event.producer;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.orchestration.event.domain.WorkflowEvent;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WorkflowOrchestrationProducer {

	
	@Autowired
	KafkaTemplate<String,String> kafkaTemplate;
	
	private String serviceA_topic = "serviceA-topic";
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	public void sendServiceAEvent(WorkflowEvent workflowEvent) throws JsonProcessingException {
		String key = workflowEvent.getProcessInstanceId();
		String value = objectMapper.writeValueAsString(workflowEvent);
		
		ProducerRecord<String,String> producerRecord = buildProducerRecord(serviceA_topic,key,value);
		
		ListenableFuture<SendResult<String, String>> listenableFuture =
				kafkaTemplate.send(producerRecord);
		
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				handleSuccess(key,value,result);
				
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key,value,ex);
				
			}
		});
	}
	
	private ProducerRecord<String, String> buildProducerRecord(String topic, String key, String value) {
		
		List<Header> recordHeaders = List.of(new RecordHeader("event-source","scanner".getBytes()),
				new RecordHeader("other-properties","other value".getBytes()));
		
		return new ProducerRecord<String,String>(topic,null,key,value,recordHeaders);
	}
	
	
	private void handleFailure(String key, String value, Throwable ex) {
		log.error("Error sending message and the Exception is {}",ex.getMessage());			
	}

	private void handleSuccess(String key, String value, SendResult<String, String> result) {
		log.info("Message sent successfully for the key: {} and the value is {}, particion is {}",key,value,result.getRecordMetadata().partition());
		
	}
	

}
