package com.learnkafka.producer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.LibraryEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventProducer {
	
	@Autowired
	KafkaTemplate<Integer,String> kafkaTemplate;
	
	private String topic = "test-topic";
	
	@Autowired
	ObjectMapper objectMapper;
	
	public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		
		ListenableFuture<SendResult<Integer, String>> listenableFuture =
				kafkaTemplate.sendDefault(key, value);
		
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key,value,result);
				
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key,value,ex);
				
			}
		});
	}
	
	
	public void sendLibraryEventApproach2(LibraryEvent libraryEvent) throws JsonProcessingException {
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		
		ProducerRecord<Integer,String> producerRecord = buildProducerRecord(topic,key,value);
		
		ListenableFuture<SendResult<Integer, String>> listenableFuture =
				kafkaTemplate.send(producerRecord);
		
		listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

			@Override
			public void onSuccess(SendResult<Integer, String> result) {
				handleSuccess(key,value,result);
				
			}

			@Override
			public void onFailure(Throwable ex) {
				handleFailure(key,value,ex);
				
			}
		});
	}
	
	private ProducerRecord<Integer, String> buildProducerRecord(String topic2, Integer key, String value) {
		
		List<Header> recordHeaders = List.of(new RecordHeader("event-source","scanner".getBytes()),
				new RecordHeader("other-properties","other value".getBytes()));
		
		return new ProducerRecord<Integer,String>(topic,null,key,value,recordHeaders);
	}


	public SendResult<Integer, String> sendLibraryEventSynchronous(LibraryEvent libraryEvent) throws Exception {
		
		Integer key = libraryEvent.getLibraryEventId();
		String value = objectMapper.writeValueAsString(libraryEvent);
		SendResult<Integer, String> result;
		try {
			result = kafkaTemplate.sendDefault(key, value).get();
		} catch (ExecutionException | InterruptedException e) {
			log.error("ExecutionException/InterruptedException sending message and the exception is {}",e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Exception sending message and the exception is {}",e.getMessage());
			throw e;
		}
		
		return result;
	}

	private void handleFailure(Integer key, String value, Throwable ex) {
		log.error("Error sending message and the Exception is {}",ex.getMessage());			
	}

	private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
		log.info("Message sent successfully for the key: {} and the value is {}, particion is {}",key,value,result.getRecordMetadata().partition());
		
	}

}
