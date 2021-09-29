package es.juntadeandalucia.gesstiona.service.event.producer;

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

import es.juntadeandalucia.gesstiona.service.event.domain.MessageEvent;
import es.juntadeandalucia.gesstiona.service.event.domain.types.OperationType;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceEventProducer {
	@Autowired
	KafkaTemplate<String,String> kafkaTemplate;
	
	private String wf_topic = "wf-topic";
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	public void sendWfEvent(MessageEvent message) throws JsonProcessingException {
		String key = "";
		
		if(message.getOperationType() == OperationType.WF) {
			key = message.getWfMessage().getWorkflowExecution().getProcessInstanceId();
		}else {
			key = "external";
		}
		
		String keyCopy = key;
	
		String value = objectMapper.writeValueAsString(message);
		
		ProducerRecord<String,String> producerRecord = buildProducerRecord(wf_topic,key,value);
		
		ListenableFuture<SendResult<String, String>> listenableFuture =
				kafkaTemplate.send(producerRecord);
		
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				handleSuccess(keyCopy,value,result);
				
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(keyCopy,value,ex);
				
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
