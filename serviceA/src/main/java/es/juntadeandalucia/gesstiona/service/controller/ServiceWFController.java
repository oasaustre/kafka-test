package es.juntadeandalucia.gesstiona.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import es.juntadeandalucia.gesstiona.service.client.parameter.process.StartProcessClientParam;
import es.juntadeandalucia.gesstiona.service.event.domain.ExternalMessage;
import es.juntadeandalucia.gesstiona.service.event.domain.MessageEvent;
import es.juntadeandalucia.gesstiona.service.event.domain.WorkflowParam;
import es.juntadeandalucia.gesstiona.service.event.domain.types.GroupType;
import es.juntadeandalucia.gesstiona.service.event.domain.types.OperationType;
import es.juntadeandalucia.gesstiona.service.event.domain.types.ResourceType;
import es.juntadeandalucia.gesstiona.service.event.producer.ServiceEventProducer;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value="/serviceA")
public class ServiceWFController {
	
	@Autowired
	ServiceEventProducer serviceEventProducer;

	@PostMapping(value = "/startProcess/{key}")
	public void startProcess(@PathVariable String key,@RequestBody StartProcessClientParam params) throws JsonProcessingException {
		MessageEvent messageEvent = null;
		
		messageEvent = initParamStartProcess(key, params); 
		
		serviceEventProducer.sendWfEvent(messageEvent);
	}
	
	private MessageEvent initParamStartProcess(String key,StartProcessClientParam params) {
		WorkflowParam worflowParam = new WorkflowParam();
		MessageEvent messageEvent = new MessageEvent();
		ExternalMessage externalMessage = new ExternalMessage();
		
		worflowParam.setKey(key);
		
		externalMessage.setParams(worflowParam);
		
		externalMessage.setGroupType(GroupType.PROCESS_DEFINITION);
		externalMessage.setResourceType(ResourceType.START_INSTANCE);
		
		messageEvent.setOperationType(OperationType.EXTERNAL);
		messageEvent.setExternalMessage(externalMessage);
		
		worflowParam.setKey(key);
		
		
		return messageEvent;
	}
}
