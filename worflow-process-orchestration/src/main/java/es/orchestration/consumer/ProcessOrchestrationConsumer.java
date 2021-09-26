package es.orchestration.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcessOrchestrationConsumer {
	
	@KafkaListener(topics = {"workflow-topic"})
	public void onMessage(ConsumerRecord<String,String> consumerRecord) {
		log.info("ConsumerRecord {}",consumerRecord);
	}
}
