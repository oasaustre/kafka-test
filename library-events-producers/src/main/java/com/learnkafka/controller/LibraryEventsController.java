package com.learnkafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.domain.LibraryEventType;
import com.learnkafka.producer.LibraryEventProducer;

@RestController
public class LibraryEventsController {
	
	@Autowired
	LibraryEventProducer libraryEventProducer;

	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException{
		
		libraryEvent.setType(LibraryEventType.NEW);
		//invoke kafka producer		
		libraryEventProducer.sendLibraryEventApproach2(libraryEvent);
		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);	
	}
	
	
	@PutMapping("/v1/libraryevent")
	public ResponseEntity<?> putLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException{
		
		
		if(libraryEvent.getLibraryEventId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Put libraryEventId");
		}
		
		libraryEvent.setType(LibraryEventType.UPDATE);
		//invoke kafka producer
		libraryEventProducer.sendLibraryEventApproach2(libraryEvent);
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);	
	}
}
