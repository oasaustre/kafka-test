/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camunda.example.orderhandling;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Configuration
public class HandlerConfiguration {

	  protected static final Logger LOG = LoggerFactory.getLogger(HandlerConfiguration.class);
	  
	  protected static final ResponserQueue Queue = new ResponserQueue();
	  
	  protected static final String serializationDataFormat = "application/xml";

	  public static class ResponserQueue
	  {
		  
		  private LinkedList<ExternalTask> queue = new LinkedList<ExternalTask>();
		  private RunQueue runQueue = null;
		  
		  private class RunQueue extends Thread {
			  
			  public void run() {

				  while (queue!=null && !queue.isEmpty()) {
					  
				      // wait few seconds
				      doDelay();

				      ExternalTask externalTask = queue.getFirst();
				      queue.removeFirst();
				      
				      switch (externalTask.getTopicName()) {
				      
				      case "invoiceCreator" : 
				    	  invoiceCreatorResponser(externalTask);			      		
				      break;
				      }
				  }
		      }
		  }
		  
		  private void SendRestMessage(String URL, String oper, String jsonString) {
			  
			  // variable to be used for the final response
			  String res = "";
			  try {
				  
			        // create the client for the api restful connection
			        Client client = ClientBuilder.newClient();

			        // create the target 
			        WebTarget target = client.target(URL + oper);

			        // create our request
			        Invocation.Builder request = target.request();

			        // send our json via post to the api restful
			        Response post = request.post(Entity.json(jsonString));

			        // receive and read response in a class of type string
			        // in case the response will be of type json, we should use JsonObject.class
			        String responseJson = post.readEntity(String.class);
			        res = responseJson;
			        
			        // print request status
			        LOG.info("Status: " + post.getStatus());

			        switch (post.getStatus()) { 
			               case 200:
			               case 204:
			                       res = responseJson;
			                       break;
			               default:
			                       res = "Error";
			                       break; 
			                               }

			  } catch (Exception e) { 
			  
				    // in case of error, fill res with the exception to know what happens
			        res = e.toString();
			  } 
			  
			  // print the final response of api restful
			  LOG.info("Response: " + res);
			  
		  }
		  
		  private void invoiceCreatorResponser(ExternalTask externalTask) {

			  // set variable values
		      boolean isRandomSample = Math.random() <= 0.5;
		      String invoiceId = (isRandomSample)?"A123":null;
		      String otherData = "Extra Info";
			  
		      // create request
	  	      EventMessageRequest request = new EventMessageRequest();
	  	      request.setMessageName("Message_create_invoice");
	  	      request.setProcessInstanceId(externalTask.getProcessInstanceId());
		      request.addProcessVariable("invoiceId", invoiceId, "String");
	    	  request.addProcessVariable("otherData", otherData, "String");

			  // convert request object to json
			  Gson gson = new Gson();
			  String jsonString = gson.toJson(request);
			  LOG.info(jsonString);
			  
			  this.SendRestMessage("https://desa-1.gesstiona-andalucia.com/engine-rest/", "message", jsonString);
	 	      			  
/*			  externalTaskService.complete(externalTask); */ // en el caso que no se lance evento, sino que se complete la tarea cuando se termina la acción
			  
		      LOG.info("The External Task {}, with {}, has been completed!", externalTask.getActivityId(), isRandomSample);
		  }
		  
		  // wait some random time between 15 and 60 seconds
		  public void doDelay() {
			  
			  double delay = 0;
		      while (0.25<delay) {
		    	  delay = Math.random();
		      }
		      
		      try {
		    	  
				TimeUnit.SECONDS.sleep(Math.round(delay * 60));
		      } catch (InterruptedException e) {
				e.printStackTrace();
		      }
		  }
		  
		  public void addLast(ExternalTask t) {
			  
			  if (runQueue==null || !runQueue.isAlive()) {
				  
				  runQueue= new RunQueue();
				  runQueue.start();
			  }
			  this.queue.addLast(t);
		  }
	  }	  
	  
	  @Bean
	  @ExternalTaskSubscription("invoiceCreator")
	  public ExternalTaskHandler invoiceCreatorHandler() {
	    return (externalTask, externalTaskService) -> {
	    	
	    	Queue.addLast(externalTask);
	    	
	    	externalTaskService.complete(externalTask); /* quitarlo y completar la tarea en el método que ejecuta la acción (invoiceCreatorResponser) */

	        LOG.info("The External Task {} has been queued", externalTask.getId());

	    };
	  }

	  @Bean
	  @ExternalTaskSubscription(
	      topicName = "invoiceArchiver",
	      autoOpen = false
	  )
	  public ExternalTaskHandler invoiceArchiverHandler() {

	      // wait few seconds
		  Queue.doDelay();

	      return (externalTask, externalTaskService) -> {

	    	  
	      TypedValue typedInvoice = externalTask.getVariableTyped("invoiceId", true);
	      String invoiceId = (String) typedInvoice.getValue();
	
	      typedInvoice = externalTask.getVariableTyped("otherData", true);
	      String otherData = (String) typedInvoice.getValue();
	    	  
	      LOG.info("Invoice on process scope ARCHIVED: {}, {}", invoiceId, otherData);
	      
	      externalTaskService.complete(externalTask);
	      };
	   }

	  @Bean
	  @ExternalTaskSubscription(
	      topicName = "invoiceDiscarder",
	      autoOpen = false
	  )
	  public ExternalTaskHandler invoiceDiscarderHandler() {

	      // wait few seconds
		  Queue.doDelay();

	      return (externalTask, externalTaskService) -> {
	    	  
	      TypedValue typedInvoice = externalTask.getVariableTyped("invoiceId", true);
	      String invoiceId = (String) typedInvoice.getValue();
	
	      typedInvoice = externalTask.getVariableTyped("otherData", true);
	      String otherData = (String) typedInvoice.getValue();
	    	  
	      LOG.info("Invoice on process scope DISCARDED: {}, {}", invoiceId, otherData);
	      
	      externalTaskService.complete(externalTask);
	      };
	   }

}
