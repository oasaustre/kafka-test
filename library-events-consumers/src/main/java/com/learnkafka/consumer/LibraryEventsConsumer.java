package com.learnkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LibraryEventsConsumer {

	@KafkaListener(topics = {"test-topic"})
	public void onMessage(ConsumerRecord<Integer,String> consumerRecord) {
		log.info("ConsumerRecord {}",consumerRecord);
	}
}
